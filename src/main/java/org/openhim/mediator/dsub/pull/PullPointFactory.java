package org.openhim.mediator.dsub.pull;

import com.mongodb.client.MongoDatabase;

public class PullPointFactory {

    private final MongoDatabase mongoDb;

    public PullPointFactory(MongoDatabase mongoDb) {
        this.mongoDb = mongoDb;
    }

    public PullPoint get(String locationId) {
        return new MongoPullPoint(mongoDb, locationId);
    }
}
