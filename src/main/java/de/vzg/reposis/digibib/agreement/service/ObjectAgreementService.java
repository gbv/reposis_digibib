package de.vzg.reposis.digibib.agreement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mycore.datamodel.metadata.MCRObject;
import org.mycore.mods.MCRMODSWrapper;

import de.vzg.reposis.digibib.agreement.model.TransmissionInfo;
import de.vzg.reposis.digibib.agreement.serialization.TransmissionInfoJsonMapper;

/**
 * Service class for reading and modifying agreement-related metadata
 * in {@link MCRObject} instances.
 */
public class ObjectAgreementService {

    private static final String CONFIRMED_AGREEMENT_ID_FLAG = "confirmedAgreementId";
    private static final String AGREEMENT_TRANSMISSION_INFO_FLAG = "agreementTransmissionInfo";
    private static final String STATE_PUBLISHED = "published";

    private final TransmissionInfoJsonMapper mapper;

    /**
     * Creates a new service instance using the given JSON mapper for
     * (de)serializing agreement transmission information.
     *
     * @param mapper the JSON mapper to use
     */
    public ObjectAgreementService(TransmissionInfoJsonMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Returns the shared singleton instance of {@code ObjectAgreementService}.
     *
     * @return the shared {@code ObjectAgreementService} instance
     */
    public static ObjectAgreementService obtainInstance() {
        return InstanceHolder.SHARED_INSTANCE;
    }

    /**
     * Creates a new instance of {@code ObjectAgreementService}.
     *
     * @return a new {@code ObjectAgreementService} instance
     */
    public static ObjectAgreementService createInstance() {
        return new ObjectAgreementService(TransmissionInfoJsonMapper.obtainInstance());
    }

    /**
     * Extracts the genre from the given object's MODS metadata.
     * <p>
     * The method expects a {@code mods:genre} element with a {@code valueURI}
     * attribute containing a "#" character. The part after "#" is returned.
     *
     * @param obj the MCRObject to read from
     * @return an {@link Optional} containing the genre value, or empty if none found
     */
    public Optional<String> extractGenre(MCRObject obj) {
        return new MCRMODSWrapper(obj).getElements("mods:genre").stream()
            .map(g -> g.getAttributeValue("valueURI"))
            .filter(Objects::nonNull)
            .map(uri -> uri.split("#", 2))
            .filter(uri -> uri.length > 1)
            .map(uri -> uri[1])
            .findFirst();
    }

    /**
     * Checks if the given object is published.
     *
     * @param obj the MCRObject to check
     * @return {@code true} if the object is published, otherwise {@code false}
     */
    public boolean isPublished(MCRObject obj) {
        return Objects.equals(STATE_PUBLISHED, obj.getService().getState().getId());
    }

    public String getDoi(MCRObject obj) {
        return new MCRMODSWrapper(obj).getElementValue("mods:identifier[@type='doi']");
    }

    /**
     * Checks if the given object has at least one DOI assigned in its MODS metadata.
     *
     * @param obj the MCRObject to check
     * @return {@code true} if the object has a DOI, otherwise {@code false}
     */
    public boolean hasDoi(MCRObject obj) {
        return getDoi(obj) != null;
    }

    /**
     * Returns all confirmed agreements for the given object.
     *
     * @param obj the MCRObject to read from
     * @return a list of confirmed agreement ids, possibly empty
     */
    public List<String> getConfirmedAgreements(MCRObject obj) {
        return obj.getService().getFlags(CONFIRMED_AGREEMENT_ID_FLAG);
    }

    /**
     * Checks whether the given object has a confirmed agreement with the specified id.
     *
     * @param obj the MCRObject to check
     * @param agreementId the agreement id to look for
     * @return {@code true} if the object has this agreement, otherwise {@code false}
     */
    public boolean hasConfirmedAgreement(MCRObject obj, String agreementId) {
        return getConfirmedAgreements(obj).contains(agreementId);
    }

    /**
     * Removes all confirmed agreement flags from the given object.
     *
     * @param obj the MCRObject to modify
     */
    public void removeConfirmedAgreements(MCRObject obj) {
        obj.getService().removeFlags(CONFIRMED_AGREEMENT_ID_FLAG);
    }

    /**
     * Checks whether the object has agreement transmission information set.
     *
     * @param obj the MCRObject to check
     * @return {@code true} if transmission info is present, otherwise {@code false}
     */
    public boolean hasAgreementTransmissionInfo(MCRObject obj) {
        return !obj.getService().getFlags(AGREEMENT_TRANSMISSION_INFO_FLAG).isEmpty();
    }

    /**
     * Stores current agreement transmission information for the given object.
     * <p>
     * The information contains the provided agreement id and the current timestamp.
     * Any previous transmission info flags will be removed before adding the new one.
     *
     * @param obj the MCRObject to update
     * @param agreementId the id of the transmitted agreement
     */
    public void setAgreementTransmissionInfoNow(MCRObject obj, String agreementId) {
        final TransmissionInfo info = new TransmissionInfo(agreementId, LocalDateTime.now());
        final String json = mapper.toJson(info);
        obj.getService().removeFlags(AGREEMENT_TRANSMISSION_INFO_FLAG);
        obj.getService().addFlag(AGREEMENT_TRANSMISSION_INFO_FLAG, json);
    }

    /**
     * Removes all agreement transmission information from the given object.
     *
     * @param obj the MCRObject to modify
     */
    public void removeAgreementTransmissionInfo(MCRObject obj) {
        obj.getService().removeFlags(AGREEMENT_TRANSMISSION_INFO_FLAG);
    }

    private static final class InstanceHolder {
        private static final ObjectAgreementService SHARED_INSTANCE = createInstance();
    }

}
