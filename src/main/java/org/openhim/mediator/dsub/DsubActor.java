package org.openhim.mediator.dsub;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.openhim.mediator.dsub.pull.PullPointFactory;
import org.openhim.mediator.dsub.repository.MongoSubscriptionRepository;
import org.openhim.mediator.dsub.repository.SubscriptionRepository;
import org.openhim.mediator.dsub.service.DsubService;
import org.openhim.mediator.dsub.service.DsubServiceImpl;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;

public class DsubActor extends UntypedActor {

    private final MediatorConfig config;
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final MongoDatabase mongoDb;

    private DsubService dsubService;

    public DsubActor(MediatorConfig config) {
        this.config = config;

        MongoClient mongoClient = new MongoClient(config.getProperty("mediator.mongoUrl"));
        mongoDb = mongoClient.getDatabase("dsub");

        PullPointFactory pullPointFactory = new PullPointFactory(mongoDb);
        SubscriptionRepository subRepo = new MongoSubscriptionRepository(mongoDb, log);

        dsubService = new DsubServiceImpl(pullPointFactory, subRepo, log);
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof MediatorHTTPRequest) {
            readMessage((MediatorHTTPRequest) msg);
        }
    }

    private void readMessage(MediatorHTTPRequest request) {
        // TOOD: read SOAP msg here
    }
}
