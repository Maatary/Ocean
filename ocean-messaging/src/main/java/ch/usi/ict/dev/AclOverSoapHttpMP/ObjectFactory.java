
package ch.usi.ict.dev.AclOverSoapHttpMP;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.usi.ict.dev.AclOverSoapHttpMP package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.usi.ict.dev.AclOverSoapHttpMP
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AclMessage }
     * 
     */
    public AclMessage createAclMessage() {
        return new AclMessage();
    }

    /**
     * Create an instance of {@link AclEnvelope }
     * 
     */
    public AclEnvelope createAclEnvelope() {
        return new AclEnvelope();
    }

    /**
     * Create an instance of {@link AclPayload }
     * 
     */
    public AclPayload createAclPayload() {
        return new AclPayload();
    }

    /**
     * Create an instance of {@link Received }
     * 
     */
    public Received createReceived() {
        return new Received();
    }

	/**
     * Create an instance of {@link Params }
     * 
     */
    public Params createParams() {
        return new Params();
    }

	/**
     * Create an instance of {@link AgentIdentifier }
     * 
     */
    public AgentIdentifier createAgentIdentifier() {
        return new AgentIdentifier();
    }

	/**
     * Create an instance of {@link AgentIdentifier }
     * 
     */
    public AgentIdentifier createAgentIdentifer() {
        return new AgentIdentifier();
    }

}
