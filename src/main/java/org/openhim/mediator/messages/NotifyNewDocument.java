package org.openhim.mediator.messages;

public class NotifyNewDocument {
    private String labOrderDocumentId;

    public NotifyNewDocument(String labOrderDocumentId) {
        this.labOrderDocumentId = labOrderDocumentId;
    }

    public String getLabOrderDocumentId() {
        return labOrderDocumentId;
    }
}
