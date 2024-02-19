//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2024.02.19 at 04:53:42 PM GMT 
//


package sn.esmt.hlr_api.soam;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sn.esmt.hlr_api.soam package. 
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

    private final static QName _ActivateSubscriberRequest_QNAME = new QName("http://esmt.sn/hlr_api/soam", "activateSubscriberRequest");
    private final static QName _ActivateSubscriberResponse_QNAME = new QName("http://esmt.sn/hlr_api/soam", "activateSubscriberResponse");
    private final static QName _DeactivateSubscriberResponse_QNAME = new QName("http://esmt.sn/hlr_api/soam", "deactivateSubscriberResponse");
    private final static QName _DisplaySubscriberResponse_QNAME = new QName("http://esmt.sn/hlr_api/soam", "displaySubscriberResponse");
    private final static QName _ModifyServiceSubscriberResponse_QNAME = new QName("http://esmt.sn/hlr_api/soam", "modifyServiceSubscriberResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sn.esmt.hlr_api.soam
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SubscriberData }
     * 
     */
    public SubscriberData createSubscriberData() {
        return new SubscriberData();
    }

    /**
     * Create an instance of {@link ApiResponse }
     * 
     */
    public ApiResponse createApiResponse() {
        return new ApiResponse();
    }

    /**
     * Create an instance of {@link DeactivateSubscriberRequest }
     * 
     */
    public DeactivateSubscriberRequest createDeactivateSubscriberRequest() {
        return new DeactivateSubscriberRequest();
    }

    /**
     * Create an instance of {@link DisplaySubscriberRequest }
     * 
     */
    public DisplaySubscriberRequest createDisplaySubscriberRequest() {
        return new DisplaySubscriberRequest();
    }

    /**
     * Create an instance of {@link ModifyServiceSubscriberRequest }
     * 
     */
    public ModifyServiceSubscriberRequest createModifyServiceSubscriberRequest() {
        return new ModifyServiceSubscriberRequest();
    }

    /**
     * Create an instance of {@link Service }
     * 
     */
    public Service createService() {
        return new Service();
    }

    /**
     * Create an instance of {@link Services }
     * 
     */
    public Services createServices() {
        return new Services();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubscriberData }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SubscriberData }{@code >}
     */
    @XmlElementDecl(namespace = "http://esmt.sn/hlr_api/soam", name = "activateSubscriberRequest")
    public JAXBElement<SubscriberData> createActivateSubscriberRequest(SubscriberData value) {
        return new JAXBElement<SubscriberData>(_ActivateSubscriberRequest_QNAME, SubscriberData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApiResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ApiResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://esmt.sn/hlr_api/soam", name = "activateSubscriberResponse")
    public JAXBElement<ApiResponse> createActivateSubscriberResponse(ApiResponse value) {
        return new JAXBElement<ApiResponse>(_ActivateSubscriberResponse_QNAME, ApiResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApiResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ApiResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://esmt.sn/hlr_api/soam", name = "deactivateSubscriberResponse")
    public JAXBElement<ApiResponse> createDeactivateSubscriberResponse(ApiResponse value) {
        return new JAXBElement<ApiResponse>(_DeactivateSubscriberResponse_QNAME, ApiResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubscriberData }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SubscriberData }{@code >}
     */
    @XmlElementDecl(namespace = "http://esmt.sn/hlr_api/soam", name = "displaySubscriberResponse")
    public JAXBElement<SubscriberData> createDisplaySubscriberResponse(SubscriberData value) {
        return new JAXBElement<SubscriberData>(_DisplaySubscriberResponse_QNAME, SubscriberData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApiResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ApiResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://esmt.sn/hlr_api/soam", name = "modifyServiceSubscriberResponse")
    public JAXBElement<ApiResponse> createModifyServiceSubscriberResponse(ApiResponse value) {
        return new JAXBElement<ApiResponse>(_ModifyServiceSubscriberResponse_QNAME, ApiResponse.class, null, value);
    }

}
