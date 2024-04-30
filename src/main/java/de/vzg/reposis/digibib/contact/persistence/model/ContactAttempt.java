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

package de.vzg.reposis.digibib.contact.persistence.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a contact attempt.
 * This entity stores information about a contact attempt, including the dates of sending, error, and confirmation.
 */
@Entity
@Table(name = "contact_attempt")
public class ContactAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "contact_attempt_id", nullable = false)
    private UUID id;

    @Column(name = "send_date", nullable = true)
    private LocalDateTime sendDate;

    @Column(name = "error_date", nullable = true)
    private LocalDateTime errorDate;

    @Column(name = "success_date", nullable = true)
    private LocalDateTime successDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_ticket_id")
    private ContactTicket contactTicket;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_info_id")
    private ContactInfo contactInfo;

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
     * Gets the contact ticket associated with this contact attempt.
     *
     * @return the contact information
     */
    public ContactTicket getContactTicket() {
        return contactTicket;
    }

    /**
     * Sets the contact information associated with this contact attempt.
     *
     * @param contactTicket the contact information
     */
    public void setContactTicket(ContactTicket contactTicket) {
        this.contactTicket = contactTicket;
    }

    /**
     * Gets the contact information associated with this contact attempt.
     *
     * @return the contact information
     */
    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the contact information associated with this contact attempt.
     *
     * @param contactInfo the contact information
     */
    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(successDate, contactInfo, errorDate, id, sendDate);
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
        return Objects.equals(successDate, other.successDate)
            && Objects.equals(contactInfo, other.contactInfo) && Objects.equals(errorDate, other.errorDate)
            && Objects.equals(id, other.id) && Objects.equals(sendDate, other.sendDate);
    }
}
