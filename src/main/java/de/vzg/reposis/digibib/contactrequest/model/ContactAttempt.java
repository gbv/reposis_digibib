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

package de.vzg.reposis.digibib.contactrequest.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * This entity stores information about an email contact attempt.
 * Contains recipient information and status data..
 */
@Entity
@Table(name = "contact_attempt")
public class ContactAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "contact_attempt_id", nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AttemptType type;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "recipient_reference", nullable = false)
    private String recipientReference;

    @Column(name = "send_date", nullable = true)
    private LocalDateTime sendDate;

    @Column(name = "error_date", nullable = true)
    private LocalDateTime errorDate;

    @Column(name = "success_date", nullable = true)
    private LocalDateTime successDate;

    @Column(name = "comment", nullable = true)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_request_id")
    private ContactRequest contactRequest;

    /**
     * Gets the unique identifier for the contact attempt.
     *
     * @return the unique identifier
     */
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
     * Returns the type.
     *
     * @return the type
     */
    public AttemptType getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the type
     */
    public void setType(AttemptType type) {
        this.type = type;
    }

    /**
     * Returns the recipient name.
     *
     * @return the recipient name
     */
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
    public LocalDateTime getSendDate() {
        return sendDate;
    }

    /**
     * Sets the date when the contact attempt was sent.
     *
     * @param sendDate the send date
     */
    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    /**
     * Gets the date when an error occurred during the contact attempt.
     *
     * @return the error date
     */
    public LocalDateTime getErrorDate() {
        return errorDate;
    }

    /**
     * Sets the date when an error occurred during the contact attempt.
     *
     * @param errorDate the error date
     */
    public void setErrorDate(LocalDateTime errorDate) {
        this.errorDate = errorDate;
    }

    /**
     * Gets the date when the contact attempt was confirmed.
     *
     * @return the confirmation date
     */
    public LocalDateTime getSuccessDate() {
        return successDate;
    }

    /**
     * Sets the date when the contact attempt was confirmed.
     *
     * @param successDate the success date
     */
    public void setSuccessDate(LocalDateTime successDate) {
        this.successDate = successDate;
    }

    /**
     * Returns the comment.
     *
     * @return the comment
     */
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

    /**
     * Gets the contact request associated with this contact attempt.
     *
     * @return the contact information
     */
    public ContactRequest getContactRequest() {
        return contactRequest;
    }

    /**
     * Sets the contact request associated with this contact attempt.
     *
     * @param contactRequest the contact request
     */
    public void setContactRequest(ContactRequest contactRequest) {
        this.contactRequest = contactRequest;
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, contactRequest, errorDate, id, recipientReference, recipientName, sendDate,
            successDate);
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
        ContactAttempt other = (ContactAttempt) obj;
        return Objects.equals(comment, other.comment) && Objects.equals(contactRequest, other.contactRequest)
            && Objects.equals(errorDate, other.errorDate) && Objects.equals(id, other.id)
            && Objects.equals(recipientReference, other.recipientReference)
            && Objects.equals(recipientName, other.recipientName) && Objects.equals(sendDate, other.sendDate)
            && Objects.equals(successDate, other.successDate);
    }

    /**
     * Defines the attempt type.
     */
    public static enum AttemptType {

        /**
         * Email type.
         */
        EMAIL,
    }

}
