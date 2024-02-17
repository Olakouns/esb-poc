package sn.esmt.gesb.tpo_manager;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import sn.esmt.gesb.tpo_manager.builder.SoapRequestBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class TpoManagerApplicationTests {

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
        // Expression régulière pour identifier les occurrences de ${...}
        Map<String, String> variables = new HashMap<>();
        Pattern pattern = Pattern.compile("\\$\\{(.*?)}");
        Matcher matcher = pattern.matcher(xmlString);

        // Récupération des variables
        while (matcher.find()) {
            String variable = matcher.group(1);
            System.out.println("Variable trouvée : " + variable);
            // Vous pouvez stocker ces variables dans une Map si nécessaire
            variables.put(variable, "12");
        }

        // Pour l'instant, je retourne une Map vide, vous pouvez ajuster selon vos besoins
        return variables;
    }

}
