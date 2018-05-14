package org.openhim.mediator.dsub.service;

import java.util.List;

public interface DsubService {

    void createSubscription(String url);

    void deleteSubscription(String url);

    void notifyNewDocument(String docId);

    void newDocumentForPullPoint(String docId, String locationId);

    List<String> getDocumentsForPullPoint(String locationId);
}
