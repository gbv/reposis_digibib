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

package de.vzg.reposis.digibib.email.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * DTOfor encapsulating email details for sending with the Java Mail API.
 */
@XmlRootElement(name = "email")
public class SimpleEmailDto {

    private String from;

    private List<String> to = new ArrayList<>();

    private List<String> cc = new ArrayList<>();

    private List<String> bcc = new ArrayList<>();

    private String subject;

    private String body;

    private Date sentDate;

    private Map<String, String> headers = new HashMap<>();

    /**
     * Gets the sender's email address.
     *
     * @return the sender's email address
     */
    @XmlElement(name = "from")
    public String getFrom() {
        return from;
    }

    /**
     * Sets the sender's email address.
     *
     * @param from the sender's email address
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Gets the list of recipient email addresses.
     *
     * @return the list of recipient email addresses
     */
    @XmlElement(name = "to")
    public List<String> getTo() {
        return to;
    }

    /**
     * Sets the list of recipient email addresses.
     *
     * @param to the list of recipient email addresses
     */
    public void setTo(List<String> to) {
        this.to = to;
    }

    /**
     * Gets the list of CC (carbon copy) email addresses.
     *
     * @return the list of CC email addresses
     */
    @XmlElement(name = "cc")
    public List<String> getCc() {
        return cc;
    }

    /**
     * Sets the list of CC (carbon copy) email addresses.
     *
     * @param cc the list of CC email addresses
     */
    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    /**
     * Gets the list of BCC (blind carbon copy) email addresses.
     *
     * @return the list of BCC email addresses
     */
    @XmlElement(name = "bcc")
    public List<String> getBcc() {
        return bcc;
    }

    /**
     * Sets the list of BCC (blind carbon copy) email addresses.
     *
     * @param bcc the list of BCC email addresses
     */
    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    /**
     * Gets the subject of the email.
     *
     * @return the subject of the email
     */
    @XmlElement(name = "subject")
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the email.
     *
     * @param subject the subject of the email
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets the body of the email.
     *
     * @return the body of the email
     */
    @XmlElement(name = "body")
    public String getBody() {
        return body;
    }

    /**
     * Sets the body of the email.
     *
     * @param body the body of the email
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Gets the date of the email.
     *
     * @return the date of the email
     */
    @XmlElement(name = "date")
    public Date getSentDate() {
        return sentDate;
    }

    /**
     * Sets the date of the email.
     *
     * @param date the date of the email
     */
    public void setSentDate(Date date) {
        this.sentDate = date;
    }

    /**
     * Gets the headers of the email.
     *
     * @return the headers of the email
     */
    @XmlElement(name = "headers")
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets the headers of the email.
     *
     * @param headers the headers of the email
     */
    public void setBody(Map<String, String> headers) {
        this.headers = headers;
    }
}
