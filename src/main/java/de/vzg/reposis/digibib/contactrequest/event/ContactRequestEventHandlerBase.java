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

import de.vzg.reposis.digibib.contactrequest.dto.ContactRequestDto;
import de.vzg.reposis.digibib.contactrequest.persistence.model.ContactRequest;

/**
 * Abstract helper class that can be subclassed to implement event handlers more easily.
 */
public abstract class ContactRequestEventHandlerBase implements MCREventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * The {@link ContactRequest} event type.
     */
    public static final String CONTACT_REQUEST_TYPE = "contactrequest";

    /**
     * This method handle all calls for EventHandler for the event type contact request.
     *
     * @param evt the MCREvent object
     *
     */
    @Override
    public void doHandleEvent(MCREvent evt) {
        if (Objects.equals(CONTACT_REQUEST_TYPE, evt.getCustomObjectType())) {
            final ContactRequestDto contactRequest = (ContactRequestDto) evt.get(CONTACT_REQUEST_TYPE);
            if (contactRequest != null) {
                if (evt.getEventType().equals(MCREvent.EventType.CREATE)) {
                    handleContactRequestCreated(evt, contactRequest);
                } else if (evt.getEventType().equals(MCREvent.EventType.UPDATE)) {
                    handleContactRequestUpdated(evt, contactRequest);
                } else if (evt.getEventType().equals(MCREvent.EventType.DELETE)) {
                    handleContactRequestDeleted(evt, contactRequest);
                } else {
                    LOGGER.warn("Can't find method for an slot data handler for event type {}", evt.getEventType());
                }
                return;
            }
            LOGGER.warn("Can't find method for {} for event type {}", CONTACT_REQUEST_TYPE, evt.getEventType());
        }
    }

    /**
     * This method roll back all calls for EventHandler for the event type contact request.
     *
     * @param evt the MCREvent object
     *
     */
    @Override
    public void undoHandleEvent(MCREvent evt) {
        if (Objects.equals(CONTACT_REQUEST_TYPE, evt.getCustomObjectType())) {
            final ContactRequestDto contactRequestDto = (ContactRequestDto) evt.get(CONTACT_REQUEST_TYPE);
            if (contactRequestDto != null) {
                if (evt.getEventType().equals(MCREvent.EventType.CREATE)) {
                    undoContactRequestCreated(evt, contactRequestDto);
                } else if (evt.getEventType().equals(MCREvent.EventType.UPDATE)) {
                    undoContactRequestUpdated(evt, contactRequestDto);
                } else if (evt.getEventType().equals(MCREvent.EventType.DELETE)) {
                    undoContactRequestDeleted(evt, contactRequestDto);
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
     * This method can be overridden by subclasses to provide specific handling logic.
     *
     * @param evt the event
     * @param contactRequestDto the DTO object containing the contact request details
     */
    protected void handleContactRequestCreated(MCREvent evt, ContactRequestDto contactRequestDto) {
        doNothing(evt, contactRequestDto);
    }

    /**
     * Handles the update of a contact request.
     * This method can be overridden by subclasses to provide specific handling logic.
     *
     * @param evt the event
     * @param contactRequestDto the DTO object containing the contact request details
     */
    protected void handleContactRequestUpdated(MCREvent evt, ContactRequestDto contactRequestDto) {
        doNothing(evt, contactRequestDto);
    }

    /**
     * Handles the deletion of a contact request.
     * This method can be overridden by subclasses to provide specific handling logic.
     *
     * @param evt the event
     * @param contactRequestDto the DTO object containing the contact request details
     */
    protected void handleContactRequestDeleted(MCREvent evt, ContactRequestDto contactRequestDto) {
        doNothing(evt, contactRequestDto);
    }

    /**
     * Undoes the creation of a contact request.
     * This method can be overridden by subclasses to provide specific undo logic.
     *
     * @param evt the event
     * @param contactRequestDto the DTO object containing the contact request details
     */
    protected void undoContactRequestCreated(MCREvent evt, ContactRequestDto contactRequestDto) {
        doNothing(evt, contactRequestDto);
    }

    /**
     * Undoes the update of a contact request.
     * This method can be overridden by subclasses to provide specific undo logic.
     *
     * @param evt the event
     * @param contactRequestDto the DTO object containing the contact request details
     */
    protected void undoContactRequestUpdated(MCREvent evt, ContactRequestDto contactRequestDto) {
        doNothing(evt, contactRequestDto);
    }

    /**
     * Undoes the deletion of a contact request.
     * This method can be overridden by subclasses to provide specific undo logic.
     *
     * @param evt the event
     * @param contactRequestDto the DTO object containing the contact request details
     */
    protected void undoContactRequestDeleted(MCREvent evt, ContactRequestDto contactRequestDto) {
        doNothing(evt, contactRequestDto);
    }

}
