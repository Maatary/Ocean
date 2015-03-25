package ch.usi.ict.dev.ois.osinfrastructure.comChannels;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ch.usi.ict.dev.AclOverSoapHttpMP.AclMessage;
import ch.usi.ict.dev.AclOverSoapHttpMP.components.MessageOutBoxAdapter;

public class MessageOutBoxOSInfrastructureAdapter implements MessageOutBoxAdapter {
	
	private BlockingQueue<AclMessage> msgQueue = null;
	
	public MessageOutBoxOSInfrastructureAdapter () {
		
		msgQueue = new LinkedBlockingQueue<AclMessage>();
	}

	@Override
	public void postMsg(AclMessage aclMessage) {
		try {
			msgQueue.put(aclMessage);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public AclMessage pickMsg() {

		AclMessage msg = null;
		
		try {
			msg = msgQueue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}

}
