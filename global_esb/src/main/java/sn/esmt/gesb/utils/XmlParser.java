package sn.esmt.gesb.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sn.esmt.gesb.soam.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    List<EsbParameter> esbParameterList = new ArrayList<>();
    List<EsbService> esbServices = new ArrayList<>();
    public EsbRootActionRequest parseXml(String xmlContent, String rootName) throws Exception {
        // Convertir la chaîne XML en un InputStream
        EsbRootActionRequest esbRootActionRequest = new EsbRootActionRequest();
        EsbServices servicesParent = new EsbServices();


        InputStream is = new ByteArrayInputStream(xmlContent.getBytes());

        // Créer un DocumentBuilderFactory et un DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Parse l'InputStream pour obtenir un Document
        Document document = builder.parse(is);

        // Obtenir l'élément racine
        Element rootElement = document.getDocumentElement();

        // Parcourir les nœuds à partir de l'élément racine
        esbParameterList = new ArrayList<>();
        esbServices = new ArrayList<>();

        traverseNode(rootElement, rootName);

        EsbContent esbContent = new EsbContent();
        esbContent.getEsbParameter().addAll(esbParameterList);
        servicesParent.getEsbService().addAll(esbServices);
        esbContent.setEsbServices(servicesParent);
        esbRootActionRequest.setEsbContent(esbContent);
        return esbRootActionRequest;
    }

    private void traverseNode(Node node, String rootName) {
        // Vérifier le type de nœud
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            // Récupérer le nom du nœud
            String nodeName = node.getNodeName();
            nodeName = nodeName.split(":")[1];

            // Récupérer et afficher la valeur de texte du nœud (si elle existe)
            String nodeValue = node.getTextContent().trim();
            if (!nodeValue.isEmpty() && !nodeName.equals(rootName)) {
                if (nodeName.equals("service")){
                    NodeList childNodes = node.getChildNodes();
                    EsbService esbService = new EsbService();
                    for (int i = 0; i < childNodes.getLength(); i++) {
                        Node childNode = childNodes.item(i);
                        EsbParameter parameter = new EsbParameter();
                        parameter.setName(childNode.getNodeName().split(":")[1]);
                        parameter.setOldValue(childNode.getTextContent().trim());
                        parameter.setNewValue("");
                        esbService.getEsbParameter().add(parameter);
                    }
                    esbServices.add(esbService);
                } else {
                    EsbParameter esbParameter = new EsbParameter();
                    esbParameter.setName(nodeName);
                    esbParameter.setNewValue("");
                    esbParameter.setOldValue(nodeValue);
                    esbParameterList.add(esbParameter);
                }
            }

            if(nodeName.equals("service")){
                return;
            }
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);
                traverseNode(childNode, rootName);
            }
        }
    }
}