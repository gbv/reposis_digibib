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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.mycore.backend.jpa.MCRObjectIDConverter;
import org.mycore.datamodel.metadata.MCRObjectID;

import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Represents a contact ticket with associated contact information and request details.
 */
@NamedQueries({
    @NamedQuery(name = "ContactTicket.findAll", query = "SELECT r"
        + "  FROM ContactTicket r"
        + "  ORDER BY r.created DESC"),
    @NamedQuery(name = "ContactTicket.findByObjectId", query = "SELECT r"
        + "  FROM ContactTicket r"
        + "  WHERE r.objectId = :objectId"
        + "  ORDER BY r.created DESC"),
    @NamedQuery(name = "ContactTicket.findByStatus", query = "SELECT r"
        + "  FROM ContactTicket r"
        + "  WHERE r.status = :status"
        + "  ORDER BY r.created DESC"),
})
@Entity
@Table(name = "contact_ticket")
public class ContactTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_id", nullable = false)
    private UUID id;

    @Column(name = "object_id", length = MCRObjectID.MAX_LENGTH, nullable = false)
    @Convert(converter = MCRObjectIDConverter.class)
    private MCRObjectID objectId;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContactTicket.Status status;

    @Column(name = "comment", nullable = true)
    private String comment;

    @Embedded
    private ContactRequest contactRequest;

    @OneToMany(mappedBy = "contactTicket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactInfo> contactInfos = new ArrayList<>();

    @OneToMany(mappedBy = "contactTicket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactAttempt> contactAttempts = new ArrayList<>();

    /**
     * Returns the unique identifier of the contact ticket.
     *
     * @return the unique identifier
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the contact ticket.
     *
     * @param id the unique identifier to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Returns the object identifier associated with the contact ticket.
     *
     * @return the object identifier
     */
    public MCRObjectID getObjectId() {
        return objectId;
    }

    /**
     * Sets the object identifier associated with the contact ticket.
     *
     * @param objectId the object identifier to set
     */
    public void setObjectId(MCRObjectID objectId) {
        this.objectId = objectId;
    }

    /**
     * Returns the creation date of the contact ticket.
     *
     * @return the creation date
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * Sets the creation date of the contact ticket.
     *
     * @param created the creation date to set
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * Returns the creator of the contact ticket.
     *
     * @return the creator
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the creator of the contact ticket.
     *
     * @param createdBy the creator to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Returns the status of the contact ticket.
     *
     * @return the status
     */
    public ContactTicket.Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the contact ticket.
     *
     * @param status the status to set
     */
    public void setStatus(ContactTicket.Status status) {
        this.status = status;
    }

    /**
     * Returns the comment associated with the contact ticket.
     *
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment associated with the contact ticket.
     *
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns the contact request details associated with the contact ticket.
     *
     * @return the contact request details
     */
    public ContactRequest getContactRequest() {
        return contactRequest;
    }

    /**
     * Sets the contact request details associated with the contact ticket.
     *
     * @param contactRequest the contact request details to set
     */
    public void setContactRequest(ContactRequest contactRequest) {
        this.contactRequest = contactRequest;
    }

    /**
     * Returns the list of contact information entries associated with the contact ticket.
     *
     * @return the list of contact information entries
     */
    public List<ContactInfo> getContactInfos() {
        return contactInfos;
    }

    /**
     * Sets the list of contact information entries associated with the contact ticket.
     *
     * @param contactInfos the list of contact information entries to set
     */
    public void setContactInfos(List<ContactInfo> contactInfos) {
        this.contactInfos = contactInfos;
    }

    /**
     * Returns the list of contact attempt entries associated with the contact ticket.
     *
     * @return the list of contact attempt entries
     */
    public List<ContactAttempt> getContactAttempts() {
        return contactAttempts;
    }

    /**
     * Sets the list of contact attempt entries associated with the contact ticket.
     *
     * @param contactAttempts the list of contact attempt entries to set
     */
    public void setContactAttempts(List<ContactAttempt> contactAttempts) {
        this.contactAttempts = contactAttempts;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, objectId, created, createdBy, status, comment, contactRequest);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ContactTicket other = (ContactTicket) obj;
        return Objects.equals(id, other.id) &&
            Objects.equals(objectId, other.objectId) &&
            Objects.equals(created, other.created) &&
            Objects.equals(createdBy, other.createdBy) &&
            status == other.status &&
            Objects.equals(comment, other.comment) &&
            Objects.equals(contactRequest, other.contactRequest);
    }

    /**
     * Enum representing the possible statuses of a contact ticket.
     */
    public enum Status {

        /**
         * Indicates that the contact ticket is open.
         */
        OPEN(0),

        /**
         * Indicates that the contact ticket is closed.
         */
        CLOSED(10);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        /**
         * Resolves and returns the ticket status by its numeric value.
         *
         * @param value the numeric value of the status
         * @return the corresponding {@link Status}, or {@code null} if no matching status is found
         */
        public static Status resolve(int value) {
            return Arrays.stream(values()).filter(status -> status.value == value).findFirst().orElse(null);
        }

        /**
         * Returns the numeric value of the status.
         *
         * @return the numeric value
         */
        @JsonValue
        public int getValue() {
            return value;
        }
    }
}
