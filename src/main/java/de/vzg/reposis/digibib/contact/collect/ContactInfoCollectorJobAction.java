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

package de.vzg.reposis.digibib.contact.collect;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.datamodel.metadata.MCRMetadataManager;
import org.mycore.datamodel.metadata.MCRObject;
import org.mycore.services.queuedjob.MCRJob;
import org.mycore.services.queuedjob.MCRJobAction;

import de.vzg.reposis.digibib.contact.exception.ContactTicketNotFoundException;
import de.vzg.reposis.digibib.contact.mapper.ContactInfoMapper;
import de.vzg.reposis.digibib.contact.persistence.model.ContactInfo;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;
import de.vzg.reposis.digibib.contact.service.ContactInfoService;
import de.vzg.reposis.digibib.contact.service.ContactInfoServiceImpl;
import de.vzg.reposis.digibib.contact.service.ContactTicketService;
import de.vzg.reposis.digibib.contact.service.ContactTicketServiceImpl;

/**
 * Implements a {@link MCRJobAction} that collects {@link ContactInfo} elements for {@link ContactTicket}.
 */
public class ContactInfoCollectorJobAction extends MCRJobAction {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String CONTACT_TICKET_ID = "ticket_id";

    private static final ContactTicketService TICKET_SERVICE = ContactTicketServiceImpl.getInstance();

    private static final ContactInfoService CONTACT_INFO_SERVICE = ContactInfoServiceImpl.getInstance();

    /**
     * Constructs new {@link ContactInfoCollectorJobAction} from {@link MCRJob}.
     *
     * @param job job
     */
    public ContactInfoCollectorJobAction(MCRJob job) {
        super(job);
    }

    /**
     * Constructs and returns new {@link ContactInfoCollectorJobAction}.
     *
     * @param contactTicketId contact ticket id
     * @return job
     */
    public static MCRJob createJob(UUID contactTicketId) {
        final MCRJob job = new MCRJob(ContactInfoCollectorJobAction.class);
        job.setParameter(CONTACT_TICKET_ID, contactTicketId.toString());
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
        final UUID contactTicketId = getContactTicketId();
        LOGGER.info("Started to collect contacts for ticket: {}", contactTicketId);
        try {
            collectContactInfos(contactTicketId);
        } catch (ContactTicketNotFoundException e) {
            // ignore
        } catch (Exception e) {
            LOGGER.error("Job action failed: ", e);
            throw new ExecutionException(e);
        }
    }

    private void collectContactInfos(UUID contactTicketId) {
        final ContactTicket contactTicket = TICKET_SERVICE.getContactTicketById(contactTicketId);
        final MCRObject object = MCRMetadataManager.retrieveMCRObject(contactTicket.getObjectId());
        ContactInfoCollectorService.collectContactInfos(object).stream().map(ContactInfoMapper::toDto)
            .forEach(i -> CONTACT_INFO_SERVICE.createContactInfo(contactTicketId, i));
    }

    private UUID getContactTicketId() {
        return UUID.fromString(job.getParameter(CONTACT_TICKET_ID));
    }

    @Override
    public void rollback() {
        // nothing to rollback
    }

}
