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

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.services.queuedjob.MCRJob;
import org.mycore.services.queuedjob.MCRJobAction;

import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptPartialUpdateDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.dto.util.Nullable;
import de.vzg.reposis.digibib.contactrequest.service.ContactAttemptService;
import de.vzg.reposis.digibib.contactrequest.service.ContactRequestFactory;
import de.vzg.reposis.digibib.email.exception.EmailException;

/**
 * Represents an action for handling email-related jobs in the context of {@link MCRJob}.
 * <p>
 * This class extends {@link MCRJobAction} and provides static methods for creating different types of
 * email-related jobs, such as sending request confirmations, providing new request information to staff,
 * and forwarding requests.
 * </p>
 */
public class EmailJobAction extends MCRJobAction {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String CONTACT_REQUEST_ID = "contact_request_id";

    private static final String CONTACT_ATTEMPT_ID = "contact_attempt_id";

    private static final String METHOD = "email_method";

    /**
     * Constructs an {@link EmailJobAction} from the provided {@link MCRJob}.
     *
     * @param job the job instance used to initialize this action
     */
    public EmailJobAction(MCRJob job) {
        super(job);
    }

    /**
     * Creates a new {@link MCRJob} configured to send a request confirmation email.
     *
     * @param contactRequestId the id of the contact request for which confirmation is to be sent
     * @return a new job instance configured to send the request confirmation
     */
    public static MCRJob createSendRequestConfirmationJob(UUID contactRequestId) {
        final MCRJob job = new MCRJob(EmailJobAction.class);
        job.setParameter(METHOD, Method.SEND_REQUEST_CONFIRMATION.toString());
        job.setParameter(CONTACT_REQUEST_ID, contactRequestId.toString());
        return job;
    }

    /**
     * Creates a new {@link MCRJob} configured to send new request information to staff.
     *
     * @param contactRequestId the id of the contact request for which new information is to be sent
     * @return a new job instance configured to send the new request information to staff
     */
    public static MCRJob createSendNewRequestInfoJob(UUID contactRequestId) {
        final MCRJob job = new MCRJob(EmailJobAction.class);
        job.setParameter(METHOD, Method.SEND_NEW_REQUEST_INFO.toString());
        job.setParameter(CONTACT_REQUEST_ID, contactRequestId.toString());
        return job;
    }

    /**
     * Creates a new {@link MCRJob} configured to forward an email related to a contact request.
     *
     * @param contactRequestId the id of the contact request to which the email is related
     * @param contactAttemptId the id of the contact attempt for which the email is being forwarded
     * @return a new job instance configured to forward the email
     */
    public static MCRJob createForwardRequestJob(UUID contactRequestId, UUID contactAttemptId) {
        final MCRJob job = new MCRJob(EmailJobAction.class);
        job.setParameter(METHOD, Method.FORWARD_REQUEST.toString());
        job.setParameter(CONTACT_REQUEST_ID, contactRequestId.toString());
        job.setParameter(CONTACT_ATTEMPT_ID, contactAttemptId.toString());
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
                case SEND_REQUEST_CONFIRMATION -> handleSendRequestConfirmation();
                case SEND_NEW_REQUEST_INFO -> handleSendNewRequestInfo();
                case FORWARD_REQUEST -> handleForwardRequest();
            }
        } catch (Exception e) {
            LOGGER.error("Job action failed: ", e);
            throw new ExecutionException(e);
        }
    }

    private void handleSendRequestConfirmation() {
        final ContactRequestDto contactRequestDto
            = ContactRequestFactory.getContactRequestService().getContactRequestById(getContactRequestId());
        ContactRequestFactory.getContactEmailService().sendRequestConfirmation(contactRequestDto);
    }

    private void handleSendNewRequestInfo() {
        final ContactRequestDto contactRequestDto
            = ContactRequestFactory.getContactRequestService().getContactRequestById(getContactRequestId());
        ContactRequestFactory.getContactEmailService().sendNewRequestInfo(contactRequestDto);
    }

    private void handleForwardRequest() {
        final UUID contactAttemptId = getContactAttemptId();
        final ContactAttemptService contactAttemptService = ContactRequestFactory.getContactAttemptService();
        final ContactAttemptDto contactAttemptDto = contactAttemptService.getContactAttemptById(contactAttemptId);
        final UUID contactRequestId = getContactRequestId();
        final ContactRequestDto contactRequestDto = ContactRequestFactory.getContactRequestService()
            .getContactRequestById(contactRequestId);
        final ContactAttemptPartialUpdateDto contactAttemptUpdateDto = new ContactAttemptPartialUpdateDto();
        try {
            ContactRequestFactory.getContactEmailService().sendRequestForwarding(contactRequestDto, contactAttemptDto);
            contactAttemptUpdateDto.setSendDate(new Nullable<Date>(new Date()));
        } catch (EmailException e) {
            contactAttemptUpdateDto.setErrorDate(new Nullable<Date>(new Date()));
        } finally {
            contactAttemptService.partialUpdateContactAttempt(contactAttemptId, contactAttemptUpdateDto);
        }
    }

    private UUID getContactRequestId() {
        return UUID.fromString(job.getParameter(CONTACT_REQUEST_ID));
    }

    private UUID getContactAttemptId() {
        return UUID.fromString(job.getParameter(CONTACT_ATTEMPT_ID));
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
    private static enum Method {

        SEND_REQUEST_CONFIRMATION,

        SEND_NEW_REQUEST_INFO,

        FORWARD_REQUEST

    }

}
