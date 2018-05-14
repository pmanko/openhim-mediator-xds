package org.openhim.mediator.dsub.pull;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.openhim.mediator.dsub.MongoSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MongoPullPoint extends MongoSupport implements PullPoint {

    private final static String DOC_ID = "documentId";
    private final static String LOCATION_ID = "locationId";

    private final String locationId;

    MongoPullPoint(MongoDatabase mongoDb, String locationId) {
        super(mongoDb, "pull_point_documents");
        this.locationId = locationId;
    }

    @Override
    public List<String> getDocumentIds() {
        List<String> ids = new ArrayList<>();
        for (Document doc : getCollection().find(Filters.eq(LOCATION_ID, locationId))) {
            ids.add(doc.getString(DOC_ID));
        }
        return ids;
    }

    @Override
    public void registerDocument(String documentId) {
        getCollection().insertOne(new Document("_id", UUID.randomUUID())
            .append(DOC_ID, documentId)
            .append(LOCATION_ID, locationId));
    }
}
