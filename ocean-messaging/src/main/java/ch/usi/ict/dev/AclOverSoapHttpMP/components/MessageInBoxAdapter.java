package ch.usi.ict.dev.AclOverSoapHttpMP.components;

import ch.usi.ict.dev.AclOverSoapHttpMP.AclMessage;

/**
 * 
 * @author Maatari Daniel OKOUYA
 * 
 * Adapts the custom dispatcher or structure used to dispatch the message
 * in the internal of the overall system that obviously run in another thread.
 *
 */

public interface MessageInBoxAdapter {
	
	
	/**
	 * This method must be thread safe.
	 * Indeed the web-service implementation instance is shared among the many
	 * thread of incoming request of service connection. Given that it calls the inBox method
	 * postMsg, to post the message inside the system where other thread might be waiting to reading the incoming data, 
	 * this method must be thread safe.
	 * 
	 * This method is compulsory (It is used by the generic inComingChannel)
	 * 
	 * Need to be thread-safe
	 * 
	 * @param aclMessage
	 */
	public void postMsg(AclMessage aclMessage);
	
	
	
	/**
	 * This method is not compulsory as it is meant as a convenience method to be
	 * used by the encompassing internal systems. The internal systems retrieve message depending
	 * on the internal structure used for message transfer between thread.
	 * 
	 * Need to be thread-safe
	 * @return
	 */
	public AclMessage pickMsg();

}
