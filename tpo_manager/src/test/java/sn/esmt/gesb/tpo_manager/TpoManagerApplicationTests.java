package sn.esmt.gesb.tpo_manager;

import jakarta.transaction.Transactional;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sn.esmt.gesb.dto.Workflow;
import sn.esmt.gesb.soam.*;
import sn.esmt.gesb.tpo_manager.builder.SoapRequestBuilder;
import sn.esmt.gesb.tpo_manager.services.TPOService;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TpoManagerApplicationTests {

    @Autowired
    private TPOService tpoService;

    @Test
    void contextLoads() {
        String soapRequest = "<esbRootActionRequest xmlns=\"http://esmt.sn/gesb/soam\" requestId=\"1235478\">\n" +
                "            <esbContent verb=\"ADD\">\n" +
                "                <esbParameter name=\"ola\" newValue=\"HI\"/>\n" +
                "            </esbContent>\n" +
                "        </esbRootActionRequest>";
        System.out.println(SoapRequestBuilder.buildSoapRequest(soapRequest));
    }

    @ParameterizedTest
    @ValueSource(strings = {"<subscriber>\n" +
            "\t<name>${subscriberName}</name>\n" +
            "\t<phone>${phoneNumber}</phone>\n" +
            "\t<type>${subscriberType}</type>\n" +
            "\t<imsi>${imsi}</imsi>\n" +
            "</subscriber>"})
    public void testMapping(String xmlString) {
        try {
            Document document = new SAXBuilder().build(new StringReader(xmlString));
            Element rootElement = document.getRootElement();

            // Récupération des variables à remplacer
            Map<String, String> variables = extractVariables(xmlString);

            // Remplacement des variables
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                xmlString = xmlString.replace("${" + entry.getKey() + "}", entry.getValue());
            }

            System.out.println("XML après remplacement :");
            System.out.println(xmlString);
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> extractVariables(String xmlString) {
        Map<String, String> variables = new HashMap<>();
        Pattern pattern = Pattern.compile("\\$\\{(.*?)}");
        Matcher matcher = pattern.matcher(xmlString);

        while (matcher.find()) {
            String variable = matcher.group(1);
            variables.put(variable, "12");
        }
        // Pour l'instant, je retourne une Map vide, vous pouvez ajuster selon vos besoins
        return variables;
    }

    @ParameterizedTest
    @CsvSource({
            "True, !active, false",
            "False, !active, true",
            "True, active, true",
            "False, active, false"
    })
    public void shouldTestBinding(String originalValue, String variableName, String expectedValue) {
        originalValue = originalValue.toLowerCase();
        boolean isNegation = variableName.startsWith("!");
        boolean actualResult = Boolean.parseBoolean(originalValue) != isNegation;
        assertEquals(Boolean.parseBoolean(expectedValue), actualResult);
    }

    @Test
    @Transactional
    public void shouldTestMapping() {
        EsbRootActionRequest esbRootActionRequest = new EsbRootActionRequest();
        esbRootActionRequest.setRequestId("1235864");
        EsbContent esbContent = new EsbContent();
        esbContent.setVerb(VerbType.ADD);


        EsbParameter esbParameter1 = new EsbParameter();
        esbParameter1.setName("subscriberName");
        esbParameter1.setNewValue("Razacki");
        esbParameter1.setOldValue("");

        EsbParameter esbParameter2 = new EsbParameter();
        esbParameter2.setName("phoneNumber");
        esbParameter2.setNewValue("+221785900131");
        esbParameter2.setOldValue("");

        EsbParameter esbParameter3 = new EsbParameter();
        esbParameter3.setName("subscriberType");
        esbParameter3.setNewValue("PRE_PAID");
        esbParameter3.setOldValue("");

        EsbParameter esbParameter4 = new EsbParameter();
        esbParameter4.setName("imsi");
        esbParameter4.setNewValue("10547544");
        esbParameter4.setOldValue("");

        esbContent.getEsbParameter().addAll(List.of(esbParameter1, esbParameter2, esbParameter3, esbParameter4));

        EsbServices esbServices = new EsbServices();

        EsbService esbService = new EsbService();
        esbService.setVerb(VerbType.ADD);


        EsbParameter esbParameter5 = new EsbParameter();
        esbParameter5.setName("active");
        esbParameter5.setNewValue("true");
        esbParameter5.setOldValue("");

        EsbParameter esbParameter6 = new EsbParameter();
        esbParameter6.setName("serviceType");
        esbParameter6.setNewValue("SERV_LTE");
        esbParameter6.setOldValue("");

        EsbParameter esbParameter7 = new EsbParameter();
        esbParameter7.setName("targetNumber");
        esbParameter7.setNewValue("785900131");
        esbParameter7.setOldValue("");

        esbService.getEsbParameter().addAll(List.of(esbParameter6, esbParameter7, esbParameter5));

        EsbService esbService2 = new EsbService();
        esbService2.setVerb(VerbType.ADD);

        EsbParameter esbParameter51 = new EsbParameter();
        esbParameter51.setName("active");
        esbParameter51.setNewValue("true");
        esbParameter51.setOldValue("");

        EsbParameter esbParameter61 = new EsbParameter();
        esbParameter61.setName("serviceType");
        esbParameter61.setNewValue("SERV_ROAMING");
        esbParameter61.setOldValue("");

        EsbParameter esbParameter71 = new EsbParameter();
        esbParameter71.setName("targetNumber");
        esbParameter71.setNewValue("785900131");
        esbParameter71.setOldValue("");

        esbService2.getEsbParameter().addAll(List.of(esbParameter51, esbParameter61, esbParameter71));

        esbServices.getEsbService().addAll(List.of(esbService, esbService2));

        esbContent.setEsbServices(esbServices);

        esbRootActionRequest.setEsbContent(esbContent);

        Workflow workflow = tpoService.getMappingData(1, esbRootActionRequest);
        System.err.println(workflow);
        Assertions.assertEquals(workflow.getWorkflowSteps().size(), 5);
    }

}
