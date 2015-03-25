package ch.usi.ict.dev.ois.osinfrastructure.comChannels;

import java.util.concurrent.BlockingQueue;

import ch.usi.ict.dev.AclOverSoapHttpMP.AclMessage;
import ch.usi.ict.dev.AclOverSoapHttpMP.components.MessageInBoxAdapter;

/**
 * This class is not a pure adapter per say. 
 * In this case it helps to align the access to the dispatch functionality
 * from client which is the web-service implementation class.  
 * This class do the work of dispatch for the institution as it directly contain 
 * the structure to pass the message to, that is a thread safe queue.
 */

public class MessageInBoxOSInfrastructureAdapter implements MessageInBoxAdapter {
	
	private final BlockingQueue<AclMessage> msgQueue;

	public MessageInBoxOSInfrastructureAdapter(BlockingQueue<AclMessage> msgQueue) {
		
		this.msgQueue = msgQueue;
	}
	
	/**
	 * No Need to use synchronized statement for the method
	 * as the queue access is thread safe. It is a BlockingQueue. 
	 */
	@Override
	public void postMsg(AclMessage message) {
		
		try {
			msgQueue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public AclMessage pickMsg() {
		
		AclMessage msg = null;
		try {
			msg = msgQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return msg;
	}

}
