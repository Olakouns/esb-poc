package sn.esmt.gesb.controllers;


import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import sn.esmt.gesb.wsdl.*;

@Endpoint
public class GesbController {

    private static final String NAMESPACE_URI = "http://esb.sn/esmt";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "actionRequest")
    @ResponsePayload
    public ActionResponse processRequest(@RequestPayload ActionRequest actionRequest){
        System.out.println(actionRequest.getMainAction());
        return new ActionResponse();
    }
}
