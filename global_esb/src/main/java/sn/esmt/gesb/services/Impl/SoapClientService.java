package sn.esmt.gesb.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

@Service
@RequiredArgsConstructor
public class SoapClientService {

    private final WebServiceTemplate webServiceTemplate;

//    public Object sendSoapRequest(Object request) {
//        return webServiceTemplate.marshalSendAndReceive(request);
//    }

    public DOMResult sendSoapRequest(String serverUri, String request) {
        DOMResult responseResult = new DOMResult();
        webServiceTemplate.sendSourceAndReceiveToResult(serverUri, new StreamSource(new StringReader(request)), responseResult);
        return responseResult;
    }
}
