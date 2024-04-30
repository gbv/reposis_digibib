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

package de.vzg.reposis.digibib.contactrequest.event;

import org.mycore.common.config.MCRConfiguration2;
import org.mycore.common.events.MCREvent;
import org.mycore.services.queuedjob.MCRJobQueueManager;

import de.vzg.reposis.digibib.contactrequest.ContactRequestConstants;
import de.vzg.reposis.digibib.contactrequest.collect.ContactInfoCollectorJobAction;
import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.email.EmailJobAction;

/**
 * Default implementation of the {@link ContactRequestEventHandlerBase} for handling contact request and attempt events.
 * <p>
 * This class handles specific event types by enqueueing jobs related to contact requests and attempts. It uses
 * configuration parameters to determine whether to send emails, collect contact information, or perform other actions.
 * </p>
 */
public class ContactRequestDefaultEventHandler extends ContactRequestEventHandlerBase {

    private static final boolean SEND_REQUEST_CONFIRMATION_EMAIL
        = MCRConfiguration2.getBoolean(ContactRequestConstants.CONF_PREFIX + "SendRequestConfirmationEmail")
            .orElseThrow();

    private static final boolean SEND_NEW_REQUEST_INFO_ENAIL
        = MCRConfiguration2.getBoolean(ContactRequestConstants.CONF_PREFIX + "SendNewRequestInfoEmailToStaff")
            .orElseThrow();

    private static final boolean COLLECT_CONTACT_INFOS
        = MCRConfiguration2.getBoolean(ContactRequestConstants.CONF_PREFIX + "CollectContactInfos").orElseThrow();

    @Override
    protected void handleContactRequestCreated(MCREvent evt, ContactRequestDto contactRequestDto) {
        if (SEND_REQUEST_CONFIRMATION_EMAIL) {
            MCRJobQueueManager.getInstance().getJobQueue(EmailJobAction.class)
                .add(EmailJobAction.createSendRequestConfirmationJob(contactRequestDto.getId()));
        }
        if (SEND_NEW_REQUEST_INFO_ENAIL) {
            MCRJobQueueManager.getInstance().getJobQueue(EmailJobAction.class)
                .add(EmailJobAction.createSendNewRequestInfoJob(contactRequestDto.getId()));
        }
        if (COLLECT_CONTACT_INFOS) {
            MCRJobQueueManager.getInstance().getJobQueue(ContactInfoCollectorJobAction.class)
                .add(ContactInfoCollectorJobAction.createJob(contactRequestDto.getId()));
        }
    }

    @Override
    protected void handleContactAttemptCreated(MCREvent evt, ContactRequestDto contactRequestDto,
        ContactAttemptDto contactAttemptDto) {
        MCRJobQueueManager.getInstance().getJobQueue(EmailJobAction.class)
            .add(EmailJobAction.createForwardRequestJob(contactRequestDto.getId(), contactAttemptDto.getId()));
    }
}
