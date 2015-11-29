//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.15 at 12:47:09 PM CST 
//


package com.rossjourdain.util.xero;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConversionDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConversionDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="ConversionProductName" type="{}conversionProductName"/>
 *         &lt;element name="OriginatingProductName" type="{}originatingProductName"/>
 *         &lt;element name="OriginatingProductVersion" type="{}originatingProductVersion"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConversionDetails", propOrder = {

})
public class ConversionDetails {

    @XmlElement(name = "ConversionProductName", required = true)
    protected String conversionProductName;
    @XmlElement(name = "OriginatingProductName", required = true)
    protected String originatingProductName;
    @XmlElement(name = "OriginatingProductVersion", required = true)
    protected String originatingProductVersion;

    /**
     * Gets the value of the conversionProductName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConversionProductName() {
        return conversionProductName;
    }

    /**
     * Sets the value of the conversionProductName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConversionProductName(String value) {
        this.conversionProductName = value;
    }

    /**
     * Gets the value of the originatingProductName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginatingProductName() {
        return originatingProductName;
    }

    /**
     * Sets the value of the originatingProductName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginatingProductName(String value) {
        this.originatingProductName = value;
    }

    /**
     * Gets the value of the originatingProductVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginatingProductVersion() {
        return originatingProductVersion;
    }

    /**
     * Sets the value of the originatingProductVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginatingProductVersion(String value) {
        this.originatingProductVersion = value;
    }

}
