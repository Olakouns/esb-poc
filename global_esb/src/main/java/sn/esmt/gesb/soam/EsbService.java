//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2024.02.13 at 04:34:55 PM GMT 
//


package sn.esmt.gesb.soam;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EsbService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EsbService"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="esbParameter" type="{http://esmt.sn/gesb/soam}EsbParameter" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="verb" use="required" type="{http://esmt.sn/gesb/soam}VerbType" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EsbService", propOrder = {
    "esbParameter"
})
public class EsbService {

    @XmlElement(required = true)
    protected List<EsbParameter> esbParameter;
    @XmlAttribute(name = "verb", required = true)
    protected VerbType verb;

    /**
     * Gets the value of the esbParameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the esbParameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEsbParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EsbParameter }
     * 
     * 
     */
    public List<EsbParameter> getEsbParameter() {
        if (esbParameter == null) {
            esbParameter = new ArrayList<EsbParameter>();
        }
        return this.esbParameter;
    }

    /**
     * Gets the value of the verb property.
     * 
     * @return
     *     possible object is
     *     {@link VerbType }
     *     
     */
    public VerbType getVerb() {
        return verb;
    }

    /**
     * Sets the value of the verb property.
     * 
     * @param value
     *     allowed object is
     *     {@link VerbType }
     *     
     */
    public void setVerb(VerbType value) {
        this.verb = value;
    }

}
