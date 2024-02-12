package sn.esmt.gesb.tpo_manager.builder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class SoapRequestBuilder {
    public static String buildSoapRequest(String bodyContent) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            // Create SOAP ENV
            Element envelope = document.createElementNS("http://schemas.xmlsoap.org/soap/envelope/", "Envelope");
            document.appendChild(envelope);

            // Create SOAP body
            Element body = document.createElementNS("http://schemas.xmlsoap.org/soap/envelope/", "Body");
            envelope.appendChild(body);

            // Add body request to SOAP body
            body.appendChild(parseXmlString(bodyContent, document));

            // Transform SOAP document into XMl String
            return documentToString(document);
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Element parseXmlString(String xmlString, Document document) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xmlDocument = builder.parse(new InputSource(new StringReader(xmlString)));
        return (Element) document.importNode(xmlDocument.getDocumentElement(), true);
    }

    private static String documentToString(Document document) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString();
    }
}
