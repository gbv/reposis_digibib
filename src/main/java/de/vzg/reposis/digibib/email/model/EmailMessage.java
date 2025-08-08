package de.vzg.reposis.digibib.email.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an immutable email message with sender, recipients,
 * subject, body, sent date, and custom headers.
 * <p>
 * Instances of this class are created via the {@link Builder} to
 * enforce immutability and ease of construction.
 */
public class EmailMessage {

    private final String from;

    private final List<String> toRecipients;

    private final List<String> ccRecipients;

    private final List<String> bccRecipients;

    private final String subject;

    private final String textBody;

    private final String htmlBody;

    private final Date sentDate;

    private final Map<String, String> headers;

    private EmailMessage(Builder builder) {
        this.from = builder.from;
        this.toRecipients = Collections.unmodifiableList(new ArrayList<>(builder.toRecipients));
        this.ccRecipients = Collections.unmodifiableList(new ArrayList<>(builder.ccRecipients));
        this.bccRecipients = Collections.unmodifiableList(new ArrayList<>(builder.bccRecipients));
        this.subject = builder.subject;
        this.textBody = builder.textBody;
        this.htmlBody = builder.htmlBody;
        this.sentDate = builder.sentDate;
        this.headers = Collections.unmodifiableMap(new HashMap<>(builder.headers));
    }

    /**
     * Gets the sender's email address.
     *
     * @return the sender's email address
     */
    public String getFrom() {
        return from;
    }

    /**
     * Gets the list of recipient email addresses.
     *
     * @return the list of recipient email addresses
     */
    public List<String> getToRecipients() {
        return toRecipients;
    }

    /**
     * Gets the list of CC (carbon copy) email addresses.
     *
     * @return the list of CC email addresses
     */
    public List<String> getCcRecipients() {
        return ccRecipients;
    }

    /**
     * Gets the list of BCC (blind carbon copy) email addresses.
     *
     * @return the list of BCC email addressesemailDto
     */
    public List<String> getBccRecipients() {
        return bccRecipients;
    }

    /**
     * Gets the subject of the email.
     *
     * @return the subject of the email
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Gets the text body of the email.
     *
     * @return the text body of the email
     */
    public String getTextBody() {
        return textBody;
    }

    /**
     * Gets the html body of the email.
     *
     * @return the html body of the email
     */
    public String getHtmlBody() {
        return htmlBody;
    }

    /**
     * Gets the date of the email.
     *
     * @return the date of the email
     */
    public Date getSentDate() {
        return sentDate;
    }

    /**
     * Gets the headers of the email.
     *
     * @return the headers of the email
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Builder class to create immutable {@link EmailMessage} instances.
     */
    public static class Builder {

        private String from;

        private final List<String> toRecipients = new ArrayList<>();

        private final List<String> ccRecipients = new ArrayList<>();

        private final List<String> bccRecipients = new ArrayList<>();

        private String subject;

        private String textBody;

        private String htmlBody;

        private Date sentDate;

        private final Map<String, String> headers = new HashMap<>();

        /**
         * Creates a new Builder with a sender and a single "To" recipient.
         *
         * @param toRecipient a single primary recipient's email address
         */
        public Builder(String toRecipient) {
            Objects.requireNonNull(toRecipient, "to must not be null");
            this.toRecipients.add(toRecipient);
        }

        /**
         * Creates a new Builder with a sender and multiple "To" recipients.
         *
         * @param toRecipients list of primary recipient email addresses
         */
        public Builder(List<String> toRecipients) {
            Objects.requireNonNull(toRecipients, "to must not be null");
            this.toRecipients.addAll(toRecipients);
        }

        /**
         * Sets the from of the email.
         *
         * @param from the email from
         * @return this builder instance
         */
        public Builder from(String from) {
            this.from = from;
            return this;
        }

        /**
         * Adds carbon copy (CC) recipients.
         *
         * @param ccRecipients list of CC recipient email addresses
         * @return this builder instance
         */
        public Builder ccRecipients(List<String> ccRecipients) {
            this.ccRecipients.addAll(ccRecipients);
            return this;
        }

        /**
         * Adds blind carbon copy (BCC) recipients.
         *
         * @param bccRecipients list of BCC recipient email addresses
         * @return this builder instance
         */
        public Builder bccRecipients(List<String> bccRecipients) {
            this.bccRecipients.addAll(bccRecipients);
            return this;
        }

        /**
         * Sets the subject of the email.
         *
         * @param subject the email subject
         * @return this builder instance
         */
        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Sets the text body content of the email.
         *
         * @param textBody the email body content
         * @return this builder instance
         */
        public Builder textBody(String textBody) {
            this.textBody = textBody;
            return this;
        }

        /**
         * Sets the html body content of the email.
         *
         * @param htmlBody the email body content
         * @return this builder instance
         */
        public Builder htmlBody(String htmlBody) {
            this.htmlBody = htmlBody;
            return this;
        }

        /**
         * Sets the sent date of the email.
         *
         * @param sentDate the date the email was sent
         * @return this builder instance
         */
        public Builder sentDate(Date sentDate) {
            this.sentDate = sentDate;
            return this;
        }

        /**
         * Adds a custom header key-value pair to the email.
         *
         * @param key the header name
         * @param value the header value
         * @return this builder instance
         */
        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        /**
         * Adds multiple custom headers to the email.
         *
         * @param headers map of header names and values
         * @return this builder instance
         */
        public Builder headers(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        /**
         * Builds the immutable {@link EmailMessage} instance.
         *
         * @return a new EmailMessage object
         */
        public EmailMessage build() {
            return new EmailMessage(this);
        }
    }
}
