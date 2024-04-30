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

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.common.events.MCREvent;
import org.mycore.common.events.MCREventHandler;

import de.vzg.reposis.digibib.contactrequest.dto.ContactAttemptDto;
import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;

/**
 * Abstract base class for handling events related to contact requests and attempts.
 * <p>
 * This class implements the {@link MCREventHandler} interface and provides default implementations for
 * handling and undoing events related to {@link ContactRequestDto} and {@link ContactAttemptDto}.
 * Subclasses can override the methods to provide specific handling and undo logic for these events.
 * </p>
 */
public abstract class ContactRequestEventHandlerBase implements MCREventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Event type for contact requests.
     */
    public static final String CONTACT_REQUEST_TYPE = "contactrequest";

    /**
     * Event type for contact attempts.
     */
    public static final String CONTACT_ATTEMPT_TYPE = "contactattempt";

    @Override
    public void doHandleEvent(MCREvent evt) {
        if (Objects.equals(CONTACT_REQUEST_TYPE, evt.getCustomObjectType())) {
            final ContactRequestDto contactRequest = (ContactRequestDto) evt.get(CONTACT_REQUEST_TYPE);
            if (contactRequest != null) {
                if (evt.getEventType().equals(MCREvent.EventType.CREATE)) {
                    handleContactRequestCreated(evt, contactRequest);
                } else {
                    LOGGER.warn("Can't find method for an slot data handler for event type {}", evt.getEventType());
                }
                return;
            }
            LOGGER.warn("Can't find method for {} for event type {}", CONTACT_REQUEST_TYPE, evt.getEventType());
        } else if (Objects.equals(CONTACT_ATTEMPT_TYPE, evt.getCustomObjectType())) {
            final ContactAttemptDto contactAttempt = (ContactAttemptDto) evt.get(CONTACT_ATTEMPT_TYPE);
            if (contactAttempt != null) {
                if (evt.getEventType().equals(MCREvent.EventType.CREATE)) {
                    final ContactRequestDto contactRequest = (ContactRequestDto) evt.get(CONTACT_REQUEST_TYPE);
                    handleContactAttemptCreated(evt, contactRequest, contactAttempt);
                }
                return;
            }
            LOGGER.warn("Can't find method for {} for event type {}", CONTACT_ATTEMPT_TYPE, evt.getEventType());
        }
    }

    @Override
    public void undoHandleEvent(MCREvent evt) {
        if (Objects.equals(CONTACT_REQUEST_TYPE, evt.getCustomObjectType())) {
            final ContactRequestDto contactRequestDto = (ContactRequestDto) evt.get(CONTACT_REQUEST_TYPE);
            if (contactRequestDto != null) {
                if (evt.getEventType().equals(MCREvent.EventType.CREATE)) {
                    undoContactRequestCreated(evt, contactRequestDto);
                } else {
                    LOGGER.warn("Can't find method for an slot data handler for event type {}", evt.getEventType());
                }
                return;
            }
            LOGGER.warn("Can't find method for {} for event type {}", CONTACT_REQUEST_TYPE, evt.getEventType());
        } else if (Objects.equals(CONTACT_ATTEMPT_TYPE, evt.getCustomObjectType())) {
            final ContactAttemptDto contactAttempt = (ContactAttemptDto) evt.get(CONTACT_ATTEMPT_TYPE);
            if (contactAttempt != null) {
                if (evt.getEventType().equals(MCREvent.EventType.CREATE)) {
                    final ContactRequestDto contactRequest = (ContactRequestDto) evt.get(CONTACT_REQUEST_TYPE);
                    undoContactAttemptCreated(evt, contactRequest, contactAttempt);
                } else {
                    LOGGER.warn("Can't find method for an slot data handler for event type {}", evt.getEventType());
                }
                return;
            }
            LOGGER.warn("Can't find method for {} for event type {}", CONTACT_REQUEST_TYPE, evt.getEventType());
        }

    }

    private void doNothing(MCREvent evt, Object obj) {
        LOGGER.info("{} does nothing on {} {} {}", getClass().getName(), evt.getEventType(), evt.getObjectType(),
            obj.toString());
    }

    /**
     * Handles the creation of a contact request.
     *
     * @param evt the event
     * @param contactRequestDto the DTO containing the contact request details
     */
    protected void handleContactRequestCreated(MCREvent evt, ContactRequestDto contactRequestDto) {
        doNothing(evt, contactRequestDto);
    }

    /**
     * Undoes the creation of a contact request.
     *
     * @param evt the event
     * @param contactRequestDto the DTO containing the contact request details
     */
    protected void undoContactRequestCreated(MCREvent evt, ContactRequestDto contactRequestDto) {
        doNothing(evt, contactRequestDto);
    }

    /**
     * Handles the creation of a contact attempt.
     *
     * @param evt the event
     * @param contactRequestDto the DTO containing the contact request details
     * @param contactAttemptDto the DTO containing the contact attempt details
     */
    protected void handleContactAttemptCreated(MCREvent evt, ContactRequestDto contactRequestDto,
        ContactAttemptDto contactAttemptDto) {
        doNothing(evt, contactAttemptDto);
    }

    /**
     * Undoes the creation of a contact attempt.
     *
     * @param evt the event
     * @param contactRequestDto the DTO containing the contact request details
     * @param contactAttemptDto the DTO object containing the contact attempt details
     */
    protected void undoContactAttemptCreated(MCREvent evt, ContactRequestDto contactRequestDto,
        ContactAttemptDto contactAttemptDto) {
        doNothing(evt, contactAttemptDto);
    }

}
