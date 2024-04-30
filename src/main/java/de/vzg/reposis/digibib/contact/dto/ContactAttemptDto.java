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

import com.fasterxml.jackson.annotation.JsonProperty;

import de.vzg.reposis.digibib.contact.persistence.model.ContactAttempt;
import jakarta.validation.constraints.NotNull;

/**
 * This class represents a DTO for {@link ContactAttempt}.
 */
public class ContactAttemptDto {

    private UUID id;

    private Date sendDate;

    private Date errorDate;

    private Date successDate;

    private UUID contactInfoId;

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
     * Returns associated the contact info id.
     *
     * @return the contact info id
     */
    @NotNull
    @JsonProperty("contactInfoId")
    public UUID getContactInfoId() {
        return contactInfoId;
    }

    /**
     * Sets the associated contact info id.
     *
     * @param contactInfoId the contact info id
     */
    public void setContactInfoId(UUID contactInfoId) {
        this.contactInfoId = contactInfoId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(successDate, contactInfoId, errorDate, id, sendDate);
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
        return Objects.equals(successDate, other.successDate)
            && Objects.equals(contactInfoId, other.contactInfoId) && Objects.equals(errorDate, other.errorDate)
            && Objects.equals(id, other.id) && Objects.equals(sendDate, other.sendDate);
    }

}
