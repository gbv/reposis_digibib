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

import com.fasterxml.jackson.annotation.JsonProperty;

import de.vzg.reposis.digibib.contactrequest.model.ContactAttempt;

/**
 * This class represents a DTO for {@link ContactAttempt}.
 */
public class ContactAttemptDto {

    private UUID id;

    private String type;

    private String recipientName;

    private String recipientReference;

    private Date sendDate;

    private Date errorDate;

    private Date successDate;

    private String comment;

    /**
     * Gets the unique identifier for the contact attempt.
     *
     * @return the unique identifier
     */
    @JsonProperty("id")
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the contact attempt.
     *
     * @param id the unique identifier
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Returns the attempt type.
     *
     * @return the attempt type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the attempt type.
     *
     * @param type the attempt type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the recipient name.
     *
     * @return the recipient name
     */
    @JsonProperty("recipientName")
    public String getRecipientName() {
        return recipientName;
    }

    /**
     * Sets the recipient name.
     *
     * @param recipientName the recipient name
     */
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    /**
     * Returns the recipient reference.
     *
     * @return the recipient reference
     */
    @JsonProperty("recipientReference")
    public String getRecipientReference() {
        return recipientReference;
    }

    /**
     * Sets the recipient reference.
     *
     * @param recipientReference the recipient reference
     */
    public void setRecipientReference(String recipientReference) {
        this.recipientReference = recipientReference;
    }

    /**
     * Gets the date when the contact attempt was sent.
     *
     * @return the send date
     */
    @JsonProperty("sendDate")
    public Date getSendDate() {
        return sendDate;
    }

    /**
     * Sets the date when the contact attempt was sent.
     *
     * @param sendDate the send date
     */
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    /**
     * Gets the date when an error occurred during the contact attempt.
     *
     * @return the error date
     */
    @JsonProperty("errorDate")
    public Date getErrorDate() {
        return errorDate;
    }

    /**
     * Sets the date when an error occurred during the contact attempt.
     *
     * @param errorDate the error date
     */
    public void setErrorDate(Date errorDate) {
        this.errorDate = errorDate;
    }

    /**
     * Gets the date when the contact attempt was confirmed.
     *
     * @return the success date
     */
    @JsonProperty("successDate")
    public Date getSuccessDate() {
        return successDate;
    }

    /**
     * Sets the date when the contact attempt was confirmed.
     *
     * @param successDate the success date
     */
    public void setSuccessDate(Date successDate) {
        this.successDate = successDate;
    }

    /**
     * Returns the comment.
     *
     * @return the comment
     */
    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment.
     *
     * @param comment the comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, errorDate, id, recipientName, recipientReference, sendDate, successDate, type);
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
        ContactAttemptDto other = (ContactAttemptDto) obj;
        return Objects.equals(comment, other.comment) && Objects.equals(errorDate, other.errorDate)
            && Objects.equals(id, other.id) && Objects.equals(recipientName, other.recipientName)
            && Objects.equals(recipientReference, other.recipientReference) && Objects.equals(sendDate, other.sendDate)
            && Objects.equals(successDate, other.successDate) && Objects.equals(type, other.type);
    }

}
