package de.vzg.reposis.digibib.agreement.service;

import org.mycore.access.MCRAccessException;
import org.mycore.common.MCRPersistenceException;
import org.mycore.datamodel.metadata.MCRMetadataManager;
import org.mycore.datamodel.metadata.MCRObject;
import org.mycore.datamodel.metadata.MCRObjectID;

import de.vzg.reposis.digibib.agreement.exceptions.AgreementException;

/**
 * Wrapper service for accessing and modifying MyCoRe metadata via {@link MCRMetadataManager}.
 * <p>
 * This class delegates calls to {@link MCRMetadataManager} but exists mainly to
 * increase testability by allowing the metadata access layer to be mocked or replaced
 * in unit and integration tests.
 */
public class AgreementMetadataManager {

    /**
     * Returns a shared singleton instance of the AgreementMetadataManager.
     *
     * @return the shared singleton instance of AgreementMetadataManager
     */
    public static AgreementMetadataManager obtainInstance() {
        return InstanceHolder.SHARED_INSTANCE;
    }

    /**
     * Creates a new, independent instance of AgreementMetadataManager.
     *
     * @return a new instance of AgreementMetadataManager
     */
    public static AgreementMetadataManager createInstance() {
        return new AgreementMetadataManager();
    }

    /**
     * Checks whether a metadata object with the given ID exists in the repository.
     *
     * @param objectId the ID of the object to check
     * @return {@code true} if the object exists, otherwise {@code false}
     */
    public boolean exists(MCRObjectID objectId) {
        return MCRMetadataManager.exists(objectId);
    }

    /**
     * Retrieves a metadata object with the given ID.
     *
     * @param objectId the ID of the object to retrieve
     * @return the retrieved {@link MCRObject}
     */
    public MCRObject retrieveMCRObject(MCRObjectID objectId) {
        return MCRMetadataManager.retrieveMCRObject(objectId);
    }

    /**
     * Updates the given metadata object in the repository.
     * <p>
     * Wraps any persistence or access exceptions into an {@link AgreementException}
     * for consistent error handling in agreement-related services.
     *
     * @param object the {@link MCRObject} to update
     * @throws AgreementException if the update fails
     */
    public void update(MCRObject object) {
        try {
            MCRMetadataManager.update(object);
        } catch (MCRPersistenceException | MCRAccessException e) {
            throw new AgreementException("Cannot update object", e);
        }
    }

    private static final class InstanceHolder {
        private static final AgreementMetadataManager SHARED_INSTANCE = createInstance();
    }
}
