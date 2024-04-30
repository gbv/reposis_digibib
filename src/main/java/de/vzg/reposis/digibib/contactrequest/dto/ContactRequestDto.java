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

package de.vzg.reposis.digibib.contactrequest.dto;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.mycore.datamodel.metadata.MCRObjectID;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.vzg.reposis.digibib.contactrequest.model.ContactRequest;
import jakarta.validation.constraints.NotNull;

/**
 * This class represents a DTO for {@link ContactRequest}.
 */
public class ContactRequestDto {

    private UUID id;

    private MCRObjectID objectId;

    private String comment;

    private ContactRequestBodyDto body;

    private String status;

    private Date created;

    private String createdBy;

    /**
     * Gets the unique identifier for the contact request.
     *
     * @return the unique identifier for the contact request
     */
    @JsonProperty("id")
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the contact request.
     *
     * @param id the unique identifier for the contact request
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
     * Gets the comment associated with the contact request.
     *
     * @return the comment associated with the contact request
     */
    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment associated with the contact request.
     *
     * @param comment the comment associated with the contact request
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the body associated with this contact request.
     *
     * @return the body associated with this contact request
     */
    @NotNull
    @JsonProperty("body")
    public ContactRequestBodyDto getBody() {
        return body;
    }

    /**
     * Sets the body associated with this contact request.
     *
     * @param body the body associated with this contact request
     */
    public void setBody(ContactRequestBodyDto body) {
        this.body = body;
    }

    /**
     * Gets the status of the contact request.
     *
     * @return the status of the contact request
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the contact request.
     *
     * @param status the status of the contact request
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the creation date of the request.
     *
     * @return the creation date of the request
     */
    @JsonProperty("created")
    public Date getCreated() {
        return created;
    }

    /**
     * Sets the creation date of the request.
     *
     * @param created the creation date of the request
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Gets the user who created the request.
     *
     * @return the user who created the request
     */
    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created the request.
     *
     * @param createdBy the user who created the request
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, body, created, createdBy, id, objectId, status);
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
        ContactRequestDto other = (ContactRequestDto) obj;
        return Objects.equals(comment, other.comment) && Objects.equals(body, other.body)
            && Objects.equals(created, other.created) && Objects.equals(createdBy, other.createdBy)
            && Objects.equals(id, other.id) && Objects.equals(objectId, other.objectId)
            && Objects.equals(status, other.status);
    }

}
