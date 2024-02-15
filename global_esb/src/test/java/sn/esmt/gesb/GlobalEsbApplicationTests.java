package sn.esmt.gesb;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.xml.transform.StringSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sn.esmt.gesb.services.Impl.SoapClientService;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringWriter;

@SpringBootTest
class GlobalEsbApplicationTests {

	@Autowired
	private SoapClientService soapClientService;

    @Test
	void contextLoads() {
	}

	@Test
	void shouldTestSOAPRequest(){
		SoapMessageFactory messageFactory = new SaajSoapMessageFactory();

//		String request = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
//				"<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
//				"    <Body>\n" +
//				"        <esbRootActionRequest xmlns=\"http://esmt.sn/gesb/soam\" requestId=\"1235478\">\n" +
//				"            <esbContent verb=\"ADD\">\n" +
//				"                <esbParameter name=\"ola\" newValue=\"HI\"/>\n" +
//				"            </esbContent>\n" +
//				"        </esbRootActionRequest>\n" +
//				"    </Body>\n" +
//				"</Envelope>";

		String request2 = "<newConnectionRequest xmlns=\"http://esmt.sn/in_api/soam\" requestId=\"1235478\">\n" +
				"            <esbContent verb=\"ADD\">\n" +
				"                <esbParameter name=\"ola\" newValue=\"HI\"/>\n" +
				"            </esbContent>\n" +
				"        </newConnectionRequest>";
		try{
			DOMResult result =  soapClientService.sendSoapRequestA("http://localhost:8091/ws", request2);
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
		}catch (Exception e){
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

}