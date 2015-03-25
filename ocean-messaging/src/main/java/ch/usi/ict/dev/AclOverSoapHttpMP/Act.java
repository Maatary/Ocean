
package ch.usi.ict.dev.AclOverSoapHttpMP;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for act.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="act">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="accept-proposal"/>
 *     &lt;enumeration value="agree"/>
 *     &lt;enumeration value="cancel"/>
 *     &lt;enumeration value="cfp"/>
 *     &lt;enumeration value="confirm"/>
 *     &lt;enumeration value="declare"/>
 *     &lt;enumeration value="disconfirm"/>
 *     &lt;enumeration value="failure"/>
 *     &lt;enumeration value="inform"/>
 *     &lt;enumeration value="not-understood"/>
 *     &lt;enumeration value="propose"/>
 *     &lt;enumeration value="query-if"/>
 *     &lt;enumeration value="query-ref"/>
 *     &lt;enumeration value="refuse"/>
 *     &lt;enumeration value="reject-proposal"/>
 *     &lt;enumeration value="request"/>
 *     &lt;enumeration value="request-when"/>
 *     &lt;enumeration value="request-whenever"/>
 *     &lt;enumeration value="subscribe"/>
 *     &lt;enumeration value="inform-if"/>
 *     &lt;enumeration value="inform-ref"/>
 *     &lt;enumeration value="proxy"/>
 *     &lt;enumeration value="propagate"/>
 *     &lt;enumeration value="promise"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "act")
@XmlEnum
//@Generated(value = "com.sun.tools.xjc.Driver", date = "2011-07-20T11:55:06+02:00", comments = "JAXB RI vhudson-jaxb-ri-2.2-27")
public enum Act {

    @XmlEnumValue("accept-proposal")
    ACCEPT_PROPOSAL("accept-proposal"),
    @XmlEnumValue("agree")
    AGREE("agree"),
    @XmlEnumValue("cancel")
    CANCEL("cancel"),
    @XmlEnumValue("cfp")
    CFP("cfp"),
    @XmlEnumValue("confirm")
    CONFIRM("confirm"),
    @XmlEnumValue("declare")
    DECLARE("declare"), 
    @XmlEnumValue("disconfirm")
    DISCONFIRM("disconfirm"),
    @XmlEnumValue("failure")
    FAILURE("failure"),
    @XmlEnumValue("inform")
    INFORM("inform"),
    @XmlEnumValue("not-understood")
    NOT_UNDERSTOOD("not-understood"),
    @XmlEnumValue("propose")
    PROPOSE("propose"),
    @XmlEnumValue("query-if")
    QUERY_IF("query-if"),
    @XmlEnumValue("query-ref")
    QUERY_REF("query-ref"),
    @XmlEnumValue("refuse")
    REFUSE("refuse"),
    @XmlEnumValue("reject-proposal")
    REJECT_PROPOSAL("reject-proposal"),
    @XmlEnumValue("request")
    REQUEST("request"),
    @XmlEnumValue("request-when")
    REQUEST_WHEN("request-when"),
    @XmlEnumValue("request-whenever")
    REQUEST_WHENEVER("request-whenever"),
    @XmlEnumValue("subscribe")
    SUBSCRIBE("subscribe"),
    @XmlEnumValue("inform-if")
    INFORM_IF("inform-if"),
    @XmlEnumValue("inform-ref")
    INFORM_REF("inform-ref"),
    @XmlEnumValue("proxy")
    PROXY("proxy"),
    @XmlEnumValue("propagate")
    PROPAGATE("propagate"), 
    @XmlEnumValue("promise")
    PROMISE("promise");
    private final String value;

    Act(String v) {
        value = v;
       
    }

    public String value() {
        return value;
    }

    public static Act fromValue(String v) {
        for (Act c: Act.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
