package de.vzg.reposis.digibib.email.model;

import java.util.Collections;
import java.util.List;

/**
 * Represents an email send request containing the email message
 * and optional attachments.
 * <p>
 * This class is an immutable data structure used to encapsulate all information required
 * to send an email, including its message and any associated attachments.
 */
public class EmailSendRequest {

    private final EmailMessage message;

    private final List<EmailAttachment> attachments;

    /**
     * Constructs an {@code EmailSendRequest} with only an email message and no attachments.
     *
     * @param message the email message to be sent
     */
    public EmailSendRequest(EmailMessage message) {
        this.message = message;
        this.attachments = Collections.emptyList();
    }

    /**
     * Constructs an {@code EmailSendRequest} with an email message and attachments.
     *
     * @param message the email message to be sent
     * @param attachments a list of attachments for this message
     */
    public EmailSendRequest(EmailMessage message, List<EmailAttachment> attachments) {
        this.message = message;
        this.attachments = attachments;
    }

    /**
     * Returns the email message associated with this send request.
     *
     * @return the email message
     */
    public EmailMessage getMessage() {
        return message;
    }

    /**
     * Returns the list of attachments associated with this send request.
     *
     * @return the attachments, or an empty list if none were added
     */
    public List<EmailAttachment> getAttachments() {
        return attachments;
    }
}
