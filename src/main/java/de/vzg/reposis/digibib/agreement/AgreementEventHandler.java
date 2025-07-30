package de.vzg.reposis.digibib.agreement;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.common.events.MCREvent;
import org.mycore.common.events.MCREventHandlerBase;
import org.mycore.datamodel.classifications2.MCRCategoryDAOFactory;
import org.mycore.datamodel.classifications2.MCRCategoryID;
import org.mycore.datamodel.metadata.MCRObject;
import org.mycore.mods.MCRMODSWrapper;
import org.mycore.services.queuedjob.MCRJobQueueManager;

public class AgreementEventHandler extends MCREventHandlerBase {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String AGREEMENT_FLAG = "agreement";

    @Override
    protected void handleObjectUpdated(MCREvent evt, MCRObject obj) {
        if (!checkPublishState(obj)) {
            LOGGER.debug(() -> obj.getId().toString() + " is not published. Skipping...");
            return;
        }

        final List<String> currentAgreements = obj.getService().getFlags(AGREEMENT_FLAG);
        if (currentAgreements.isEmpty()) {
            LOGGER.debug(() -> obj.getId().toString() + " has no agreements. Skipping...");
            return;
        }

        final Optional<String> genre = getGenre(obj);
        if (genre.isEmpty()) {
            LOGGER.debug(() -> obj.getId().toString() + " has no genre. Skipping...");
            return;
        }

        final Optional<String> agreementName = getAgreementName(genre.get());
        if (agreementName.isEmpty()) {
            LOGGER.debug(() -> genre.get() + " requires no agreement. Skipping...");
            return;
        }

        if (!currentAgreements.contains(agreementName.get())) {
            LOGGER.debug(() -> obj.getId().toString() + " has no valid agreement. Skipping...");
            return;
        }

        MCRJobQueueManager.getInstance().getJobQueue(DeliverAgreementJobAction.class)
            .add(DeliverAgreementJobAction.createJob(obj.getId()));
    }

    private Optional<String> getGenre(MCRObject object) {
        return new MCRMODSWrapper(object).getElements("mods:genre").stream().map(g -> g.getAttributeValue("valueURI"))
            .filter(Objects::nonNull).map(uri -> uri.split("#", 2)).filter(uri -> uri.length > 1).map(uri -> uri[1])
            .findFirst();
    }

    private Optional<String> getAgreementName(String genre) {
        // TODO load file name from properties
        return MCRCategoryDAOFactory.getInstance().getCategory(MCRCategoryID.fromString("mir_genres"), -1).getChildren()
            .stream().filter(c -> Objects.equals(genre, c.getId().getId()))
            .map(c -> c.getLabel("x-agree")).filter(Optional::isPresent).map(c -> c.get().getText()).findFirst();
    }

    private boolean checkPublishState(MCRObject object) {
        return Objects.equals("published", object.getService().getState().getId());
    }
}
