package sn.esmt.gesb.controllers;


import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import sn.esmt.gesb.wsdl.*;

@Endpoint
public class GesbController {

    private static final String NAMESPACE_URI = "http://localhost:8080";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ActionRequest")
    public boolean processESBRequest(@RequestPayload ActionRequest actionRequest){
        System.out.println(actionRequest);
        return true;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "testSOAPRequest")
    public boolean testSOAP(){
        System.out.println("actionRequest");
        return true;
    }
}
