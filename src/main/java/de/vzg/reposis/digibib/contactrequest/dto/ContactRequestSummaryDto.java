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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents a DTO for contact request summary.
 */
public class ContactRequestSummaryDto {

    private String statusString;

    private List<String> emails = new ArrayList<>();

    /**
     * Gets the status string.
     *
     * @return the status string
     */
    @JsonProperty("status")
    public String getStatusString() {
        return statusString;
    }

    /**
     * Sets the status string.
     *
     * @param statusString the status string
     */
    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    /**
     * Gets the emails.
     *
     * @return the emails
     */
    @JsonProperty("emails")
    public List<String> getEmails() {
        return emails;
    }

    /**
     * Sets the emails.
     *
     * @param emails the emails
     */
    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

}
