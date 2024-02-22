package sn.esmt.gesb.utils;

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
}

