package sn.esmt.hlr_api.controllers;

import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import sn.esmt.hlr_api.services.SubscriberUserService;
import sn.esmt.hlr_api.soam.*;

@Endpoint
@RequiredArgsConstructor
@Slf4j
public class BaseController {

    private final SubscriberUserService subscriberUserService;
    private final ObjectFactory objectFactory;

    private static final String NAMESPACE_URI = "http://esmt.sn/hlr_api/soam";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "activateSubscriberRequest")
    @ResponsePayload
    public JAXBElement<ApiResponse> activateSubscriber(@RequestPayload SubscriberData subscriberData) {
        log.info("ActivateSubscriber - Get request from client: {}", subscriberData.getImsi());
        return objectFactory.createActivateSubscriberResponse(subscriberUserService.activateSubscriber(subscriberData));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deactivateSubscriberRequest")
    @ResponsePayload
    public JAXBElement<ApiResponse> deactivateSubscriber(@RequestPayload DeactivateSubscriberRequest deactivateSubscriberRequest) {
        log.info("DeactivateSubscriber - Get request from client: {}", deactivateSubscriberRequest.getPhoneNumber());
        return objectFactory.createDeactivateSubscriberResponse(subscriberUserService.deactivateSubscriber(deactivateSubscriberRequest));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "displaySubscriberRequest")
    @ResponsePayload
    public JAXBElement<SubscriberData> displaySubscriber(@RequestPayload DisplaySubscriberRequest displaySubscriberRequest) {
        log.info("DisplaySubscriberRequest - Get request from client: {}", displaySubscriberRequest.getPhoneNumber());
        return objectFactory.createDisplaySubscriberResponse(subscriberUserService.displaySubscriber(displaySubscriberRequest));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "modifyServiceSubscriberRequest")
    @ResponsePayload
    public JAXBElement<ApiResponse> modifyServiceSubscriber(@RequestPayload ModifyServiceSubscriberRequest modifyServiceSubscriberRequest) {
        log.info("ModifyServiceSubscriber - Get request from client: {}", modifyServiceSubscriberRequest.getPhoneNumber());
        return objectFactory.createModifyServiceSubscriberResponse(subscriberUserService.modifyServiceSubscriber(modifyServiceSubscriberRequest));
    }
}
