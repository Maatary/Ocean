package ch.usi.ict.dev.ois.interactionKB.fixtures

import java.text.SimpleDateFormat

import ch.usi.ict.dev.AclOverSoapHttpMP.{AclMessage, Act, AgentIdentifier}
import ch.usi.ict.dev.langCodec.OceanRdfCodec
import ch.usi.ict.dev.ontology.{OceanCL, PayDeliveryOntology, TimeOntology}
import jade.content.abs.{AbsAgentAction, AbsConcept, AbsPredicate}
import jade.content.{ContentElement, ContentManager}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat



trait JohnToMaryMsgSkeleton {
	
		val senderName			   = "seller-john@10.62.65.103:1099/JADE"
		val receiverName           = "buyer-mary@10.62.65.103:1098/JADE"
		
		private val sender         = AgentIdentifier.getAgentIdentifierWithParams(senderName, "http://192.168.0.1")
		private val receiver       = AgentIdentifier.getAgentIdentifierWithParams(receiverName, "http://192.168.0.2")
		private val language       = "OceanCL"
		private val replywith      = "02"
		private val convId         = "testId"
		private val domainOntoIRI  = "http://www.people.lu.unisi.ch/okouyad/PayDeliveryOntology.owl#"

		
		val msgSkeleton      	   = AclMessage.createMsgFrom(Act.INFORM, sender, receiver, language, domainOntoIRI, replywith, convId)

	    
	    
}

trait DonePayLordOfRing50ContentManager {
	
	val contentmanager    = new ContentManager();
	private val langCodec = new OceanRdfCodec("OceanCL");
	private val ontology  = PayDeliveryOntology.getInstance();
	
	
	contentmanager.registerLanguage(langCodec);
	contentmanager.registerOntology(ontology);
	
	
	def getmsgWithDoneContent(msg: AclMessage): AclMessage = {
		
		contentmanager.registerLanguage(langCodec);
		contentmanager.registerOntology(ontology);
		
		val actorName = msg.getAclPayload().getSender().getName();
		val payeeName = msg.getAclPayload().getReceiver().get(0).getName();
		
		contentmanager.fillContent(msg, createDonePayLordOfTheRingContent(actorName, payeeName))
		msg
	}
	
	
	def createDonePayLordOfTheRingContent(actorName: String, payeeName: String): ContentElement = {
	
		val done = new AbsPredicate(OceanCL.Vocabulary.DONE.URIValue())
		
		//The stated Action
		val actor = new AbsConcept(OceanCL.Vocabulary.AGENT.URIValue());
		actor.set(OceanCL.Vocabulary.HAS_AID.URIValue(), actorName);
		
		val payee = new AbsConcept(OceanCL.Vocabulary.AGENT.URIValue());
		payee.set(OceanCL.Vocabulary.HAS_AID.URIValue(), payeeName);

		val pay = new AbsAgentAction(PayDeliveryOntology.Vocabulary.PAY.URIValue())
		pay.set(OceanCL.Vocabulary.HAS_ACTOR.URIValue(), actor);
		pay.set(PayDeliveryOntology.Vocabulary.HAS_PAYEE.URIValue(), payee);
		pay.set(PayDeliveryOntology.Vocabulary.HAS_AMOUNT.URIValue(), "15");
		
		done.set(OceanCL.Vocabulary.HAS_STATEDACTION.URIValue(), pay)

		
		//The stated Time
		val  dateTime      = DateTime.now()
		val  fmt           = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
		val  date          = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(dateTime.toString(fmt));
		val  instant       = new AbsConcept(TimeOntology.Vocabulary.VALID_INSTANT.URIValue());
		
		instant.set(TimeOntology.Vocabulary.HAS_TIME.URIValue(), date);
		
		done.set(OceanCL.Vocabulary.HAS_STATEDTIME.URIValue(), instant) 
		
		
		done
	}
	
	

}






