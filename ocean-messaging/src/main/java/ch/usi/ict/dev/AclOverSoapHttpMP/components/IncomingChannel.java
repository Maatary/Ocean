package ch.usi.ict.dev.AclOverSoapHttpMP.components;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.usi.ict.dev.AclOverSoapHttpMP.AclOverSoapHttpMPInterface;
import ch.usi.ict.dev.AclOverSoapHttpMP.AclOverSoapHttpMPInterfaceImpl;

public class IncomingChannel {
	
	protected final JaxWsServerFactoryBean sf;
	protected final MessageInBoxAdapter    inbox;
	protected final WShttpAddress          address;
	
	private final Logger logger = LoggerFactory.getLogger("OISInfrastructure");
	
	
	
	
	
	public IncomingChannel(WShttpAddress address, MessageInBoxAdapter inbox) {
		
		this.address      = address;
		this.inbox         = inbox;
		this.sf           = new JaxWsServerFactoryBean();
		String list = sf.getFeatures().toString();
		
	}
	
	public void start() {
		
		AclOverSoapHttpMPInterface implementor = new AclOverSoapHttpMPInterfaceImpl(inbox);
		
		//sf.setServiceClass(AclOverSoapHttpMPInterface.class);
		//sf.setWsdlLocation("./WebContent/wsdl/AclOverSoapHttpMP.wsdl");
		sf.setServiceBean(implementor);
	    sf.setAddress(address.getStringAddress());
	    
	    logger.info("Starting Incomingchannel.... on  adress: " + address.getStringAddress());
	    sf.create();
	   
	    logger.info("Incomingchannel ready listening ....");
	    
		
	}
	
	public synchronized void  shutdown() {
		
		System.out.println("stopping the server ....");
		sf.getServer().stop();
		
		System.out.println("destroying the server ....");
		sf.getServer().destroy();
		
		System.out.println("Incomming channel stop ....");
		
	}
	
	public String getListeningAddress () {
		
		return address.getStringAddress();
	}

}
