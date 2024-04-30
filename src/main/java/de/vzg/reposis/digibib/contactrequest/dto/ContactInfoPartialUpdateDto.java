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

import com.fasterxml.jackson.annotation.JsonProperty;

import de.vzg.reposis.digibib.contactrequest.dto.util.Nullable;
import de.vzg.reposis.digibib.contactrequest.model.ContactInfo;
import de.vzg.reposis.digibib.contactrequest.validation.annotation.IfPresentNotNull;

/**
 * DTO for partially updating {@link ContactInfo}.
 * This class uses {@link Nullable} wrappers for the date fields to distinguish between the absence of a value
 * and the presence of a null value.
 */
public class ContactInfoPartialUpdateDto {

    private Nullable<String> name = new Nullable<>();

    private Nullable<String> email = new Nullable<>();

    private Nullable<String> origin = new Nullable<>();

    private Nullable<String> reference = new Nullable<>();

    /**
     * Returns the name.
     * The value is wrapped in a {@link Nullable} to indicate whether the name is present or absent.
     *
     * @return the name wrapped in a Nullable
     */
    @IfPresentNotNull
    @JsonProperty("name")
    public Nullable<String> getName() {
        return name;
    }

    /**
     * Sets the name.
     * The value should be wrapped in a {@link Nullable} to indicate whether the name is present or absent.
     *
     * @param name the name wrapped in a Nullable
     */
    public void setName(Nullable<String> name) {
        this.name = name;
    }

    /**
     * Returns the email.
     * The value is wrapped in a {@link Nullable} to indicate whether the email is present or absent.
     *
     * @return the email wrapped in a Nullable
     */
    @IfPresentNotNull
    @JsonProperty("email")
    public Nullable<String> getEmail() {
        return email;
    }

    /**
     * Sets the email.
     * The value should be wrapped in a {@link Nullable} to indicate whether the email is present or absent.
     *
     * @param email the email wrapped in a Nullable
     */
    public void setEmail(Nullable<String> email) {
        this.email = email;
    }

    /**
     * Returns the origin.
     * The value is wrapped in a {@link Nullable} to indicate whether the origin is present or absent.
     *
     * @return the origin wrapped in a Nullable
     */
    @IfPresentNotNull
    @JsonProperty("origin")
    public Nullable<String> getOrigin() {
        return origin;
    }

    /**
     * Sets the origin.
     * The value should be wrapped in a {@link Nullable} to indicate whether the origin is present or absent.
     *
     * @param origin the origin wrapped in a Nullable
     */
    public void setOrigin(Nullable<String> origin) {
        this.origin = origin;
    }

    /**
     * Returns the reference.
     * The value is wrapped in a {@link Nullable} to indicate whether the reference is present or absent.
     *
     * @return the reference wrapped in a Nullable
     */
    @JsonProperty("reference")
    public Nullable<String> getReference() {
        return reference;
    }

    /**
     * Sets the reference.
     * The value should be wrapped in a {@link Nullable} to indicate whether the reference is present or absent.
     *
     * @param reference the reference wrapped in a Nullable
     */
    public void setReference(Nullable<String> reference) {
        this.reference = reference;
    }

}
