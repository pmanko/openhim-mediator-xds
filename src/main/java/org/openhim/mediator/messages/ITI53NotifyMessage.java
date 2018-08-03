package org.openhim.mediator.messages;

import org.apache.http.HttpStatus;
import org.openhim.mediator.engine.messages.FinishRequest;

import java.util.Objects;
import java.util.UUID;

public class ITI53NotifyMessage {

    private String messageId;
    private String recipientServerAddress;
    private String brokerServerAddress;
    private String documentId;

    private static final String TEMPLATE =
            "------OPENHIM\n" +
            "Content-Type: application/xop+xml; charset=utf-8; type=\"application/soap+xml\"\n" +
            "\n" +
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" " +
            "xmlns:a=\"http://www.w3.org/2005/08/addressing\" " +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
            "xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" " +
            "xmlns:xds=\"urn:ihe:iti:xds-b:2007\" " +
            "xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" " +
            "xsi:schemaLocation=\"http://www.w3.org/2003/05/soap-envelope http://www.w3.org/2003/05/soapenvelope " +
            "http://www.w3.org/2005/08/addressing http://www.w3.org/2005/08/addressing/ws-addr.xsd " +
            "http://docs.oasis-open.org/wsn/b-2 http://docs.oasis-open.org/wsn/b-2.xsd urn:ihe:iti:xds-b:2007 " +
            "../../schema/IHE/XDS.b_DocumentRepository.xsd urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0 " +
            "../../schema/ebRS/rim.xsd\">" +
            "<s:Header>" +
            "<a:Action>http://docs.oasis-open.org/wsn/bw-2/NotificationConsumer/Notify</a:Action>" +
            "<a:MessageID>%s</a:MessageID>" +
            "<a:To>%s</a:To>" +
            "</s:Header>" +
            "<s:Body>" +
            "<wsnt:Notify>" +
            "<wsnt:NotificationMessage>" +
            "<wsnt:SubscriptionReference>" +
            "<a:Address>%s" +
            "</a:Address>" +
            "</wsnt:SubscriptionReference>" +
            "<wsnt:Topic Dialect=\"http://docs.oasis-open.org/wsn/t1/TopicExpression/Simple\">" +
            "ihe:MinimalDocumentEntry" +
            "</wsnt:Topic>" +
            "<wsnt:ProducerReference>" +
            "<a:Address>https://ProducerReference</a:Address>" +
            "</wsnt:ProducerReference>" +
            "<wsnt:Message>" +
            "<lcm:SubmitObjectsRequest>" +
            "<rim:RegistryObjectList>" +
            "<!-- Document ID -->" +
            "<rim:ObjectRef id=\"%s\"/>" +
            "</rim:RegistryObjectList>" +
            "</lcm:SubmitObjectsRequest>" +
            "</wsnt:Message>" +
            "</wsnt:NotificationMessage>" +
            "</wsnt:Notify>" +
            "</s:Body>" +
            "</s:Envelope>" +
            "------OPENHIM--";

    public ITI53NotifyMessage(String recipientServerAddress, String brokerServerAddress, String documentId) {
        this.messageId = UUID.randomUUID().toString();
        this.recipientServerAddress = recipientServerAddress;
        this.brokerServerAddress = brokerServerAddress;
        this.documentId = documentId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRecipientServerAddress() {
        return recipientServerAddress;
    }

    public void setRecipientServerAddress(String recipientServerAddress) {
        this.recipientServerAddress = recipientServerAddress;
    }

    public String getBrokerServerAddress() {
        return brokerServerAddress;
    }

    public void setBrokerServerAddress(String brokerServerAddress) {
        this.brokerServerAddress = brokerServerAddress;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public static String getTEMPLATE() {
        return TEMPLATE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ITI53NotifyMessage that = (ITI53NotifyMessage) o;
        return Objects.equals(messageId, that.messageId) &&
                Objects.equals(recipientServerAddress, that.recipientServerAddress) &&
                Objects.equals(brokerServerAddress, that.brokerServerAddress) &&
                Objects.equals(documentId, that.documentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, recipientServerAddress, brokerServerAddress, documentId);
    }

    protected String toXML() {
        return String.format(TEMPLATE, messageId, recipientServerAddress, brokerServerAddress, documentId);
    }

    public FinishRequest toFinishRequest() {
        String contentType = "Multipart/Related; start-info=\"application/soap+xml\"; type=\"application/xop+xml\"; boundary=\"------OPENHIM\";charset=UTF-8";
        return new FinishRequest(toXML(), contentType, HttpStatus.SC_OK);
    }
}
