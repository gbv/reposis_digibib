package de.vzg.reposis.digibib.agreement.event;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.common.config.MCRConfiguration2;
import org.mycore.common.events.MCREvent;
import org.mycore.common.events.MCREventHandlerBase;
import org.mycore.datamodel.metadata.MCRObject;
import org.mycore.mods.MCRMODSWrapper;
import org.mycore.services.queuedjob.MCRJobQueue;
import org.mycore.services.queuedjob.MCRJobQueueManager;

import de.vzg.reposis.digibib.agreement.factory.AgreementContentFactory;
import de.vzg.reposis.digibib.agreement.job.TransferAgreementJobAction;
import de.vzg.reposis.digibib.agreement.model.Agreement;

public class AgreementObjectEventHandler extends MCREventHandlerBase {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String CONFIG_PREFIX = "Digibib.Agreement.";

    private static final String AGREEMENT_DEFAULT_NAME_CONF = "Default";

    private static final String AGREEMENT_FLAG = "agreement";

    private static final String STATE_PUBLISHED = "published";

    private final AgreementContentFactory contentFactory;

    private final MCRJobQueue jobQueue;

    public AgreementObjectEventHandler(MCRJobQueue jobQueue, AgreementContentFactory contentFactory) {
        this.contentFactory = contentFactory;
        this.jobQueue = jobQueue;
    }

    public AgreementObjectEventHandler() {
        this(MCRJobQueueManager.getInstance().getJobQueue(TransferAgreementJobAction.class),
            new AgreementContentFactory());
    }

    @Override
    protected void handleObjectUpdated(MCREvent evt, MCRObject obj) {
        // TODO check state changed to publish otherwise return
        // TODO check doi exists
        final String objId = obj.getId().toString();
        if (!isPublished(obj)) {
            LOGGER.debug("{} is not published. Skipping...", objId);
            return;
        }
        final List<String> agreements = getExistingAgreements(obj);
        if (agreements.isEmpty()) {
            LOGGER.debug("{} has no agreements. Skipping...", objId);
            return;
        }
        final Optional<String> genre = extractGenreFromMods(obj);
        if (genre.isEmpty()) {
            LOGGER.debug("{} has no genre. Skipping...", objId);
            return;
        }
        final Optional<String> agreementName = getAgreementName(genre.get()).or(() -> getDefaultAgreementName());
        if (agreementName.isEmpty()) {
            LOGGER.debug("No agreement name configured (including fallback). Skipping...");
            return;
        }
        enqueueAgreementJobIfMissing(obj, agreements, agreementName.get());
    }

    private void enqueueAgreementJobIfMissing(MCRObject obj, List<String> existingAgreements,
        String requiredAgreementName) {
        final String objId = obj.getId().toString();
        if (!existingAgreements.contains(requiredAgreementName)) {
            LOGGER.debug("{} does not have the required agreement '{}'. Proceeding...", objId, requiredAgreementName);
            return;
        }
        LOGGER.debug("Adding DeliverAgreementJob for {} with agreement '{}'.", objId, requiredAgreementName);
        final Agreement agreement = new Agreement(requiredAgreementName, contentFactory.fromObject(obj));
        jobQueue.add(TransferAgreementJobAction.createJob(agreement));
        // TODO remove not necessary agreements?
        // TODO save transfer time?
    }

    private List<String> getExistingAgreements(MCRObject obj) {
        return obj.getService().getFlags(AGREEMENT_FLAG);
    }

    private Optional<String> extractGenreFromMods(MCRObject object) {
        return new MCRMODSWrapper(object).getElements("mods:genre").stream().map(g -> g.getAttributeValue("valueURI"))
            .filter(Objects::nonNull).map(uri -> uri.split("#", 2)).filter(uri -> uri.length > 1).map(uri -> uri[1])
            .findFirst();
    }

    private static Optional<String> getDefaultAgreementName() {
        return getAgreementName(AGREEMENT_DEFAULT_NAME_CONF);
    }

    private static Optional<String> getAgreementName(String genre) {
        return MCRConfiguration2.getString(CONFIG_PREFIX + genre + ".Name");
    }

    private boolean isPublished(MCRObject object) {
        return Objects.equals(STATE_PUBLISHED, object.getService().getState().getId());
    }
}
