
package ch.usi.ict.dev.AclOverSoapHttpMP;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;


/**
 * <p>Java class for acl-payload complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="acl-payload">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sender" type="{http://www.usi.ch/ict/dev/AclOverSoapHttpMP/}agent-identifier"/>
 *         &lt;element name="receiver" type="{http://www.usi.ch/ict/dev/AclOverSoapHttpMP/}agent-identifier" maxOccurs="unbounded"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="conversation-id" type="{http://www.w3.org/2001/XMLSchema}ID" minOccurs="0"/>
 *         &lt;element name="reply-to" type="{http://www.usi.ch/ict/dev/AclOverSoapHttpMP/}agent-identifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reply-with" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="in-reply-to" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reply-by" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="language-representation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ontology" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="protocol" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="act" use="required" type="{http://www.usi.ch/ict/dev/AclOverSoapHttpMP/}act" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "acl-payload", propOrder = {
    "sender",
    "receiver",
    "content",
    "conversationId",
    "replyTo",
    "replyWith",
    "inReplyTo",
    "replyBy",
    "language",
    "languageRepresentation",
    "ontology",
    "protocol"
})
public class AclPayload {

    @XmlElement(required = true)
    protected AgentIdentifier sender;
    @XmlElement(required = true)
    protected List<AgentIdentifier> receiver;
    @XmlElement(required = true)
    protected String content;
    @XmlElement(name = "conversation-id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String conversationId;
    @XmlElement(name = "reply-to")
    protected List<AgentIdentifier> replyTo;
    @XmlElement(name = "reply-with")
    protected String replyWith;
    @XmlElement(name = "in-reply-to")
    protected String inReplyTo;
    @XmlElement(name = "reply-by")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar replyBy;
	protected String language;
    @XmlElement(name = "language-representation")
    protected String languageRepresentation;
	protected String ontology;
	protected String protocol;
    @XmlAttribute(name = "act", required = true)
    protected Act act;
    
	/**
     * Gets the value of the sender property.
     * 
     * @return
     *     possible object is
     *     {@link AgentIdentifier }
     *     
     */
	public AgentIdentifier getSender() {
        return sender;
    }

    /**
     * Sets the value of the sender property.
     * 
     * @param value
     *     allowed object is
     *     {@link AgentIdentifier }
     *     
     */
	public void setSender(AgentIdentifier value) {
        this.sender = value;
    }

    /**
     * Gets the value of the receiver property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receiver property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceiver().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AgentIdentifier }
     * 
     * 
     */
	public List<AgentIdentifier> getReceiver() {
        if (receiver == null) {
            receiver = new ArrayList<AgentIdentifier>();
        }
        return this.receiver;
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
	public void setContent(String value) {
        this.content = value;
    }

    /**
     * Gets the value of the conversationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getConversationId() {
        return conversationId;
    }

    /**
     * Sets the value of the conversationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
	public void setConversationId(String value) {
        this.conversationId = value;
    }

    /**
     * Gets the value of the replyTo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the replyTo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReplyTo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AgentIdentifier }
     * 
     * 
     */
	public List<AgentIdentifier> getReplyTo() {
        if (replyTo == null) {
            replyTo = new ArrayList<AgentIdentifier>();
        }
        return this.replyTo;
    }

    /**
     * Gets the value of the replyWith property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getReplyWith() {
        return replyWith;
    }

    /**
     * Sets the value of the replyWith property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
	public void setReplyWith(String value) {
        this.replyWith = value;
    }

    /**
     * Gets the value of the inReplyTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getInReplyTo() {
        return inReplyTo;
    }

    /**
     * Sets the value of the inReplyTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
	public void setInReplyTo(String value) {
        this.inReplyTo = value;
    }

    /**
     * Gets the value of the replyBy property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
	public XMLGregorianCalendar getReplyBy() {
        return replyBy;
    }

    /**
     * Sets the value of the replyBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
	public void setReplyBy(XMLGregorianCalendar value) {
        this.replyBy = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
	public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Gets the value of the languageRepresentation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getLanguageRepresentation() {
        return languageRepresentation;
    }

    /**
     * Sets the value of the languageRepresentation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
	public void setLanguageRepresentation(String value) {
        this.languageRepresentation = value;
    }

    /**
     * Gets the value of the ontology property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getOntology() {
        return ontology;
    }

    /**
     * Sets the value of the ontology property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
	public void setOntology(String value) {
        this.ontology = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
	public void setProtocol(String value) {
        this.protocol = value;
    }

    /**
     * Gets the value of the act property.
     * 
     * @return
     *     possible object is
     *     {@link Act }
     *     
     */
	public Act getAct() {
        return act;
    }

    /**
     * Sets the value of the act property.
     * 
     * @param value
     *     allowed object is
     *     {@link Act }
     *     
     */
	public void setAct(Act value) {
        this.act = value;
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
}
