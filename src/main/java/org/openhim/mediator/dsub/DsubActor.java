package org.openhim.mediator.dsub;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.oasis_open.docs.wsn.b_2.CreatePullPoint;
import org.oasis_open.docs.wsn.b_2.DestroyPullPoint;
import org.oasis_open.docs.wsn.b_2.GetCurrentMessage;
import org.oasis_open.docs.wsn.b_2.GetMessages;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.oasis_open.docs.wsn.b_2.PauseSubscription;
import org.oasis_open.docs.wsn.b_2.Renew;
import org.oasis_open.docs.wsn.b_2.ResumeSubscription;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.openhim.mediator.dsub.pull.PullPointFactory;
import org.openhim.mediator.dsub.subscription.MongoSubscriptionRepository;
import org.openhim.mediator.dsub.subscription.SoapSubscriptionNotifier;
import org.openhim.mediator.dsub.subscription.SubscriptionNotifier;
import org.openhim.mediator.dsub.subscription.SubscriptionRepository;
import org.openhim.mediator.dsub.service.DsubService;
import org.openhim.mediator.dsub.service.DsubServiceImpl;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

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
        SubscriptionNotifier subNotifier = new SoapSubscriptionNotifier();

        dsubService = new DsubServiceImpl(pullPointFactory, subRepo,
                subNotifier, log);
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof MediatorHTTPRequest) {
            readMessage((MediatorHTTPRequest) msg);
        }
    }

    private void readMessage(MediatorHTTPRequest request) {

        Object result = parseMessage(request);

        if (result instanceof Subscribe) {
            Subscribe subscribeRequest = (Subscribe) result;
            //subscribe request handling
        } else if (result instanceof Unsubscribe) {
            Unsubscribe unsubscribeRequest = (Unsubscribe) result;
            //unsubscribe request handling
        } else if (result instanceof GetCurrentMessage) {
            GetCurrentMessage getCurrentMessageRequest = (GetCurrentMessage) result;
            //get current message request handling
        } else if (result instanceof GetMessages) {
            GetMessages getMessagesRequest = (GetMessages) result;
            //get messages request handling
        } else if (result instanceof DestroyPullPoint) {
            DestroyPullPoint destroyPullPointRequest = (DestroyPullPoint) result;
            //destroy pull point request handling
        } else if (result instanceof CreatePullPoint) {
            CreatePullPoint createPullPointRequest = (CreatePullPoint) result;
            //create pull point request handling
        } else if (result instanceof Renew) {
            Renew renew = (Renew) result;
            //renew request handling
        } else if (result instanceof PauseSubscription) {
            PauseSubscription pauseSubscriptionRequest = (PauseSubscription) result;
            //pause subscription request handling
        } else if (result instanceof ResumeSubscription) {
            ResumeSubscription resumeSubscriptionRequest = (ResumeSubscription) result;
            //resume subscription request handling
        } else if (result instanceof Notify) {
            Notify notifyRequest = (Notify) result;
            //notify request handling
        } else {
            //unknown request type handling
        }
    }

    private Object parseMessage(MediatorHTTPRequest request) {
        Object result;
        try {
            String parsedRequest = DsubUtil.parseRequest(request);
            result = DsubUtil.extractRequestMessage(parsedRequest);
        } catch (ParserConfigurationException | SAXException | IOException | JAXBException | TransformerException e) {
            log.error(e, "Parsing Dsub request failure");
            result = null;
        }
        return result;
    }
}
