package sn.esmt.gesb.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import sn.esmt.gesb.services.RequestProcessorService;
import sn.esmt.gesb.wsdl.*;

@Endpoint
@RequiredArgsConstructor
public class GesbController {

    private static final String NAMESPACE_URI = "http://esb.sn/esmt";
    private final RequestProcessorService requestProcessorService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "actionRequest")
    @ResponsePayload
    public ActionResponse processRequest(@RequestPayload ActionRequest actionRequest){
        requestProcessorService.processRequest(actionRequest);
        return new ActionResponse();
    }
}
