package de.vzg.reposis.digibib.agreement.model;

import java.time.LocalDateTime;

/**
 * Data model representing metadata about the transmission of an agreement.
 */
public class TransmissionInfo {

    private String agreementId;

    private LocalDateTime transmissionDate;

    /**
     * No-argument constructor.
     * <p>
     * Required for frameworks and libraries that use reflection for object creation,
     * such as JSON deserialization tools.
     */
    public TransmissionInfo() {
    }

    /**
     * Constructs a new {@code AgreementTransmissionInfo} instance.
     *
     * @param agreementId the id of the transmitted agreement
     * @param transmissionDate the date and time when the transmission occurred
     */
    public TransmissionInfo(String agreementId, LocalDateTime transmissionDate) {
        this.agreementId = agreementId;
        this.transmissionDate = transmissionDate;
    }

    /**
     * Returns the name of the transmitted agreement.
     *
     * @return the agreement id
     */
    public String getAgreementId() {
        return agreementId;
    }

    /**
     * Sets the name of the transmitted agreement.
     *
     * @param agreementId the agreement id to set
     */
    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    /**
     * Returns the date and time when the agreement was transmitted.
     *
     * @return the transmission date and time
     */
    public LocalDateTime getTransmissionDate() {
        return transmissionDate;
    }

    /**
     * Sets the date and time when the agreement was transmitted.
     *
     * @param transmissionDate the date and time to set
     */
    public void setTransmissionDate(LocalDateTime transmissionDate) {
        this.transmissionDate = transmissionDate;
    }

    @Override
    public String toString() {
        return "AgreementTransmissionInfo{" +
            "agreementId='" + agreementId + '\'' +
            ", transmissionDate=" + transmissionDate +
            '}';
    }
}
