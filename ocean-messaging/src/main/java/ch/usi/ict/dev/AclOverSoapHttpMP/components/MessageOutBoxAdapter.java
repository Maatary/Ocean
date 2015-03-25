package ch.usi.ict.dev.AclOverSoapHttpMP.components;

import ch.usi.ict.dev.AclOverSoapHttpMP.AclMessage;


/**
 *
 *
 * @author Maatari Daniel OKOUYA
 */
public interface MessageOutBoxAdapter {
	
	
	/**
	 * @param aclMessage
	 * 
	 * Deliver the message that will be processed by the outgoing channel thread
	 * to send message out. This method is just a convenient method, that is, the method 
	 * is not compulsory.
	 */
	public void postMsg(AclMessage aclMessage); //Must be thread-safe
	
	
	/**
	 * This method is compulsory. 
	 * It is used by the generic outgoingChannel component to retrieve
	 * message to be posted. The method hide, whatever is the structure used to
	 * transfer message between thread, such as blocking queue. 
	 * 
	 * @return
	 */
	public AclMessage pickMsg(); //Must be thread-safe 

}
