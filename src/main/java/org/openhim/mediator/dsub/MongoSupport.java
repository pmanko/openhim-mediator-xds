package org.openhim.mediator.dsub;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public abstract class MongoSupport {

    protected static final String ID = "_id";

    private final MongoDatabase mongoDb;
    private final String collectionName;

    public MongoSupport(MongoDatabase mongoDb, String collectionName) {
        this.mongoDb = mongoDb;
        this.collectionName = collectionName;
    }

    protected MongoCollection<Document> getCollection() {
        return mongoDb.getCollection(collectionName);
    }
}
