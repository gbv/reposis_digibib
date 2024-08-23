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

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Represents a contact request body containing name, email, message, and optional ORCiD.
 */
@Embeddable
public class ContactRequestBody {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "orcid", nullable = true)
    private String orcid;

    /**
     * Constructs a new ContactRequest with default values.
     */
    public ContactRequestBody() {
        // Default constructor
    }

    /**
     * Constructs a new ContactRequest with the specified name, email, message, and ORCiD.
     *
     * @param name the name of the contact
     * @param email the email address of the contact
     * @param message the message from the contact
     * @param orcid the ORCiD identifier of the contact
     */
    public ContactRequestBody(String name, String email, String message, String orcid) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.orcid = orcid;
    }

    /**
     * Returns the email address of the contact.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the contact.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the message from the contact.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message from the contact.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
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
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the ORCiD identifier of the contact.
     *
     * @return the ORCiD identifier
     */
    public String getOrcid() {
        return orcid;
    }

    /**
     * Sets the ORCiD identifier of the contact.
     *
     * @param orcid the ORCiD identifier to set
     */
    public void setOrcid(String orcid) {
        this.orcid = orcid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, message, orcid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ContactRequestBody other = (ContactRequestBody) obj;
        return Objects.equals(name, other.name) && Objects.equals(email, other.email)
            && Objects.equals(message, other.message) && Objects.equals(orcid, other.orcid);
    }
}
