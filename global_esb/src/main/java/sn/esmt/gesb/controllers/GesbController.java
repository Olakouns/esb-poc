package sn.esmt.gesb.controllers;


import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapFaultException;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;
import sn.esmt.gesb.components.QueueManagerComponent;
import sn.esmt.gesb.soam.EsbRootActionRequest;
import sn.esmt.gesb.soam.EsbRootActionResponse;

@Endpoint
@RequiredArgsConstructor
@Slf4j
public class GesbController {

    private static final String NAMESPACE_URI = "http://esmt.sn/gesb/soam";
    private final QueueManagerComponent queueManagerComponent;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "esbRootActionRequest")
    @ResponsePayload
    public EsbRootActionResponse processRequest(@RequestPayload EsbRootActionRequest esbRootActionRequest,
                                                @SoapHeader(value = "{" + NAMESPACE_URI + "}callbackURL") SoapHeaderElement soapHeaderElement) {

        if (soapHeaderElement == null || StringUtils.isBlank(soapHeaderElement.getText())) {
            throw new SoapFaultException("Callback URL is required but not provided.");
        }

        if (queueManagerComponent.getQueueSize() >= queueManagerComponent.MAX_QUEUE_SIZE) {
            throw new SoapFaultException("Queue is full");
        }

        queueManagerComponent.enqueue(esbRootActionRequest);

        log.info("Get request from client: {}", esbRootActionRequest);
        return createTemplateResponse();
    }

    private EsbRootActionResponse createTemplateResponse() {
        EsbRootActionResponse response = new EsbRootActionResponse();
        response.setSuccess(true);
        response.setMessage("Request received");
        return response;
    }
}
