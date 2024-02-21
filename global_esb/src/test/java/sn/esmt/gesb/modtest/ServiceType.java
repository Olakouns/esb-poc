//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2024.02.19 at 04:53:42 PM GMT 
//


package sn.esmt.gesb.modtest;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="ServiceType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SERV_VOLTE"/&gt;
 *     &lt;enumeration value="SERV_ROAMING"/&gt;
 *     &lt;enumeration value="SERV_5G"/&gt;
 *     &lt;enumeration value="SERV_LTE"/&gt;
 *     &lt;enumeration value="SERV_VOICE"/&gt;
 *     &lt;enumeration value="SERV_SMS"/&gt;
 *     &lt;enumeration value="SERV_CFWNOREPLY"/&gt;
 *     &lt;enumeration value="SERV_CFWNOREACH"/&gt;
 *     &lt;enumeration value="SERV_CRBT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ServiceType")
@XmlEnum
public enum ServiceType {

    SERV_VOLTE("SERV_VOLTE"),
    SERV_ROAMING("SERV_ROAMING"),
    @XmlEnumValue("SERV_5G")
    SERV_5_G("SERV_5G"),
    SERV_LTE("SERV_LTE"),
    SERV_VOICE("SERV_VOICE"),
    SERV_SMS("SERV_SMS"),
    SERV_CFWNOREPLY("SERV_CFWNOREPLY"),
    SERV_CFWNOREACH("SERV_CFWNOREACH"),
    SERV_CRBT("SERV_CRBT");
    private final String value;

    ServiceType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ServiceType fromValue(String v) {
        for (ServiceType c: ServiceType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
