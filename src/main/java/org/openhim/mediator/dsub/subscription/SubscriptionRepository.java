package org.openhim.mediator.dsub.subscription;

import java.util.List;

public interface SubscriptionRepository {

    void saveSubscription(Subscription subscription);

    void deleteSubscription(String uuid);

    List<Subscription> findActiveSubscriptions(String facility);
}
