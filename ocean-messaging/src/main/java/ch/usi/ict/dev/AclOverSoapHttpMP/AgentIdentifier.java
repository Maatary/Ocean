
package ch.usi.ict.dev.AclOverSoapHttpMP;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;


/**
 * <p>Java class for agent-identifier complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="agent-identifier">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}anyURI" maxOccurs="unbounded"/>
 *         &lt;element name="resolver" type="{http://www.usi.ch/ict/dev/AclOverSoapHttpMP/}agent-identifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "agent-identifier", propOrder = {
    "name",
    "address",
    "resolver",
    "any"
})
public class AgentIdentifier {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected List<String> address;
	protected List<AgentIdentifier> resolver;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

	/**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
	public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the address property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
	public List<String> getAddress() {
        if (address == null) {
            address = new ArrayList<String>();
        }
        return this.address;
    }

    /**
     * Gets the value of the resolver property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resolver property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResolver().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AgentIdentifier }
     * 
     * 
     */
	public List<AgentIdentifier> getResolver() {
        if (resolver == null) {
            resolver = new ArrayList<AgentIdentifier>();
        }
        return this.resolver;
    }

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
	public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
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

    

//	@Override
    public boolean equals(Object obj) {
    	if (obj instanceof AgentIdentifier)
    		return equals(((AgentIdentifier)obj));
    	return super.equals(obj);
    }
    
    
    public boolean equals(AgentIdentifier agtId) {
    	
    	if (! (this.getName().equals(agtId.getName())) )
    		return false;
    	
    	//TODO Handle the case where address may not be specified in the same order
    	if (! (this.getAddress().equals(agtId.getAddress())) )
    		return false;
    	
    	if (! (this.getResolver().equals(agtId.getResolver())))
    		return false;
    	
    	return true;
    }
    
//    @Override
    public int hashCode() {
    	return super.hashCode();
    }
    
    /** Helper method**/
    
    
    public static AgentIdentifier getAgentIdentifierWithParams(String name, String adrs) {
    	
    	AgentIdentifier agtidt = new AgentIdentifier();
    	
    	agtidt.setName(name);
    	
    	agtidt.getAddress().add(adrs);
    	
    	return agtidt;
    	
    }
    
    
}
