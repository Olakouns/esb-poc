package sn.esmt.gesb.utils;

import jakarta.xml.bind.JAXBElement;
import org.glassfish.jaxb.runtime.marshaller.NamespacePrefixMapper;
import sn.esmt.gesb.critical.SubscriberData;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

import java.io.StringReader;

public class SoapResponseParser {
    public static SubscriberData parseResponse(String xml) throws Exception {
        JAXBContext context = JAXBContext.newInstance(SubscriberData.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (SubscriberData) unmarshaller.unmarshal(new StringReader(xml));
    }

    public static Object parseResponseString(String xml) throws Exception {
        JAXBContext context = JAXBContext.newInstance("com.example.jaxb"); // Package de vos classes générées
        Unmarshaller unmarshaller = context.createUnmarshaller();
        JAXBElement<?> element = (JAXBElement<?>) unmarshaller.unmarshal(new StringReader(xml));
        return element.getValue();
    }

    public static Object parse(String className, String xml) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Class.forName(className));
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller.unmarshal(new StringReader(xml));
    }
}

