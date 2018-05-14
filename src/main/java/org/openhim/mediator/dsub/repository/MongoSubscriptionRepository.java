package org.openhim.mediator.dsub.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.openhim.mediator.engine.MediatorConfig;

public class MongoSubscriptionRepository implements SubscriptionRepository {

    private static final String ID = "_id";

    private final MongoClient mongoClient;

    public MongoSubscriptionRepository(MediatorConfig mediatorConfig) {
        mongoClient = new MongoClient(mediatorConfig.getProperty("mediator.mongoUrl"));
    }

    @Override
    public void saveSubscription(Subscription subscription) {
        String id = subscription.getName();

        MongoCollection<Document> collection = getCollection();

        Document doc = new Document(ID, subscription.getName())
                .append("url", subscription.getUrl());
        collection.insertOne(doc);

        Document existing = collection.find(Filters.eq(ID, id)).first();
        if (existing == null) {
            collection.insertOne(doc);
        } else {
            collection.updateOne(Filters.eq(ID, id), doc);
        }
    }

    private MongoCollection<Document> getCollection() {
        MongoDatabase db = mongoClient.getDatabase("dsub");
        return db.getCollection("dsub_subscriptions");
    }

}
