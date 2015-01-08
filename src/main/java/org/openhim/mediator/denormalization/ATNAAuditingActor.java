package org.openhim.mediator.denormalization;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import ihe.iti.atna.AuditMessage;
import ihe.iti.atna.EventIdentificationType;
import org.openhim.mediator.ATNAUtil;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.MediatorSocketRequest;
import org.openhim.mediator.messages.ATNAAudit;

import javax.xml.bind.JAXBException;
import java.math.BigInteger;
import java.util.UUID;

/**
 * An actor for sending out audit messages to an audit repository.
 * <br/><br/>
 * Messages supported:
 * <ul>
 *     <li>ATNAAudit - fire-and-forget</li>
 * </ul>
 */
public class ATNAAuditingActor extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private MediatorConfig config;


    public ATNAAuditingActor(MediatorConfig config) {
        this.config = config;
    }

    protected String generateForPIXRequest(ATNAAudit audit) throws JAXBException {
        AuditMessage res = new AuditMessage();

        EventIdentificationType eid = new EventIdentificationType();
        eid.setEventID( ATNAUtil.buildCodedValueType("DCM", "110112", "Query") );
        eid.setEventActionCode("E");
        eid.setEventDateTime( ATNAUtil.newXMLGregorianCalendar() );
        eid.getEventTypeCode().add( ATNAUtil.buildCodedValueType("IHE Transactions", "ITI-9", "PIX Query") );
        eid.setEventOutcomeIndicator(audit.getPatientIdentifier()!=null ? BigInteger.ZERO : new BigInteger("4"));
        res.setEventIdentification(eid);

        res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant(
                config.getProperties().getProperty("pix.sendingFacility") + "|" + config.getProperties().getProperty("pix.sendingApplication"),
                ATNAUtil.getProcessID(), true, ATNAUtil.getHostIP(), (short)2, "DCM", "110153", "Source"));
        res.getActiveParticipant().add( ATNAUtil.buildActiveParticipant(
                config.getProperties().getProperty("pix.receivingFacility") + "|" + config.getProperties().getProperty("pix.receivingApplication"),
                "2100", false, config.getProperties().getProperty("pix.manager.host"), (short)1, "DCM", "110152", "Destination"));

        res.getAuditSourceIdentification().add(ATNAUtil.buildAuditSource("openhim"));

        res.getParticipantObjectIdentification().add(
                ATNAUtil.buildParticipantObjectIdentificationType(audit.getPatientIdentifier().toCX(), (short)1, (short)1, "RFC-3881", "2", "PatientNumber", null)
        );
        res.getParticipantObjectIdentification().add(
                ATNAUtil.buildParticipantObjectIdentificationType(
                        UUID.randomUUID().toString(), (short)2, (short)24, "IHE Transactions", "ITI-9", "PIX Query",
                        audit.getMessage(), new ATNAUtil.ParticipantObjectDetail("MSH-10", audit.getMsh10().getBytes())
                )
        );

        return ATNAUtil.marshallATNAObject(res);
    }

    private void sendAuditMessage(ATNAAudit audit)
            throws Exception { //Just die if something goes wrong, akka will restart

        log.info("Sending ATNA " + audit.getType() + " audit message using UDP");

        ActorSelection udpConnector = getContext().actorSelection("/user/" + config.getName() + "/udp-fire-forget-connector");
        String message = null;

        switch (audit.getType()) {
            case PIX_REQUEST: message = generateForPIXRequest(audit); break;
        }

        message = ATNAUtil.build_TCP_Msg_header() + message;
        message = message.length() + " " + message + "\r\n";

        MediatorSocketRequest request = new MediatorSocketRequest(
                ActorRef.noSender(), getSelf(), "ATNA Audit",
                config.getProperties().getProperty("atna.host"),
                Integer.parseInt(config.getProperties().getProperty("atna.udpPort")),
                message
        );

        udpConnector.tell(request, getSelf());
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof ATNAAudit) {
            sendAuditMessage((ATNAAudit) msg);
        } else {
            unhandled(msg);
        }
    }
}
