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

import org.mycore.datamodel.metadata.MCRObjectID;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.vzg.reposis.digibib.contactrequest.dto.util.Nullable;
import de.vzg.reposis.digibib.contactrequest.model.ContactRequest;
import de.vzg.reposis.digibib.contactrequest.validation.annotation.IfPresentNotNull;

/**
 * DTO for partially updating {@link ContactRequest}.
 * This class uses {@link Nullable} wrappers for the date fields to distinguish between the absence of a value
 * and the presence of a null value.
 */
public class ContactRequestPartialUpdateDto {

    private Nullable<MCRObjectID> objectId = new Nullable<>();

    private Nullable<String> comment = new Nullable<>();

    private Nullable<ContactRequestBodyDto> body = new Nullable<>();

    private Nullable<ContactRequest.Status> status = new Nullable<>();

    /**
     * Returns the object ID.
     * The value is wrapped in a {@link Nullable} to indicate whether the object ID is present or absent.
     *
     * @return the object ID wrapped in a Nullable
     */
    @IfPresentNotNull
    @JsonProperty("objectId")
    public Nullable<MCRObjectID> getObjectId() {
        return objectId;
    }

    /**
     * Sets the object ID.
     * The value should be wrapped in a {@link Nullable} to indicate whether the object ID is present or absent.
     *
     * @param objectId the object ID wrapped in a Nullable
     */
    public void setObjectId(Nullable<MCRObjectID> objectId) {
        this.objectId = objectId;
    }

    /**
     * Returns the comment.
     * The value is wrapped in a {@link Nullable} to indicate whether the comment is present or absent.
     *
     * @return the comment wrapped in a Nullable
     */
    @JsonProperty("comment")
    public Nullable<String> getComment() {
        return comment;
    }

    /**
     * Sets the comment.
     * The value should be wrapped in a {@link Nullable} to indicate whether the comment is present or absent.
     *
     * @param comment the comment wrapped in a Nullable
     */
    public void setComment(Nullable<String> comment) {
        this.comment = comment;
    }

    /**
     * Returns the contact request body.
     * The value is wrapped in a {@link Nullable} to indicate whether the body is present or absent.
     *
     * @return the contact request body wrapped in a Nullable
     */
    @IfPresentNotNull
    @JsonProperty("body")
    public Nullable<ContactRequestBodyDto> getBody() {
        return body;
    }

    /**
     * Sets the contact request body.
     * The value should be wrapped in a {@link Nullable} to indicate whether the body is present or absent.
     *
     * @param body the contact request body wrapped in a Nullable
     */
    public void setBody(Nullable<ContactRequestBodyDto> body) {
        this.body = body;
    }

    /**
     * Returns the status.
     * The value is wrapped in a {@link Nullable} to indicate whether the status is present or absent.
     *
     * @return the status wrapped in a Nullable
     */
    @IfPresentNotNull
    @JsonProperty("status")
    public Nullable<ContactRequest.Status> getStatus() {
        return status;
    }

    /**
     * Sets the status.
     * The value should be wrapped in a {@link Nullable} to indicate whether the status is present or absent.
     *
     * @param status the status wrapped in a Nullable
     */
    public void setStatus(Nullable<ContactRequest.Status> status) {
        this.status = status;
    }

}
