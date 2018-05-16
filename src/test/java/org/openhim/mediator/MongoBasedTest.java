package org.openhim.mediator;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.junit.After;
import org.junit.Before;

public abstract class MongoBasedTest {

    private MongoDatabase mongoDb;

    @Before
    public void setUp() {
        MongoClient mongoClient =
                new MongoClient("localhost");

        mongoDb = mongoClient.getDatabase("mongo_test");

        mongoInitialized(mongoDb);
    }

    @After
    public void tearDown() {
        if (mongoDb != null) {
            mongoDb.drop();
        }
    }

    protected abstract void mongoInitialized(MongoDatabase mongoDb);
}
