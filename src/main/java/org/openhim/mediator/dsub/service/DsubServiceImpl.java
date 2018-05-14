package org.openhim.mediator.dsub.service;

import akka.event.LoggingAdapter;
import org.openhim.mediator.dsub.pull.PullPoint;
import org.openhim.mediator.dsub.pull.PullPointFactory;
import org.openhim.mediator.dsub.repository.SubscriptionRepository;

import java.util.List;

public class DsubServiceImpl implements DsubService {

    private final PullPointFactory pullPointFactory;
    private final SubscriptionRepository subscriptionRepository;
    private final LoggingAdapter log;

    public DsubServiceImpl(PullPointFactory pullPointFactory,
                           SubscriptionRepository subscriptionRepository,
                           LoggingAdapter log) {
        this.pullPointFactory = pullPointFactory;
        this.subscriptionRepository = subscriptionRepository;
        this.log = log;
    }


    @Override
    public void createSubscription(String url) {
        log.info("Request to create subscription for: " + url);
        subscriptionRepository.saveSubscription(url);
    }

    @Override
    public void deleteSubscription(String url) {
        log.info("Request to delete subscription for: " + url);
        subscriptionRepository.deleteSubscription(url);
    }

    @Override
    public void notifyNewDocument(String docId) {

    }

    @Override
    public void newDocumentForPullPoint(String docId, String locationId) {
        PullPoint pullPoint = pullPointFactory.get(locationId);
        pullPoint.registerDocument(docId);
    }

    @Override
    public List<String> getDocumentsForPullPoint(String locationId) {
        PullPoint pullPoint = pullPointFactory.get(locationId);
        return pullPoint.getDocumentIds();
    }
}
