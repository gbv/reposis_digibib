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

package de.vzg.reposis.digibib.contact.dto;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.mycore.datamodel.metadata.MCRObjectID;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.vzg.reposis.digibib.contact.persistence.model.ContactTicket;
import jakarta.validation.constraints.NotNull;

/**
 * This class represents a DTO for {@link ContactTicket}.
 */
public class ContactTicketDto {

    private UUID id;

    private MCRObjectID objectId;

    private String comment;

    private ContactRequestDto contactRequest;

    private ContactTicket.Status status;

    private Date created;

    private String createdBy;

    /**
     * Gets the unique identifier for the contact ticket.
     *
     * @return the unique identifier for the contact ticket
     */
    @JsonProperty("id")
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the contact ticket.
     *
     * @param id the unique identifier for the contact ticket
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the object Id.
     *
     * @return the object Id
     */
    @NotNull
    @JsonProperty("objectId")
    public MCRObjectID getObjectId() {
        return objectId;
    }

    /**
     * Sets the object Id.
     *
     * @param objectId the object Id
     */
    public void setObjectId(MCRObjectID objectId) {
        this.objectId = objectId;
    }

    /**
     * Gets the comment associated with the contact ticket.
     *
     * @return the comment associated with the contact ticket
     */
    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment associated with the contact ticket.
     *
     * @param comment the comment associated with the contact ticket
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the contact request associated with this ticket.
     *
     * @return the contact request associated with this ticket
     */
    @NotNull
    @JsonProperty("request")
    public ContactRequestDto getContactRequest() {
        return contactRequest;
    }

    /**
     * Sets the contact request associated with this ticket.
     *
     * @param contactRequest the contact request associated with this ticket
     */
    public void setContactRequest(ContactRequestDto contactRequest) {
        this.contactRequest = contactRequest;
    }

    /**
     * Gets the status of the contact ticket.
     *
     * @return the status of the contact ticket
     */
    @JsonProperty("status")
    public ContactTicket.Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the contact ticket.
     *
     * @param status the status of the contact ticket
     */
    public void setStatus(ContactTicket.Status status) {
        this.status = status;
    }

    /**
     * Gets the creation date of the ticket.
     *
     * @return the creation date of the ticket
     */
    @JsonProperty("created")
    public Date getCreated() {
        return created;
    }

    /**
     * Sets the creation date of the ticket.
     *
     * @param created the creation date of the ticket
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Gets the user who created the ticket.
     *
     * @return the user who created the ticket
     */
    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created the ticket.
     *
     * @param createdBy the user who created the ticket
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, created, createdBy, id, objectId, contactRequest, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ContactTicketDto other = (ContactTicketDto) obj;
        return Objects.equals(comment, other.comment) && Objects.equals(created, other.created)
            && Objects.equals(createdBy, other.createdBy) && Objects.equals(id, other.id)
            && Objects.equals(objectId, other.objectId) && Objects.equals(contactRequest, other.contactRequest)
            && status == other.status;
    }
}
