package sn.esmt.gesb.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

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


    public String sendSoapRequestGettingString(String serverUri, String request) throws Exception {
        DOMResult responseResult = new DOMResult();
        webServiceTemplate.sendSourceAndReceiveToResult(serverUri, new StreamSource(new StringReader(request)), responseResult);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter stringWriter = new StringWriter();
        transformer.transform(new DOMSource(responseResult.getNode()), new StreamResult(stringWriter));
        return stringWriter.toString();
    }
}
