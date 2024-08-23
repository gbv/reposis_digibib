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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.vzg.reposis.digibib.contactrequest.dto.util.Nullable;
import de.vzg.reposis.digibib.contactrequest.model.ContactAttempt;

/**
 * DTO for partially updating {@link ContactAttempt}.
 * This class uses {@link Nullable} wrappers for the date fields to distinguish between the absence of a value
 * and the presence of a null value.
 */
public class ContactAttemptPartialUpdateDto {

    private Nullable<Date> sendDate = new Nullable<>();

    private Nullable<Date> errorDate = new Nullable<>();

    private Nullable<Date> successDate = new Nullable<>();

    /**
     * Returns the send date.
     * The value is wrapped in a {@link Nullable} to indicate whether the date is present or absent.
     *
     * @return the send date wrapped in a Nullable
     */
    @JsonProperty("sendDate")
    public Nullable<Date> getSendDate() {
        return sendDate;
    }

    /**
     * Sets the send date
     * The value should be wrapped in a {@link Nullable} to indicate whether the date is present or absent.
     *
     * @param sendDate the send date wrapped in a Nullable
     */
    public void setSendDate(Nullable<Date> sendDate) {
        this.sendDate = sendDate;
    }

    /**
     * Returns the error date.
     * The value is wrapped in a {@link Nullable} to indicate whether the date is present or absent.
     *
     * @return the error date wrapped in a Nullable
     */
    @JsonProperty("errorDate")
    public Nullable<Date> getErrorDate() {
        return errorDate;
    }

    /**
     * Sets the error date.
     * The value should be wrapped in a {@link Nullable} to indicate whether the date is present or absent.
     *
     * @param errorDate the error date wrapped in a Nullable
     */
    public void setErrorDate(Nullable<Date> errorDate) {
        this.errorDate = errorDate;
    }

    /**
     * Returns the success date.
     * The value is wrapped in a {@link Nullable} to indicate whether the date is present or absent.
     *
     * @return the success date wrapped in a Nullable
     */
    @JsonProperty("successDate")
    public Nullable<Date> getSuccessDate() {
        return successDate;
    }

    /**
     * Sets the success date.
     * The value should be wrapped in a {@link Nullable} to indicate whether the date is present or absent.
     *
     * @param successDate the success date wrapped in a Nullable
     */
    public void setSuccessDate(Nullable<Date> successDate) {
        this.successDate = successDate;
    }

}
