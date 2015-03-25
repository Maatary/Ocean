
package ch.usi.ict.dev.AclOverSoapHttpMP;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;


/**
 * <p>Java class for received complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="received">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="received-by" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="received-from" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="received-date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="received-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="received-via" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
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
@XmlType(name = "received", propOrder = {
    "receivedBy",
    "receivedFrom",
    "receivedDate",
    "receivedId",
    "receivedVia",
    "any"
})
public class Received {

    @XmlElement(name = "received-by", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String receivedBy;
    @XmlElement(name = "received-from", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String receivedFrom;
    @XmlElement(name = "received-date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar receivedDate;
    @XmlElement(name = "received-id", required = true)
    protected String receivedId;
    @XmlElement(name = "received-via", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String receivedVia;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

	/**
     * Gets the value of the receivedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getReceivedBy() {
        return receivedBy;
    }

    /**
     * Sets the value of the receivedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
	public void setReceivedBy(String value) {
        this.receivedBy = value;
    }

    /**
     * Gets the value of the receivedFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getReceivedFrom() {
        return receivedFrom;
    }

    /**
     * Sets the value of the receivedFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
	public void setReceivedFrom(String value) {
        this.receivedFrom = value;
    }

    /**
     * Gets the value of the receivedDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
	public XMLGregorianCalendar getReceivedDate() {
        return receivedDate;
    }

    /**
     * Sets the value of the receivedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
	public void setReceivedDate(XMLGregorianCalendar value) {
        this.receivedDate = value;
    }

    /**
     * Gets the value of the receivedId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getReceivedId() {
        return receivedId;
    }

    /**
     * Sets the value of the receivedId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
	public void setReceivedId(String value) {
        this.receivedId = value;
    }

    /**
     * Gets the value of the receivedVia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getReceivedVia() {
        return receivedVia;
    }

    /**
     * Sets the value of the receivedVia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
	public void setReceivedVia(String value) {
        this.receivedVia = value;
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
}
