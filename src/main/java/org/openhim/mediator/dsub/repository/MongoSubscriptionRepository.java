package org.openhim.mediator.dsub.repository;

import akka.event.LoggingAdapter;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.openhim.mediator.dsub.MongoSupport;

import java.util.UUID;

public class MongoSubscriptionRepository extends MongoSupport implements SubscriptionRepository {

    private static final String URL = "url";

    private final LoggingAdapter log;

    public MongoSubscriptionRepository(MongoDatabase mongoDb, LoggingAdapter log) {
        super(mongoDb, "subscriptions");
        this.log = log;
    }

    @Override
    public void saveSubscription(String url) {
        MongoCollection<Document> collection = getCollection();

        Document existing = collection.find(Filters.eq(URL, url)).first();
        if (existing == null) {
            log.info("Saving subscription for: "+ url);

            Document doc = new Document(ID, UUID.randomUUID())
                    .append("url", url);
            collection.insertOne(doc);
        } else {
            log.warning("Subscription already exists: " + url);
        }
    }

    @Override
    public void deleteSubscription(String url) {
        DeleteResult result = getCollection().deleteMany(Filters.eq(URL, url));
        log.info("Deleted " + result.getDeletedCount() + " subscriptions for: " + url);
    }
}
