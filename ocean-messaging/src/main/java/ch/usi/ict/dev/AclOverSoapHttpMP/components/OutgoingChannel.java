package ch.usi.ict.dev.AclOverSoapHttpMP.components;

//import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

//import javax.xml.ws.soap.SOAPBinding;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.usi.ict.dev.AclOverSoapHttpMP.AclMessage;
import ch.usi.ict.dev.AclOverSoapHttpMP.AclOverSoapHttpMPInterface;
import ch.usi.ict.dev.AclOverSoapHttpMP.AclOverSoapHttpMPService;
import ch.usi.ict.dev.AclOverSoapHttpMP.AgentIdentifier;

/**
 * 
 * TODO Need to handle multiple intended-receiver split
 * 
 * @author Maatari
 *
 */

public  class OutgoingChannel extends Thread {
	
	private MessageOutBoxAdapter outbox                                        = null;
	private ConcurrentHashMap<String,AclOverSoapHttpMPInterface>  proxyPortMap = null;
	private final Logger logger = LoggerFactory.getLogger("OISInfrastructure");
	
	public OutgoingChannel(MessageOutBoxAdapter msgOutBox) {
		outbox = msgOutBox;
		proxyPortMap = new ConcurrentHashMap<String, AclOverSoapHttpMPInterface>();
		logger.info("Starting OutgoingChannel created......");
		//System.out.println("OutgoingChannel created......");
	}
	
	
	
	@Override
	public void run() {
		
		logger.info("OutgoingChannel running.....");
		
		
		AclMessage msg                            = null;
		String addr                               = null;
		List<AgentIdentifier> intendedReceiverList = null;
		AgentIdentifier AgtId                      = null;
		AclOverSoapHttpMPInterface proxyPort      = null;
		
		
		while (true) {
			
			msg = outbox.pickMsg(); // must be blocking until a message is available

			// This code is not safe at all
			intendedReceiverList = msg.getAclEnvelope().getParams().getIntendedReceiver();
			/**TODO Split Message over the intended-receiver*/
			AgtId = intendedReceiverList.get(0);
			addr = AgtId.getAddress().get(0);  // Check which address to take if many are specified
			
			if ((proxyPort = proxyPortMap.get(addr)) == null) {
				try {
					AclOverSoapHttpMPService proxyClient = new AclOverSoapHttpMPService(new URL(addr + "?wsdl"));
					
					//The line to be removed, only used for port monitoring
					//proxyClient.addPort(AclOverSoapHttpMPService.AclOverSoapHttpMPPort, SOAPBinding.SOAP12HTTP_BINDING, "http://localhost:8081/ACLSoapOverHttpMP/services/AclOverSoapHttpMPPort");
					
					proxyPort = proxyClient.getAclOverSoapHttpMPPort();
					
					proxyPortMap.put(addr, proxyPort);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				} 
			} 
			//else we continue with the assigned value
			proxyPort.sendMessage(msg);
		}
	}
	
	public void shutdown () {
		
		//TODO clean exit
		
		
	}
	

}
