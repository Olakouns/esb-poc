package sn.esmt.in_api.controllers;


import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import sn.esmt.in_api.services.BaseService;
import sn.esmt.in_api.soam.*;

@Endpoint
@RequiredArgsConstructor
@Slf4j
public class BaseController {

    private final BaseService baseService;
    private final ObjectFactory objectFactory;

    private static final String NAMESPACE_URI = "http://esmt.sn/in_api/soam";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "newConnectionRequest")
    @ResponsePayload
    public JAXBElement<ApiResponse> newConnection(@RequestPayload NewConnectionRequest newConnectionRequest) {
        log.info("Get request from client: {}", newConnectionRequest.getImsi());
        return objectFactory.createNewConnectionResponse(baseService.newConnection(newConnectionRequest));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "rechargingRequest")
    @ResponsePayload
    public JAXBElement<ApiResponse> recharging(@RequestPayload RechargingRequest rechargingRequest) {
        log.info("Get request from client: {}", rechargingRequest);
        return objectFactory.createNewConnectionResponse(baseService.recharging(rechargingRequest));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "terminationRequest")
    @ResponsePayload
    public JAXBElement<ApiResponse> termination(@RequestPayload TerminationRequest terminationRequest) {
        log.info("Get request from client: {}", terminationRequest);
        return objectFactory.createNewConnectionResponse(baseService.termination(terminationRequest));
    }
}
