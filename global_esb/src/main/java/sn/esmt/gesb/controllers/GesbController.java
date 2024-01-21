package sn.esmt.gesb.controllers;


import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import sn.esmt.gesb.wsdl.*;

@Endpoint
public class GesbController {

    private static final String NAMESPACE_URI = "http://localhost:8080/myservice";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "requestFootMatch")
    public @ResponsePayload FootMatch getMatch(@RequestPayload String requestFootMatch){
        return new FootMatch();
    }
}
