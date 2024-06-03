package sn.esmt.gesb.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class XmlParser {
    public void parseXml(String xmlContent) throws Exception {
        // Convertir la chaîne XML en un InputStream
        InputStream is = new ByteArrayInputStream(xmlContent.getBytes());

        // Créer un DocumentBuilderFactory et un DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Parse l'InputStream pour obtenir un Document
        Document document = builder.parse(is);

        // Obtenir l'élément racine
        Element rootElement = document.getDocumentElement();

        // Parcourir les nœuds à partir de l'élément racine
        traverseNode(rootElement);
    }

    private void traverseNode(Node node) {
        // Vérifier le type de nœud
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            // Récupérer le nom du nœud
            String nodeName = node.getNodeName();

            // Récupérer et afficher la valeur de texte du nœud (si elle existe)
            String nodeValue = node.getTextContent().trim();
            if (!nodeValue.isEmpty()) {
                System.out.println("Node Name: " + nodeName);
                System.out.println("  Value: " + nodeValue);
            }

            // Parcourir les nœuds enfants
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);
                traverseNode(childNode); // Appel récursif pour traiter les enfants
            }
        }
    }

    private void printIndentation(int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
    }
}
