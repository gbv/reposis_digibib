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

package de.vzg.reposis.digibib.contactrequest.email;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.services.queuedjob.MCRJob;
import org.mycore.services.queuedjob.MCRJobAction;

import de.vzg.reposis.digibib.contactrequest.collect.ContactInfoCollectorJobAction;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactRequest;
import de.vzg.reposis.digibib.contactrequest.service.ContactRequestService;
import de.vzg.reposis.digibib.contactrequest.service.ContactRequestServiceImpl;

/**
 * Implements a {@link MCRJobAction} that sends specified email for {@link ContactRequest}.
 */
public class EmailJobAction extends MCRJobAction {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String CONTACT_REQUEST_ID = "contact_request_id";

    private static final String METHOD = "email_method";

    private static final ContactRequestService CONTACT_REQUEST_SERVICE = ContactRequestServiceImpl.getInstance();

    /**
     * Constructs new {@link ContactInfoCollectorJobAction} from {@link MCRJob}.
     *
     * @param job job
     */
    public EmailJobAction(MCRJob job) {
        super(job);
    }

    /**
     * Constructs and returns new {@link ContactInfoCollectorJobAction}
     * to send request confirmation.
     *
     * @param contactRequestId contact request id
     * @return job
     */
    public static MCRJob createRequestConfirmationJob(UUID contactRequestId) {
        final MCRJob job = new MCRJob(EmailJobAction.class);
        job.setParameter(METHOD, Method.REQUEST_CONFIRMATION.toString());
        job.setParameter(CONTACT_REQUEST_ID, contactRequestId.toString());
        return job;
    }

    /**
     * Constructs and returns new {@link ContactInfoCollectorJobAction}
     * to sends to staff.
     *
     * @param contactRequestId contact request id
     * @return job
     */
    public static MCRJob createNewRequestInfoJob(UUID contactRequestId) {
        final MCRJob job = new MCRJob(EmailJobAction.class);
        job.setParameter(METHOD, Method.NEW_REQUEST_INFO.toString());
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
        final Method method = getMethod();
        try {
            switch (method) {
                case REQUEST_CONFIRMATION -> handleRequestConfirmation();
                case NEW_REQUEST_INFO -> handleNewRequestInfo();
            }
        } catch (Exception e) {
            LOGGER.error("Job action failed: ", e);
            throw new ExecutionException(e);
        }
    }

    private void handleRequestConfirmation() {
        final ContactRequestDto contactRequestDto = CONTACT_REQUEST_SERVICE.getContactRequestById(getContactRequestId());
        EmailServiceImpl.getInstance().sendRequestConfirmation(contactRequestDto);
    }

    private void handleNewRequestInfo() {
        final ContactRequestDto contactRequestDto = CONTACT_REQUEST_SERVICE.getContactRequestById(getContactRequestId());
        EmailServiceImpl.getInstance().sendNewRequestInfo(contactRequestDto);
    }

    private UUID getContactRequestId() {
        return UUID.fromString(job.getParameter(CONTACT_REQUEST_ID));
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

    }

}
