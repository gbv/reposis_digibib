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

import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.vzg.reposis.digibib.contactrequest.model.ContactInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * This class represents a DTO for {@link ContactInfo}.
 */
public class ContactInfoDto {

    private UUID id;

    private String name;

    private String email;

    private String origin;

    private String reference;

    /**
     * Gets the unique identifier for the contact information.
     *
     * @return the unique identifier for the contact information
     */
    @JsonProperty("id")
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the contact information.
     *
     * @param id the unique identifier for the contact information
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the name associated with the contact information.
     *
     * @return the name associated with the contact information
     */
    @NotNull
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name associated with the contact information.
     *
     * @param name the name associated with the contact information
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address associated with the contact information.
     *
     * @return the email address associated with the contact information
     */
    @NotNull
    @Email
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address associated with the contact information.
     *
     * @param email the email address associated with the contact information
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the origin of the contact information.
     *
     * @return the origin of the contact information
     */
    @NotNull
    @JsonProperty("origin")
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets the origin of the contact information.
     *
     * @param origin the origin of the contact information
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * Gets the reference information associated with the contact.
     *
     * @return the reference information associated with the contact
     */
    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    /**
     * Sets the reference information associated with the contact.
     *
     * @param reference the reference information associated with the contact
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, id, name, origin, reference);
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
        ContactInfoDto other = (ContactInfoDto) obj;
        return Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(name, other.name)
            && Objects.equals(origin, other.origin) && Objects.equals(reference, other.reference);
    }

}
