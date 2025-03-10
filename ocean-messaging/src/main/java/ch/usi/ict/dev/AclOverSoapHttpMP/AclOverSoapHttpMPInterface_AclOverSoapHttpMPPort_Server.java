
package ch.usi.ict.dev.AclOverSoapHttpMP;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.xml.ws.Endpoint;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

//import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

import ch.usi.ict.dev.AclOverSoapHttpMP.components.MessageInBoxAdapter;

/**
 * This class was generated by Apache CXF 2.3.2
 * 2011-01-27T17:10:53.531+01:00
 * Generated source version: 2.3.2
 * 
 */
 
public class AclOverSoapHttpMPInterface_AclOverSoapHttpMPPort_Server{

    protected AclOverSoapHttpMPInterface_AclOverSoapHttpMPPort_Server(BlockingQueue<AclMessage> msgQueue) throws Exception {
        System.out.println("Starting Server");
        
        Object implementor = new AclOverSoapHttpMPInterfaceImpl(new MessageInBoxAdapter( ) {
        	
        	private final BlockingQueue<AclMessage> msgQueue = new LinkedBlockingQueue<AclMessage>();

			@Override
			public void postMsg(AclMessage aclMessage) {
				try {
					msgQueue.put(aclMessage);
				} catch (InterruptedException e) {					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public AclMessage pickMsg() {
				// TODO Auto-generated method stub
				return null;
			}
		});
        String address = "http://localhost:8080/ACLSoapOverHttpMP/services/AclOverSoapHttpMPPort";
        JaxWsServerFactoryBean sf = new JaxWsServerFactoryBean();
        //sf.setServiceClass(AclOverSoapHttpMPInterface.class);
        sf.setServiceBean(implementor);
	    sf.setAddress(address);
	    System.out.println("Starting Incomingchannel.... on  adress: " + address);
	    sf.create();
       // Endpoint.publish(address, implementor);
    }
    
    public static void main(String args[]) throws Exception { 
    	BlockingQueue<AclMessage> msgQueue = new LinkedBlockingQueue<AclMessage>();
    	AclMessage msg                     = null;
    	
        new AclOverSoapHttpMPInterface_AclOverSoapHttpMPPort_Server(msgQueue);
        
        while (true) {
        	msg = msgQueue.take();
        	System.out.println(">>>>>>>\n The message content is" + msg.getAclPayload().getContent() + "\n >>>>>>>>>");
        }
        
        /*System.out.println("Server ready..."); 
        
        Thread.sleep(5 * 60 * 1000); 
        System.out.println("Server exiting");
        System.exit(0);*/
    }
}
