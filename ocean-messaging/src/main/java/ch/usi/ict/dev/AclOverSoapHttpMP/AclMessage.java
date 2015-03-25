
package ch.usi.ict.dev.AclOverSoapHttpMP;

import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="acl-envelope" type="{http://www.usi.ch/ict/dev/AclOverSoapHttpMP/}acl-envelope"/>
 *         &lt;element name="acl-payload" type="{http://www.usi.ch/ict/dev/AclOverSoapHttpMP/}acl-payload"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "aclEnvelope",
    "aclPayload"
})
@XmlRootElement(name = "acl-message")
public class AclMessage {
	

    @XmlElement(name = "acl-envelope", required = true)
    protected AclEnvelope aclEnvelope;
    @XmlElement(name = "acl-payload", required = true)
    protected AclPayload aclPayload;

	/**
     * Gets the value of the aclEnvelope property.
     * 
     * @return
     *     possible object is
     *     {@link AclEnvelope }
     *     
     */
	public AclEnvelope getAclEnvelope() {
        return aclEnvelope;
    }

    /**
     * Sets the value of the aclEnvelope property.
     * 
     * @param value
     *     allowed object is
     *     {@link AclEnvelope }
     *     
     */
	public void setAclEnvelope(AclEnvelope value) {
        this.aclEnvelope = value;
    }

    /**
     * Gets the value of the aclPayload property.
     * 
     * @return
     *     possible object is
     *     {@link AclPayload }
     *     
     */
	public AclPayload getAclPayload() {
        return aclPayload;
    }

    /**
     * Sets the value of the aclPayload property.
     * 
     * @param value
     *     allowed object is
     *     {@link AclPayload }
     *     
     */
	public void setAclPayload(AclPayload value) {
        this.aclPayload = value;
    }

    /**
     * Generates a String representation of the contents of this type.
     * This is an extension method, produced by the 'ts' xjc plugin
     * 
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.MULTI_LINE_STYLE);
    }
    
	//Used for calendar conversion
	private static DatatypeFactory df          = null;
	static {
		try {
			df = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException dce) {
			throw new IllegalStateException(
					"Exception while obtaining DatatypeFactory instance", dce);
		}
	}
	
	/**
	 * Convert a date into a XMLGregorianCalendar
	 * 
	 * @param date
	 * @return
	 */
	private static XMLGregorianCalendar asXMLGregorianCalendar(Date date) {
		if (date == null) {
			return null;
		} else {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTimeInMillis(date.getTime());
			return df.newXMLGregorianCalendar(gc);
		}
	}
    
    /**
     * Provide a reply message skeleton based on the current message
     * 
     * TODO Handle multiple receiver/intended-receiver/receiver/To including managing reply-to case
     * Will have to be done in the communication module, incoming and outgoing channel
     * 
     * @return
     */
    public AclMessage getReplyMsgSkeleton() {
    	
    	AclMessage aclMsg = new AclMessage();
    	
    	//The envelope
    	
    	Params params = new Params();
    	
    	//The new To is the From of the current message and the new From is the To of the current message
    	//You should be using the reply-to field of the payload if not empty.
    	if (this.getAclPayload().getReplyTo().isEmpty())
    		params.getTo().add(this.getAclEnvelope().getParams().getFrom());
    	else
    		params.getTo().addAll(this.getAclPayload().getReplyTo());
    	
    	params.setFrom(this.getAclEnvelope().getParams().getIntendedReceiver().get(0));
    	params.setDate(asXMLGregorianCalendar(new GregorianCalendar().getTime()));
    	//handle multiple intended receiver / To
    	params.getIntendedReceiver().addAll(params.getTo());
    	params.setIndex(new BigInteger("1"));
    	
    	aclMsg.setAclEnvelope(new AclEnvelope());
    	aclMsg.getAclEnvelope().setParams(params);
    	
    	//The payload
    	
    	AclPayload aclPayload = new AclPayload();
    	
    	aclPayload.setSender(params.getFrom());
    	
    	//Is handling the multiple To/Receiver case
    	aclPayload.getReceiver().addAll(params.getTo()); 
    	
    	aclPayload.setLanguage(this.getAclPayload().getLanguage());
    	aclPayload.setLanguageRepresentation(this.getAclPayload().getLanguageRepresentation());
    	
    	aclPayload.setOntology(this.getAclPayload().getOntology());
    	
    	aclPayload.setProtocol(this.getAclPayload().getProtocol());
    	
    	aclPayload.setInReplyTo(this.getAclPayload().getReplyWith());
    	
    	aclPayload.setConversationId(this.getAclPayload().getConversationId());
    	
    	aclMsg.setAclPayload(aclPayload);
    	
    	return aclMsg;
    }
    
    public static AclMessage createMsgFrom(Act act, AgentIdentifier SenderId, AgentIdentifier ReceiverId,
    									   String language, String ontology, String replyWith, String ConvId) {
    	
    	AclMessage aclMsg = new AclMessage();
    	
    	
    	//The envelope
    	
    	Params params = new Params();
    	params.setFrom(SenderId);
    	params.getTo().add(ReceiverId);
    	params.getIntendedReceiver().addAll(params.getTo());
    	
    	params.setDate(asXMLGregorianCalendar(new GregorianCalendar().getTime()));

    	
    	params.setIndex(new BigInteger("1"));
    	
    	aclMsg.setAclEnvelope(new AclEnvelope());
    	aclMsg.getAclEnvelope().setParams(params);
    	
    	
    	//The Payload
    	
    	AclPayload aclPayload = new AclPayload();
    	
    	aclPayload.setAct(act);
    	
    	aclPayload.setSender(params.getFrom());
    	
    	//Is handling the multiple To/Receiver case
    	aclPayload.getReceiver().addAll(params.getTo()); 
    	
    	aclPayload.setLanguage(language);
    	
    	aclPayload.setOntology(ontology); 
    	
    	aclPayload.setReplyWith(replyWith);
    	
    	aclPayload.setConversationId(ConvId);
    	
    	
    	aclMsg.setAclPayload(aclPayload);
    	
    	aclMsg.setAclPayload(aclPayload);
    	
    	return aclMsg;
    	
    }

}
