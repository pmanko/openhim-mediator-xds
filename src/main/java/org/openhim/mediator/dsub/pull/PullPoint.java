package org.openhim.mediator.dsub.pull;

import java.util.List;

public interface PullPoint {

    List<String> getDocumentIds();

    void registerDocument(String documentId);
}
