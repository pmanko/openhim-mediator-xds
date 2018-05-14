package org.openhim.mediator.dsub.repository;

public interface SubscriptionRepository {
    void saveSubscription(String url);

    void deleteSubscription(String url);
}
