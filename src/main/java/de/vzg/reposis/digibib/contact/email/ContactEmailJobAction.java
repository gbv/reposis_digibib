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

package de.vzg.reposis.digibib.contact.email;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.services.queuedjob.MCRJob;
import org.mycore.services.queuedjob.MCRJobAction;

import de.vzg.reposis.digibib.contact.collect.ContactInfoCollectorJobAction;
import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;
import de.vzg.reposis.digibib.contact.service.ContactTicketService;
import de.vzg.reposis.digibib.contact.service.ContactTicketServiceImpl;

/**
 * Implements a {@link MCRJobAction} that sends specified email for {@link ContactTicket}.
 */
public class ContactEmailJobAction extends MCRJobAction {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String CONTACT_TICKET_ID = "ticket_id";

    private static final String TO_EMAIL = "to_email";

    private static final String METHOD = "email_method";

    private static final ContactTicketService TICKET_SERVICE = ContactTicketServiceImpl.getInstance();

    /**
     * Constructs new {@link ContactInfoCollectorJobAction} from {@link MCRJob}.
     *
     * @param job job
     */
    public ContactEmailJobAction(MCRJob job) {
        super(job);
    }

    /**
     * Constructs and returns new {@link ContactInfoCollectorJobAction}
     * to send request confirmation.
     *
     * @param contactTicketId contact ticket id
     * @return job
     */
    public static MCRJob createRequestConfirmationJob(UUID contactTicketId) {
        final MCRJob job = new MCRJob(ContactEmailJobAction.class);
        job.setParameter(METHOD, Method.REQUEST_CONFIRMATION.toString());
        job.setParameter(CONTACT_TICKET_ID, contactTicketId.toString());
        return job;
    }

    /**
     * Constructs and returns new {@link ContactInfoCollectorJobAction}
     * to send new request info.
     *
     * @param contactTicketId contact ticket id
     * @param toEmail to email
     * @return job
     */
    public static MCRJob createNewRequestInfoJob(UUID contactTicketId, String toEmail) {
        final MCRJob job = new MCRJob(ContactEmailJobAction.class);
        job.setParameter(METHOD, Method.NEW_REQUEST_INFO.toString());
        job.setParameter(TO_EMAIL, toEmail);
        job.setParameter(CONTACT_TICKET_ID, contactTicketId.toString());
        return job;
    }

    /**
     * Constructs and returns new {@link ContactInfoCollectorJobAction}
     * to send new request completed confirmation.
     *
     * @param contactTicketId contact ticket id
     * @return job
     */
    public static MCRJob createRequestCompletedConfirmationJob(UUID contactTicketId) {
        final MCRJob job = new MCRJob(ContactEmailJobAction.class);
        job.setParameter(METHOD, Method.REQUEST_COMPLETED_CONFIRMATION.toString());
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
        final Method method = getMethod();
        try {
            switch (method) {
            case REQUEST_CONFIRMATION -> handleRequestConfirmation();
            case NEW_REQUEST_INFO -> handleNewRequestInfo();
            case REQUEST_COMPLETED_CONFIRMATION -> handleRequestCompletedConfirmation();
            }
        } catch (Exception e) {
            LOGGER.error("Job action failed: ", e);
            throw new ExecutionException(e);
        }
    }

    private void handleRequestConfirmation() {
        final ContactTicket contactTicket = TICKET_SERVICE.getContactTicketById(getContactTicketId());
        ContactEmailService.sendRequestConfirmation(contactTicket);
    }

    private void handleNewRequestInfo() {
        final ContactTicket contactTicket = TICKET_SERVICE.getContactTicketById(getContactTicketId());
        ContactEmailService.sendNewRequestInfo(contactTicket, getToEmail());
    }

    private void handleRequestCompletedConfirmation() {
        final ContactTicket contactTicket = TICKET_SERVICE.getContactTicketById(getContactTicketId());
        ContactEmailService.sendRequestCompletedConfirmation(contactTicket);
    }

    private UUID getContactTicketId() {
        return UUID.fromString(job.getParameter(CONTACT_TICKET_ID));
    }

    private String getToEmail() {
        return job.getParameter(TO_EMAIL);
    }

    private Method getMethod() {
        return Method.valueOf(job.getParameter(METHOD));
    }

    @Override
    public void rollback() {
        // nothing to rollback
    }

    /**
     * Enum representing the possible email methods.
     */
    private enum Method {

        REQUEST_CONFIRMATION,

        NEW_REQUEST_INFO,

        REQUEST_COMPLETED_CONFIRMATION,

    }

}
