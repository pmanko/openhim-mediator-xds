package org.openhim.mediator.denormalization;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.openhim.mediator.messages.ResolvePatientIdentifier;

/**
 * Messages supported:
 * <ul>
 * <li>ResolvePatientIdentifierMessage</li>
 * </ul>
 */
public class ResolveEnterpriseIdentifierActor extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);


    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof ResolvePatientIdentifier) {
            ActorRef pixRequester = getContext().actorOf(Props.create(PIXRequestActor.class));
            pixRequester.tell(msg, getSelf());
        } else {
            unhandled(msg);
        }
    }
}
