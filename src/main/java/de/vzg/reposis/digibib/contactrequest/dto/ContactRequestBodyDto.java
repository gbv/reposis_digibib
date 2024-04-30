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

import com.fasterxml.jackson.annotation.JsonProperty;

import de.vzg.reposis.digibib.contactrequest.model.ContactRequestBody;
import de.vzg.reposis.digibib.contactrequest.validation.annotation.ValidOrcid;
import jakarta.validation.constraints.NotNull;

/**
 * This class represents a DTO for {@link ContactRequestBody}.
 */
public class ContactRequestBodyDto {

    private String name;

    private String email;

    private String message;

    private String orcid;

    /**
     * Gets the name of the person making the contact request.
     *
     * @return the name of the person
     */
    @NotNull
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the person making the contact request.
     *
     * @param name the name of the person
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the person making the contact request.
     *
     * @return the email address of the person
     */
    @NotNull
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the person making the contact request.
     *
     * @param email the email address of the person
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the message from the person making the contact request.
     *
     * @return the message from the person
     */
    @NotNull
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message from the person making the contact request.
     *
     * @param message the message from the person
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the ORCID of the person making the contact request.
     *
     * @return the ORCID of the person
     */
    @ValidOrcid
    @JsonProperty("orcid")
    public String getOrcid() {
        return orcid;
    }

    /**
     * Sets the ORCID of the person making the contact request.
     *
     * @param orcid the ORCID of the person
     */
    public void setOrcid(String orcid) {
        this.orcid = orcid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, message, name, orcid);
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
        ContactRequestBodyDto other = (ContactRequestBodyDto) obj;
        return Objects.equals(email, other.email) && Objects.equals(message, other.message)
            && Objects.equals(name, other.name) && Objects.equals(orcid, other.orcid);
    }
}
