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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Represents the contact information associated with a contact ticket.
 */
@Entity
@Table(name = "contact_info")
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "contact_info_id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "type", nullable = false)
    private String origin;

    @Column(name = "reference", nullable = true)
    private String reference;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_ticket_id")
    private ContactTicket contactTicket;

    @OneToMany(mappedBy = "contactInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactAttempt> contactAttempts = new ArrayList<>();

    /**
     * Default constructor.
     */
    public ContactInfo() {
        // Default constructor
    }

    /**
     * Constructs a new ContactInfo with the specified name, email, origin, and reference.
     *
     * @param name the name of the contact
     * @param email the email of the contact
     * @param origin the origin type of the contact
     * @param reference an optional reference for the contact
     */
    public ContactInfo(String name, String email, String origin, String reference) {
        this.name = name;
        this.email = email;
        this.origin = origin;
        this.reference = reference;
    }

    /**
     * Returns the unique identifier of the contact information.
     *
     * @return the unique identifier
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the contact information.
     *
     * @param id the unique identifier
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Returns the name of the contact.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the contact.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the email of the contact.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the contact.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the origin type of the contact.
     *
     * @return the origin type
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets the origin type of the contact.
     *
     * @param origin the origin type
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * Returns the reference of the contact.
     *
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the reference of the contact.
     *
     * @param reference the reference
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Returns the associated contact ticket.
     *
     * @return the contact ticket
     */
    public ContactTicket getContactTicket() {
        return contactTicket;
    }

    /**
     * Sets the associated contact ticket.
     *
     * @param contactTicket the contact ticket
     */
    public void setContactTicket(ContactTicket contactTicket) {
        this.contactTicket = contactTicket;
    }

    /**
     * Returns the list of contact attempts associated with the contact information.
     *
     * @return the list of contact attempts
     */
    public List<ContactAttempt> getContactAttempts() {
        return contactAttempts;
    }

    /**
     * Sets the list of contact attempts associated with the contact information.
     *
     * @param contactAttempts the list of contact attempts
     */
    public void setContactAttempts(List<ContactAttempt> contactAttempts) {
        this.contactAttempts = contactAttempts;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, origin, reference, contactTicket);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ContactInfo other = (ContactInfo) obj;
        return Objects.equals(id, other.id) &&
            Objects.equals(name, other.name) &&
            Objects.equals(email, other.email) &&
            Objects.equals(origin, other.origin) &&
            Objects.equals(reference, other.reference) &&
            Objects.equals(contactTicket, other.contactTicket);
    }
}
