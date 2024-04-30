/*
 * This file is part of ***  M y C o R e  ***
 * See http://www.mycore.de/ for details.
 *
 * MyCoRe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyCoRe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyCoRe.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.vzg.reposis.digibib.contactrequest.collect;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.datamodel.metadata.MCRMetadataManager;
import org.mycore.datamodel.metadata.MCRObject;
import org.mycore.services.queuedjob.MCRJob;
import org.mycore.services.queuedjob.MCRJobAction;

import de.vzg.reposis.digibib.contactrequest.dto.ContactInfoDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.exception.ContactRequestNotFoundException;
import de.vzg.reposis.digibib.contactrequest.service.ContactRequestFactory;
import de.vzg.reposis.digibib.contactrequest.service.ContactRequestService;

/**
 * This class implements a {@link MCRJobAction} that collects {@link ContactInfoDto} elements
 * for a contact request from a {@link MCRObject}.
 */
public class ContactInfoCollectorJobAction extends MCRJobAction {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String CONTACT_REQUEST_ID = "contact_request_id";

    private static final ContactRequestService CONTACT_REQUEST_SERVICE
        = ContactRequestFactory.getContactRequestService();

    /**
     * Constructs a new {@code ContactInfoCollectorJobAction} from the given {@link MCRJob} instance.
     *
     * @param job the job instance used to construct the job action
     */
    public ContactInfoCollectorJobAction(MCRJob job) {
        super(job);
    }

    /**
     * Creates and returns a new {@link MCRJob} instance configured for processing a contact request
     * to collect contact information from the linked {@link MCRObject}.
     *
     * @param contactRequestId the id of the contact request to be stored in the job instance
     * @return a new job instance for the specified contact request
     */
    public static MCRJob createJob(UUID contactRequestId) {
        final MCRJob job = new MCRJob(ContactInfoCollectorJobAction.class);
        job.setParameter(CONTACT_REQUEST_ID, contactRequestId.toString());
        return job;
    }

    @Override
    public boolean isActivated() {
        return true;
    }

    @Override
    public String name() {
        return getClass().getName();
    }

    @Override
    public void execute() throws ExecutionException {
        final UUID contactRequestId = getContactRequestId();
        LOGGER.info("Started to collect contacts for request: {}", contactRequestId);
        try {
            collectContactInfos(contactRequestId);
        } catch (ContactRequestNotFoundException e) {
            // ignore
        } catch (Exception e) {
            LOGGER.error("Job action failed: ", e);
            throw new ExecutionException(e);
        }
    }

    private void collectContactInfos(UUID contactRequestId) {
        final ContactRequestDto contactRequest = CONTACT_REQUEST_SERVICE.getContactRequestById(contactRequestId);
        final MCRObject object = MCRMetadataManager.retrieveMCRObject(contactRequest.getObjectId());
        new ContactInfoCollectorService().collectContactInfos(object).stream()
            .forEach(i -> CONTACT_REQUEST_SERVICE.createContactInfo(contactRequestId, i));
    }

    private UUID getContactRequestId() {
        return UUID.fromString(job.getParameter(CONTACT_REQUEST_ID));
    }

    @Override
    public void rollback() {
        // nothing to rollback
    }

}
