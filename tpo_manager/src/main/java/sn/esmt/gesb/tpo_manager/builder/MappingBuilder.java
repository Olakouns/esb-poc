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
    public String buildSOAPTemplate(String template, List<EsbParameter> esbParameters) {
        for (String extractVariable : extractVariables(template)) {
            String variableName = extractVariable.startsWith("!") ? extractVariable.substring(1) : extractVariable;
            Optional<EsbParameter> parameter = esbParameters.stream().filter(esbParameter -> esbParameter.getName().equals(variableName)).findFirst();
            if (parameter.isPresent()) {
                template = template.replace("${" + extractVariable + "}", getBindingData(extractVariable, parameter.get().getNewValue()));
            } else {
                // todo: throw exception here
                template = template.replace("${" + extractVariable + "}", "");
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

    private String getBindingData(String variableName, String value) {
        boolean isNegation = variableName.startsWith("!");
        String result = value;
        if (isNegation) {
            value = value.toLowerCase();
            boolean actualResult = !Boolean.parseBoolean(value);
            result = Boolean.toString(actualResult);
        }
        return result;
    }
}
