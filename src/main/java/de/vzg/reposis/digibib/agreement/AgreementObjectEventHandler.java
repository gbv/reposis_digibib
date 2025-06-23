package de.vzg.reposis.digibib.agreement;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.common.events.MCREvent;
import org.mycore.common.events.MCREventHandlerBase;
import org.mycore.datamodel.metadata.MCRObject;

import de.vzg.reposis.digibib.agreement.service.AgreementService;
import de.vzg.reposis.digibib.agreement.service.ObjectAgreementService;

/**
 * Event handler that listens to updates on MCRObjects and enqueues agreement
 * transfer jobs based on object metadata and configuration.
 * <p>
 * This handler checks if an object is published, has agreements, and a genre.
 * It then determines the appropriate agreement id from configuration and
 * enqueues a job to transfer the agreement if not already done.
 */
public class AgreementObjectEventHandler extends MCREventHandlerBase {

    private static final Logger LOGGER = LogManager.getLogger();

    private final AgreementService agreementService;
    private final ObjectAgreementService objectAgreementService;

    /**
     * Constructs an {@link AgreementObjectEventHandler} with the given services.
     *
     * @param agreementService the {@link AgreementService} used for agreement-related operations
     * @param objectAgreementService the {@link ObjectAgreementService} used for object-agreement operations
     */
    public AgreementObjectEventHandler(AgreementService agreementService,
        ObjectAgreementService objectAgreementService) {
        this.agreementService = agreementService;
        this.objectAgreementService = objectAgreementService;
    }

    /**
     * Default constructor initializing with default agreement service.
     */
    public AgreementObjectEventHandler() {
        this(AgreementService.obtainInstance(), ObjectAgreementService.obtainInstance());
    }

    @Override
    protected void handleObjectUpdated(MCREvent evt, MCRObject obj) {
        if (objectAgreementService.hasAgreementTransmissionInfo(obj)) {
            LOGGER.debug("An agreement for {} was already transmitted, skipping...", obj.getId());
            return;
        }
        if (agreementService.isAgreementTransferScheduled(obj.getId())) {
            LOGGER.debug("transfer for {} is already scheduled", obj.getId());
            return;
        }
        if (!objectAgreementService.isPublished(obj)) {
            LOGGER.debug("{} is not published, skipping agreement transfer.", obj.getId());
            return;
        }

        if (!objectAgreementService.hasDoi(obj)) {
            LOGGER.debug("{} has no DOI, skipping agreement transfer.", obj.getId());
            return;
        }

        final Optional<String> requiredAgreementOpt = agreementService.resolveRequiredAgreementId(obj);
        if (requiredAgreementOpt.isEmpty()) {
            LOGGER.debug("{} has no required agreement configured, skipping.", obj.getId());
            return;
        }

        String requiredAgreement = requiredAgreementOpt.get();
        if (!objectAgreementService.hasConfirmedAgreement(obj, requiredAgreement)) {
            LOGGER.debug("{} already has not required agreement '{}', skipping transfer.", obj.getId(),
                requiredAgreement);
            return;
        }
        agreementService.scheduleAgreementTransfer(obj.getId(), requiredAgreement);

        LOGGER.info("Scheduled transfer for agreement '{}' for object {}.", requiredAgreement, obj.getId());
    }
}
