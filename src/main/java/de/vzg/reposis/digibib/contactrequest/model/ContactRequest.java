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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.mycore.backend.jpa.MCRObjectIDConverter;
import org.mycore.datamodel.metadata.MCRObjectID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Represents a contact request with associated contact information and request details.
 */
@NamedQueries({
    @NamedQuery(name = "ContactRequest.findAll", query = "SELECT r"
        + "  FROM ContactRequest r"
        + "  ORDER BY r.created DESC"),
    @NamedQuery(name = "ContactRequest.findByObjectId", query = "SELECT r"
        + "  FROM ContactRequest r"
        + "  WHERE r.objectId = :objectId"
        + "  ORDER BY r.created DESC"),
    @NamedQuery(name = "ContactRequest.findByStatus", query = "SELECT r"
        + "  FROM ContactRequest r"
        + "  WHERE r.status = :status"
        + "  ORDER BY r.created DESC"),
})
@Entity
@Table(name = "contact_request")
public class ContactRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "contact_request_id", nullable = false)
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
    private ContactRequest.Status status;

    @Column(name = "comment", nullable = true)
    private String comment;

    @Embedded
    private ContactRequestBody body;

    @OneToMany(mappedBy = "contactRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ContactInfo> contactInfos = new ArrayList<>();

    @OneToMany(mappedBy = "contactRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ContactAttempt> emailContactAttempts = new ArrayList<>();

    /**
     * Returns the unique identifier of the contact request.
     *
     * @return the unique identifier
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the contact request.
     *
     * @param id the unique identifier to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Returns the object identifier associated with the contact request.
     *
     * @return the object identifier
     */
    public MCRObjectID getObjectId() {
        return objectId;
    }

    /**
     * Sets the object identifier associated with the contact request.
     *
     * @param objectId the object identifier to set
     */
    public void setObjectId(MCRObjectID objectId) {
        this.objectId = objectId;
    }

    /**
     * Returns the creation date of the contact request.
     *
     * @return the creation date
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * Sets the creation date of the contact request.
     *
     * @param created the creation date to set
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * Returns the creator of the contact request.
     *
     * @return the creator
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the creator of the contact request.
     *
     * @param createdBy the creator to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Returns the status of the contact request.
     *
     * @return the status
     */
    public ContactRequest.Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the contact request.
     *
     * @param status the status to set
     */
    public void setStatus(ContactRequest.Status status) {
        this.status = status;
    }

    /**
     * Returns the comment associated with the contact request.
     *
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment associated with the contact request.
     *
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns the contact request body associated with the contact request.
     *
     * @return the contact request body
     */
    public ContactRequestBody getBody() {
        return body;
    }

    /**
     * Sets the contact request body associated with the contact request.
     *
     * @param body the contact request body to set
     */
    public void setBody(ContactRequestBody body) {
        this.body = body;
    }

    /**
     * Returns the list of contact information entries associated with the contact request.
     *
     * @return the list of contact information entries
     */
    public List<ContactInfo> getContactInfos() {
        return contactInfos;
    }

    /**
     * Sets the list of contact information entries associated with the contact request.
     *
     * @param contactInfos the list of contact information entries to set
     */
    public void setContactInfos(List<ContactInfo> contactInfos) {
        this.contactInfos = contactInfos;
    }

    /**
     * Returns the list of email contact attempt entries associated with the contact request.
     *
     * @return the list of contact attempt entries
     */
    public List<ContactAttempt> getEmailContactAttempts() {
        return emailContactAttempts;
    }

    /**
     * Sets the list of email contact attempt entries associated with the contact request.
     *
     * @param contactAttempts the list of contact attempt entries to set
     */
    public void setEmailContactAttempts(List<ContactAttempt> contactAttempts) {
        emailContactAttempts = contactAttempts;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, objectId, created, createdBy, status, comment, body);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ContactRequest other = (ContactRequest) obj;
        return Objects.equals(id, other.id) &&
            Objects.equals(objectId, other.objectId) &&
            Objects.equals(created, other.created) &&
            Objects.equals(createdBy, other.createdBy) &&
            status == other.status &&
            Objects.equals(comment, other.comment) &&
            Objects.equals(body, other.body);
    }

    /**
     * Enum representing the possible statuses of a contact request.
     */
    public enum Status {

        /**
         * Indicates that the contact request is open.
         */
        OPEN(0),

        /**
         * Indicates that the contact request is closed.
         */
        CLOSED(10);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        /**
         * Resolves and returns the request status by its numeric value.
         *
         * @param value the numeric value of the status
         * @return the corresponding {@link Status}
         * @throws IllegalArgumentException if unknown status value
         */
        public static Status resolve(int value) {
            return Arrays.stream(values()).filter(status -> status.value == value).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(Integer.toString(value)));
        }

        /**
         * Returns the numeric value of the status.
         *
         * @return the numeric value
         */
        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return name();
        }
    }
}
