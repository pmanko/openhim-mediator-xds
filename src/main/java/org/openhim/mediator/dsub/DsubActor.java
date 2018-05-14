package org.openhim.mediator.dsub;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.openhim.mediator.dsub.repository.MongoSubscriptionRepository;
import org.openhim.mediator.dsub.repository.SubscriptionRepository;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;

public class DsubActor extends UntypedActor {

    private final MediatorConfig config;
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final SubscriptionRepository subRepo;

    public DsubActor(MediatorConfig config) {
        this.config = config;
        this.subRepo = new MongoSubscriptionRepository(config);
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
