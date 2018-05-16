package org.openhim.mediator.dsub;

import org.apache.commons.io.IOUtils;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

public final class DsubUtil {

    private static final int BODY_NODE = 3;
    private static final int MESSAGE_NODE = 1;
    private static final String YES_PROPERTY = "yes";

    public static Object extractRequestMessage(String parsedRequest) throws JAXBException {
        Object result;
        JAXBContext jaxbContext = JAXBContext.newInstance("org.oasis_open.docs.wsn.b_2");
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        result = unmarshaller.unmarshal(IOUtils.toInputStream(parsedRequest));
        return result;
    }

    public static String parseRequest(MediatorHTTPRequest request) throws ParserConfigurationException, SAXException,
            IOException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dom = db.parse(IOUtils.toInputStream(request.getBody()));
        NamedNodeMap envelopeAttributes = dom.getDocumentElement().getAttributes();
        Node messageNode = dom.getDocumentElement().getChildNodes().item(BODY_NODE).getChildNodes().item(MESSAGE_NODE);
        for (int i = 0; i< envelopeAttributes.getLength() ; i++) {
            String attrName = envelopeAttributes.item(i).getNodeName();
            String attrValue = envelopeAttributes.item(i).getNodeValue();
            ((Element)messageNode).setAttribute(attrName, attrValue);
        }

        StringWriter sw = new StringWriter();
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, YES_PROPERTY);
        t.setOutputProperty(OutputKeys.INDENT, YES_PROPERTY);
        t.transform(new DOMSource(messageNode), new StreamResult(sw));
        return sw.toString();
    }

    private DsubUtil(){}
}
