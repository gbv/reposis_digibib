package de.vzg.reposis.digibib.email.model;

/**
 * Represents an attachment for an email, including its filename,
 * MIME type, and raw data.
 * <p>
 * This class is used to encapsulate the contents and metadata of an
 * attachment that can be added to an email.
 */
public class EmailAttachment {

    private String filename;

    private String mimeType;

    private byte[] data;

    /**
     * Constructs a new {@code EmailAttachment}.
     *
     * @param filename the name of the file
     * @param mimeType the MIME type of the attachment
     * @param data the raw data of the attachment as a byte array
     */
    public EmailAttachment(String filename, String mimeType, byte[] data) {
        this.filename = filename;
        this.mimeType = mimeType;
        this.data = data;
    }

    /**
     * Returns the filename of this attachment.
     *
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the filename for this attachment.
     *
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Returns the MIME type of this attachment.
     *
     * @return the MIME type
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets the MIME type for this attachment.
     *
     * @param mimeType the MIME type to set
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Returns the raw data of this attachment.
     *
     * @return the attachment data as a byte array
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Sets the raw data for this attachment.
     *
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

}
