package sn.esmt.gesb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sn.esmt.gesb.critical.*;
import sn.esmt.gesb.services.Impl.SoapClientService;
import sn.esmt.gesb.services.Impl.YourClassWithRetryLogic;
import sn.esmt.gesb.soam.*;
import sn.esmt.gesb.utils.SoapResponseParser;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@EnableRetry
class GlobalEsbApplicationTests {

    @Autowired
    private SoapClientService soapClientService;
    @Autowired
    private YourClassWithRetryLogic yourClassWithRetryLogic;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldTestSOAPRequest() {
        SoapMessageFactory messageFactory = new SaajSoapMessageFactory();

        String request2 = "<newConnectionRequest xmlns=\"http://esmt.sn/in_api/soam\" requestId=\"1235478\">\n" +
                "            <esbContent verb=\"ADD\">\n" +
                "                <esbParameter name=\"ola\" newValue=\"HI\"/>\n" +
                "            </esbContent>\n" +
                "        </newConnectionRequest>";
        try {
            DOMResult result = soapClientService.sendSoapRequest("http://localhost:8091/ws", request2);
            System.out.println("ICI");

            Node resultNode = result.getNode();

            // Utiliser une fabrique de transformateurs pour afficher le contenu du nœud DOM
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Transformer le nœud DOM en une chaîne et l'afficher
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(resultNode), new StreamResult(stringWriter));

            // Afficher le contenu du nœud DOM sous forme de chaîne
            System.out.println("Contenu du DOMResult : " + stringWriter.toString());

            /**
             * Autre code
             */

            Node root = result.getNode();
            if (root instanceof Document) {
                Document document = (Document) root;

                // Récupérer la balise esbRootActionResponse
                NodeList esbRootActionResponseList = document.getElementsByTagNameNS("http://esmt.sn/gesb/soam", "esbRootActionResponse");
                if (esbRootActionResponseList.getLength() > 0) {
                    Element esbRootActionResponse = (Element) esbRootActionResponseList.item(0);

                    // Extraire et afficher le contenu de la balise esbRootActionResponse
                    String success = getElementTextContent(esbRootActionResponse, "ns2:success");
                    String message = getElementTextContent(esbRootActionResponse, "ns2:message");

                    System.out.println("Contenu de esbRootActionResponse :");
                    System.out.println("Success: " + success);
                    System.out.println("Message: " + message);
                } else {
                    System.out.println("Balise esbRootActionResponse non trouvée dans le document XML.");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals("1", e.getMessage());

        }


    }


    private static String getElementTextContent(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    @Test
    void shouldTestRetry() {
        for (int i = 0; i < 10; i++) {
            yourClassWithRetryLogic.retry(i);
        }
    }

    @Test
    void shouldTestGettingParameters() {
        SubscriberData subscriberData = generateSubscriberData();
        EsbContent esbContent = getEsbParameters(subscriberData);
        // System.err.println(esbContent);
        Assertions.assertEquals(esbContent.getEsbParameter().size(), 4);
        Assertions.assertNotNull(esbContent.getEsbServices());
        Assertions.assertNotNull(esbContent.getEsbServices().getEsbService());
        Assertions.assertEquals(esbContent.getEsbServices().getEsbService().size(), 3);
    }


    private SubscriberData generateSubscriberData() {
        SubscriberData subscriberData = new SubscriberData();
        Service service = new Service();
        service.setServiceType(ServiceType.SERV_5_G);
        service.setTargetNumber("");
        service.setActive(true);

        Service service2 = new Service();
        service2.setServiceType(ServiceType.SERV_CFWNOREACH);
        service2.setTargetNumber("785900131");
        service2.setActive(true);

        Service service3 = new Service();
        service3.setServiceType(ServiceType.SERV_LTE);
        service3.setTargetNumber("");
        service3.setActive(false);

        Services services = new Services();
        services.getService().addAll(List.of(service, service2, service3));

        subscriberData.setServices(services);
        subscriberData.setSubscriberType("PRE_PAID");
        subscriberData.setImsi("12345678956");
        subscriberData.setPhoneNumber("788963214");
        subscriberData.setName("Razacki");
        return subscriberData;
    }

    public static <T> EsbContent getEsbParameters(T object) {
        EsbContent esbContent = new EsbContent();
        List<EsbParameter> esbParameters = new ArrayList<>();
        EsbServices esbServices = new EsbServices();

        Class<?> clazz = object.getClass();

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {

            if (!Modifier.isPublic(field.getModifiers())) {
                try {
                    field.setAccessible(true);
                } catch (InaccessibleObjectException e) {
                    continue;
                }
            }

            if (!field.isEnumConstant()) {

                String name = field.getName();

                Object value = null;
                try {
                    value = field.get(object);

                    if (value == null) continue;

                    if (value instanceof String || value instanceof Boolean) {
                        EsbParameter esbParameter = buildEsbParameter(name, value.toString(), null);
                        esbParameters.add(esbParameter);
                    } else if (value instanceof Enum<?>) {
                        Enum<?> enums = (Enum<?>) value;
                        EsbParameter esbParameter = buildEsbParameter(name, enums.name(), null);
                        esbParameters.add(esbParameter);
                    } else if (value instanceof List) {
                        ArrayList<?> list = (ArrayList<?>) value;
                        for (Object subObject : list) {
                            EsbContent esbContent2 = getEsbParameters(subObject);
                            EsbService esbService = buildEsbService(VerbType.ADD, esbContent2.getEsbParameter());
                            esbServices.getEsbService().add(esbService);
                        }
                    } else {
                        EsbContent esbContent2 = getEsbParameters(value);
                        esbServices.getEsbService().addAll(esbContent2.getEsbServices().getEsbService());
                        esbParameters.addAll(esbContent2.getEsbParameter());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // todo : throw custom error here
                }
            }
        }

        esbContent.setEsbServices(esbServices);
        esbContent.getEsbParameter().addAll(esbParameters);
        return esbContent;
    }

    public static EsbParameter buildEsbParameter(String name, String newValue, String oldValue) {
        EsbParameter esbParameter = new EsbParameter();
        esbParameter.setName(name);
        esbParameter.setNewValue(newValue);
        esbParameter.setOldValue(oldValue);
        return esbParameter;
    }

    public static EsbService buildEsbService(VerbType verb, List<EsbParameter> esbParameter) {
        EsbService esbService = new EsbService();
        esbService.setVerb(verb);
        esbService.getEsbParameter().addAll(esbParameter);
        return esbService;
    }

    @ParameterizedTest
    @ValueSource(strings = {"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><ns2:displaySubscriberResponse xmlns:ns2=\"http://esmt.sn/hlr_api/soam\"><ns2:name>Razacki</ns2:name><ns2:phoneNumber>+221785900131</ns2:phoneNumber><ns2:imsi>10547544</ns2:imsi><ns2:subscriberType>PRE_PAID</ns2:subscriberType><ns2:services><ns2:service><ns2:serviceType>SERV_LTE</ns2:serviceType><ns2:targetNumber/><ns2:active>true</ns2:active></ns2:service><ns2:service><ns2:serviceType>SERV_ROAMING</ns2:serviceType><ns2:targetNumber/><ns2:active>true</ns2:active></ns2:service></ns2:services></ns2:displaySubscriberResponse>"})
    public void testParserDataForHLR(String xmlString) {
        String className = "sn.esmt.gesb.critical.SubscriberData";
        try {
            SubscriberData data = (SubscriberData) SoapResponseParser.parse(className, xmlString);
            System.out.println(data);
            Assertions.assertEquals(data.getImsi(), "10547544");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><ns2:displaySubscriberResponse xmlns:ns2=\"http://esmt.sn/in_api/soam\"><ns2:subscriberName>Razacki</ns2:subscriberName><ns2:phoneNumber>+221785900131</ns2:phoneNumber><ns2:imsi>10547544</ns2:imsi><ns2:dataBalance>500.0</ns2:dataBalance><ns2:callBalance>500.0</ns2:callBalance><ns2:smsBalance>500.0</ns2:smsBalance></ns2:displaySubscriberResponse>"})
    public void testParserDataForIN(String xmlString) {
        String className = "sn.esmt.gesb.critical.DisplaySubscriberResponse";
        try {
            DisplaySubscriberResponse data = (DisplaySubscriberResponse) SoapResponseParser.parse(className, xmlString);
            System.out.println(data);
            Assertions.assertEquals(data.getImsi(), "10547544");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}