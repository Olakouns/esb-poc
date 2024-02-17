package sn.esmt.gesb.tpo_manager.builder;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.stereotype.Component;
import sn.esmt.gesb.soam.EsbParameter;
import sn.esmt.gesb.soam.EsbRootActionRequest;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MappingBuilder {
    public String buildSOAPTemplate(String template, EsbRootActionRequest esbRootActionRequest) throws JDOMException, IOException {
        Document document = new SAXBuilder().build(new StringReader(template));
        Element rootElement = document.getRootElement();

        for (String extractVariable : extractVariables(template)) {
            Optional<EsbParameter> parameter = esbRootActionRequest.getEsbContent().getEsbParameter().stream().filter(esbParameter -> esbParameter.getName().equals(extractVariable)).findFirst();
            if (parameter.isPresent()){
                template = template.replace("${" + extractVariable + "}", parameter.get().getNewValue());
            }
        }
        return template;
    }

    private static List<String> extractVariables(String xmlString) {
        List<String> variables = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\$\\{(.*?)}");
        Matcher matcher = pattern.matcher(xmlString);
        while (matcher.find()) {
            variables.add(matcher.group(1));
        }
        return variables;
    }

    private String getValueFromRequest(){
        return "";
    }
}
