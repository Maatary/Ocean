package ch.usi.ict.dev.ois.osinfrastructure;

import jade.content.ContentManager;
import jade.content.abs.AbsAgentAction;
import jade.content.abs.AbsAggregate;
import jade.content.abs.AbsConcept;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;




import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ch.usi.ict.dev.langCodec.OceanRdfCodec;
import ch.usi.ict.dev.ois.gui.OISGui;
import ch.usi.ict.dev.ois.interactionKB.InteractionKB;
import ch.usi.ict.dev.ois.interactionKB.KbConfigModule;
import ch.usi.ict.dev.ois.interactionKB.OISconfigProfile;
import ch.usi.ict.dev.ois.interactionKB.OISconfigProfileImpl;
import ch.usi.ict.dev.ois.osinfrastructure.comChannels.MessageInBoxOSInfrastructureAdapter;
import ch.usi.ict.dev.ois.osinfrastructure.comChannels.MessageOutBoxOSInfrastructureAdapter;
import ch.usi.ict.dev.ontology.OceanCL;
import ch.usi.ict.dev.ontology.PayDeliveryOntology;
import ch.usi.ict.dev.ontology.PlatformOntology;
import ch.usi.ict.dev.ontology.TimeOntology;

//import org.eclipse.jface.window.ApplicationWindow;

import ch.usi.ict.dev.AclOverSoapHttpMP.AclMessage;
import ch.usi.ict.dev.AclOverSoapHttpMP.Act;
import ch.usi.ict.dev.AclOverSoapHttpMP.AgentIdentifier;
import ch.usi.ict.dev.AclOverSoapHttpMP.components.IncomingChannel;
import ch.usi.ict.dev.AclOverSoapHttpMP.components.OutgoingChannel;
import ch.usi.ict.dev.AclOverSoapHttpMP.components.WShttpAddress;


public class OISInfrastructureService implements Runnable {

	
	private MessageOutBoxOSInfrastructureAdapter outbox 				   = new MessageOutBoxOSInfrastructureAdapter();
    private MessageInBoxOSInfrastructureAdapter inbox   				   = new MessageInBoxOSInfrastructureAdapter(new LinkedBlockingQueue<AclMessage>());
	
    
    
    //Injected in the constructor
	OISGui gui                                                             = null;
	private ConcurrentHashMap<String, AgentIdentifier>  registeredAgentMap = null;
	
	
	
	//Created in the start
	private OutgoingChannel outChannel                  				   = null;
	private IncomingChannel inChannel                   			 	   = null;
	private AclMessage      servicedMsg                     			   = null;
	private String          lPort                                		   = null;
	private WShttpAddress   hta                           				   = null;
	private String          AMSP_NAME                                      = null;

	
	
	private ContentManager contentmanager 								   = null;
	
	private int            count										   = 0;
	private InteractionKB  kb;
		
	public OISInfrastructureService(String option, ConcurrentHashMap<String, AgentIdentifier> aregisteredAgentMap, OISGui agui) {	
	
		outbox             = new MessageOutBoxOSInfrastructureAdapter();
		inbox              = new MessageInBoxOSInfrastructureAdapter(new LinkedBlockingQueue<AclMessage>());
		lPort              = option;
		registeredAgentMap = aregisteredAgentMap;//use to create the map here.
		gui                = agui;
		
		//start();
		
	}
	
	


	
	@Override
	public void run() {
		start();
		//doProcessingCycle();
	}
	
	
	public void start() {
		
		contentmanager          = new ContentManager();
		OceanRdfCodec langCodec = new OceanRdfCodec("OceanCL");
		Ontology ontology		= PayDeliveryOntology.getInstance();
		contentmanager.registerLanguage(langCodec);
		contentmanager.registerOntology(ontology);
		
		Logger logger = LoggerFactory.getLogger("OIS");
		
		logger.info("...Initiating KB");
		
		//System.out.println("...Initiating KB");
		
		//kb = KbFactory.createKb("http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl", "data/main/reasoningOntos/PayDeliveryOntology.owl");

        //TODO Load the file with the help of the class loader
        ClassLoader cl = OISInfrastructureService.class.getClassLoader();

		OISconfigProfile config = new OISconfigProfileImpl("http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl");
		Injector injector = Guice.createInjector(new KbConfigModule(config));
		kb = injector.getInstance(InteractionKB.class);
		
		logger.info("...KB Ready");
		//System.out.println("...KB Ready");
		
		logger.info("...Initiating Messaging Layer\n\n\n");
		//System.out.println("...Initiating Messaging Layer");
		
		try {
			hta = new WShttpAddress(InetAddress.getLocalHost().getCanonicalHostName(),Integer.parseInt(lPort), false);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		AMSP_NAME = "AMSP@" + hta.getStringAddress();
		
		inChannel  = new IncomingChannel(hta, inbox);
		outChannel = new OutgoingChannel(outbox);
		
		inChannel.start();
		outChannel.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		logger.info("\n\n\n...Messaging Layer Ready\n");
		System.out.println("...Messaging Layer Ready\n");

		doProcessingCycle();
	}
	
	protected void doProcessingCycle() {

		while (true) {
			getIncomingmsg();
			doOSInfrastructureService();
			setOutgoingmsg();
		}
		
	}
	
	/**
	 * 
	 * Check the message receiver and forward it to the proper service providers :
	 * 
	 * (If necessary such as if there is multiple intended receiver split the message first)
	 * 
	 * 1-/ If the intended-receiver is an outsider the message is submitted to 
	 * 	   the message forward pre-processing service provider (MFPSP)
	 * 
	 * 2-/ If the intended-receiver is the agent management service provider (AMSP)
	 * 	   the message is submitted to it.
	 * 
	 */
	
	protected void doOSInfrastructureService() {

		AgentIdentifier agtId = servicedMsg.getAclEnvelope().getParams().getIntendedReceiver().get(0);
		
		if (agtId.getName().equalsIgnoreCase(AMSP_NAME)) 
			doAMSPservice();
		else 
			doMFPSPservice();
	
	}


	/**
	 * Put the servicedMsg in the outgoing queue.
	 *  
	 */
	private void setOutgoingmsg() {
		if (servicedMsg != null)
			outbox.postMsg(servicedMsg);
	}

	/**
	 * Take incoming message out of the incoming queue 
	 * and set the servicedMsg with it. 
	 * 
	 */
	private void getIncomingmsg() {
		servicedMsg = inbox.pickMsg();
	}
		
	
	protected void shutDownBasicInsitutionServices() {
		outChannel.shutdown();
		inChannel.shutdown();
		return;
	}
	
	/**
	 * Temporary method, simulate the service done by the
	 * agent management service provider
	 * @param registered 
	 * 
	 */
	private void doAMSPservice() {

		if (servicedMsg.getAclPayload().getAct() == Act.REQUEST ) {

			AbsAgentAction absAction = null;	

			try {

				// Retrieve the abstractContent, it must be an action
				absAction = (AbsAgentAction) contentmanager.extractAbsContent(servicedMsg);

				if (absAction == null) {
					doNothingSettings();
					return;
				}
				//If the content is "registerSelf execute the action only if the name of the sender chosen is not already in use
				if (absAction.getTypeName().equalsIgnoreCase(PlatformOntology.Vocabulary.REGISTER_SELF.URIValue()) ){

					AgentIdentifier sender = servicedMsg.getAclPayload().getSender();

					if (!isAgtNameInUse(sender.getName())) {// register and prepare inform confirmation message

						registeredAgentMap.put(sender.getName(), sender);
						gui.updateLogView(prettyPrintmsg(servicedMsg));
						
						servicedMsg = servicedMsg.getReplyMsgSkeleton();
						servicedMsg.getAclPayload().setAct(Act.INFORM);
						AbsPredicate done = new AbsPredicate(OceanCL.Vocabulary.DONE.URIValue());
						done.set(OceanCL.Vocabulary.HAS_STATEDACTION.URIValue(), absAction);
						done.set(OceanCL.Vocabulary.HAS_STATEDTIME.URIValue(), createValidInstant(DateTime.now().toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ"))));
						
						
						
						contentmanager.fillContent(servicedMsg, done);

						gui.updateLogView(prettyPrintmsg(servicedMsg));
						gui.updateAgtListView();

					}
					else { // Already in use
						gui.updateLogView(prettyPrintmsg(servicedMsg));
						servicedMsg = servicedMsg.getReplyMsgSkeleton();
						servicedMsg.getAclPayload().setAct(Act.REFUSE);
						
						//TODO Provide a sound method for proposition handling (already in use).
						AbsPredicate statement = new AbsPredicate(OceanCL.Vocabulary.REIFIED_STATEMENT.URIValue());
						//this is the reply message,  hence the subject is taken from "in reply to"
						statement.set(OceanCL.Vocabulary.HAS_SUBJECT.URIValue(), hta.getStringAddress() + "#AMSP_REGISTRY" ); 
						statement.set(OceanCL.Vocabulary.HAS_PREDICATE.URIValue(), PlatformOntology.Vocabulary.HAS_NAME_IN_USE.URIValue());
						
						AbsConcept rdfRessourceObject = new AbsConcept(OceanCL.Vocabulary.RDF_RESSOURCE.URIValue());
						rdfRessourceObject.set(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_VALUE.URIValue(), sender.getName());
						rdfRessourceObject.set(OceanCL.Vocabulary.HAS_RDF_RESSOURCE_TYPE.URIValue(), BasicOntology.STRING);

						statement.set(OceanCL.Vocabulary.HAS_OBJECT.URIValue(), rdfRessourceObject);

						contentmanager.fillContent(servicedMsg, statement);
						
						gui.updateLogView(prettyPrintmsg(servicedMsg));
					}

					return;
				}

				//TODO Handle List. Must be defined in the Ontology etc... how to handle list of element case ?
				if (absAction.getTypeName().equalsIgnoreCase(PlatformOntology.Vocabulary.LISTPARTICIPANT.URIValue())) {

					new AbsAggregate(BasicOntology.SEQUENCE);
					StringBuffer ParticipantList = new StringBuffer();
					ParticipantList.append("[");
					for (Iterator<AgentIdentifier> iterator = registeredAgentMap.values().iterator(); iterator.hasNext();) {
						AgentIdentifier participant =  iterator.next();
						//When Listing the participants, the address has to be the one of the intermediary
						ParticipantList.append("(Name: ").append(participant.getName()).append(" | Address: ").append(hta.getStringAddress()).append(")\n");              
					} 

					ParticipantList.append("]");

					gui.updateLogView(prettyPrintmsg(servicedMsg));
					servicedMsg = servicedMsg.getReplyMsgSkeleton();
					servicedMsg.getAclPayload().setAct(Act.INFORM);
					servicedMsg.getAclPayload().setContent(ParticipantList.toString()); //Should be a statement 
					gui.updateLogView(prettyPrintmsg(servicedMsg));

					return;
				}

				doNothingSettings();
				return;

			} catch (CodecException e) {
				doNothingSettings();
				e.printStackTrace();
				return;
			} catch (OntologyException e) {
				doNothingSettings();
				e.printStackTrace();
				return;
			} catch ( ClassCastException e) {
				doNothingSettings();
				e.printStackTrace();
				return;
			}
			
			
		} // If it is a request
		
		//Unsupported performtive
		doNothingSettings();
		return;
	}
	


	/**
	 * This is the message forwarding service. 
	 * TODO it sounds like cases where there might be multiple receiver (intended or not) is not handled.
	 */
	private void doMFPSPservice() {
		
		AgentIdentifier sender   = servicedMsg.getAclEnvelope().getParams().getFrom();
		// At this stage there should be only one intended receiver in the field, indeed if necessary the message shall have been split already
		AgentIdentifier receiver = servicedMsg.getAclEnvelope().getParams().getIntendedReceiver().get(0);
	
		if (!isSenderRegistered(sender)){//if the sender is registered 
			
			prepareUnknownSenderErrorMsg();	
			return;
			
		}
		
		if (!isReceiverKnown(receiver)) {//If the receiver exist
			
			prepareUnknowReceiverErrorMsg();
			return;
		}
		
		//Message forwarding alright, so we print the message
		gui.updateLogView(prettyPrintmsg(servicedMsg));
		
		
		
		//TODO update the Ontology. Here we treat the semantic of the message
		
		
		count = kb.updateStateOnCommunicativeAct(servicedMsg, contentmanager, count);

		
		
	
		
		
		//Get the registered receiver AID that contains its real address
		AgentIdentifier registeredReceiver = registeredAgentMap.get(receiver.getName());
		
		/*Replace the receiver in the servicedMsg
		**In the envelope: TO/intended-receiver and in the Payload: receiver/reply-to*/
		
		//Intended-receiver
		//TODO Handle the case where you may have an agtId represented many times in the list. Tips: use collection and removeall
		servicedMsg.getAclEnvelope().getParams().getIntendedReceiver().remove(receiver);
		servicedMsg.getAclEnvelope().getParams().getIntendedReceiver().add(registeredReceiver);
		//TO
		//TODO Handle the case where you may have an agtId represented many times in the list. Tips: use collection and removeall
		servicedMsg.getAclEnvelope().getParams().getTo().remove(receiver);
		servicedMsg.getAclEnvelope().getParams().getTo().add(registeredReceiver);
		
		//receivers
		servicedMsg.getAclPayload().getReceiver().remove(receiver);
		servicedMsg.getAclPayload().getReceiver().add(registeredReceiver);
		
		//TODO Implement the reply-to case, with the proper semantic (choose it fast)
		
		
		//In the sender replace the from address with the one from the open system infrastructure, in both the envelope and payload
		//TODO Need to properly handle multiple address case
		sender.getAddress().remove(0);
		sender.getAddress().add(hta.getStringAddress());
		
		//This sender is the one from the envelope From field. So we only need to update by replacement, the one from the payload
		servicedMsg.getAclPayload().setSender(sender);
		
	}
	
	
	/**
	 * Given that the java ontology only deal with the Date type which is not the xs:dateTime
	 * used in the OWL/RDF ontology, we allow ourlseves, to write things in the xs:dateTime 
	 * style and let this function do the appropriate translation.
	 * More specifically joda.DateTime knows how to read yyyy-MM-dd'T'HH:mm:ssZZ and output 
	 * yyyy-MM-dd'T'HH:mm:ssZ which is the format that Date knows how to read and manipulate
	 * 
	 * @param time
	 * @return
	 */
	private AbsConcept createValidInstant(String time) {
		DateTime          dateTime          = new DateTime(time);
		DateTimeFormatter fmt               = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
		AbsConcept        validInstant      = (AbsConcept) new AbsConcept(TimeOntology.Vocabulary.VALID_INSTANT.URIValue());
		Date              date              = null;
		
		
			try {
				date = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ssZ").parse(dateTime.toString(fmt));
			} catch (ParseException e) {
				e.printStackTrace();
				System.err.println("Problem parsing time in CreateValidInstant");
			}
			
			validInstant.set(TimeOntology.Vocabulary.HAS_TIME.URIValue(), date);
			
		return validInstant;
	}

	/**
	 * this method just says if the combination of the agent
	 * name with the agent physical address is already registered
	 * together.
	 * @param sender 
	 * @return
	 */
	private boolean isSenderRegistered(AgentIdentifier sender) {
		
		AgentIdentifier registeredSender = registeredAgentMap.get(sender.getName());
		
		if (registeredSender == null)
			return false;
		
		//TODO Adapt for the multiple address case
		if (registeredSender.getAddress().get(0).equalsIgnoreCase(sender.getAddress().get(0)))
			return true;
		
		return false;
	}

	private boolean isReceiverKnown(AgentIdentifier receiver) {
		
		return registeredAgentMap.containsKey(receiver.getName());
	}
	
	private boolean isAgtNameInUse(String agtName) {
		
		return registeredAgentMap.containsKey(agtName);
	}

	private void prepareUnknownSenderErrorMsg() {
		gui.updateLogView(prettyPrintmsg(servicedMsg));
		servicedMsg = servicedMsg.getReplyMsgSkeleton();
		servicedMsg.getAclPayload().setAct(Act.REFUSE);
		//TODO handle "unknown sender" message with ontology
		//servicedMsg.getAclPayload().setContent("unknown sender");
		servicedMsg.getAclPayload().setContent("");
		gui.updateLogView(prettyPrintmsg(servicedMsg));
	}
	
	private void prepareUnknowReceiverErrorMsg() {
		gui.updateLogView(prettyPrintmsg(servicedMsg));
		servicedMsg = servicedMsg.getReplyMsgSkeleton();
		servicedMsg.getAclPayload().setAct(Act.FAILURE);
		//TODO handle "unknown receiver" message with ontology
		//servicedMsg.getAclPayload().setContent("unknown reicever");
		servicedMsg.getAclPayload().setContent("");
		gui.updateLogView(prettyPrintmsg(servicedMsg));
	}
	
	private void doNothingSettings() {
		gui.updateLogView(prettyPrintmsg(servicedMsg));
		servicedMsg = servicedMsg.getReplyMsgSkeleton();
		servicedMsg.getAclPayload().setAct(Act.NOT_UNDERSTOOD);
		servicedMsg.getAclPayload().setContent("");
		gui.updateLogView(prettyPrintmsg(servicedMsg));
		
	}
	
	private String prettyPrintmsg(AclMessage aclmsg) {
		
		StringBuffer msg = new StringBuffer();
		
		msg.append("*****\n--> Agent: " + aclmsg.getAclPayload().getSender().getName() + " \n" + "    Sent message: \n");
		msg.append("[ " + aclmsg.getAclPayload().getAct().value() + ": " + "\"" + aclmsg.getAclPayload().getContent() + "\""+ " ]\n");
		msg.append("to\n -->Agent:" + aclmsg.getAclEnvelope().getParams().getIntendedReceiver().get(0).getName() + "\n\n*****\n");
		
		return msg.toString();
	}

	
	
}
