package org.openhim.mediator.dsub.subscription;

import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.messages.ITI53NotifyMessage;

public class SoapSubscriptionNotifier implements SubscriptionNotifier {

    private MediatorConfig config;

    public SoapSubscriptionNotifier(MediatorConfig config) {
        this.config = config;
    }

    @Override
    public void notifySubscription(Subscription subscription, String documentId) {
        String hostAdress = config.getProperty("core.host");
        ITI53NotifyMessage message = new ITI53NotifyMessage(subscription.getUrl(), hostAdress, documentId);


    }
}
