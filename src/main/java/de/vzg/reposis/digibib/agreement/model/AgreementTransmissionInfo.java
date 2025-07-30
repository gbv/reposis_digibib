package de.vzg.reposis.digibib.agreement.model;

import java.time.LocalDateTime;

/**
 * Data model representing metadata about the transmission of an agreement.
 */
public class AgreementTransmissionInfo {

    private String agreementName;

    private LocalDateTime transmissionDate;

    /**
     * No-argument constructor.
     * <p>
     * Required for frameworks and libraries that use reflection for object creation,
     * such as JSON deserialization tools.
     */
    public AgreementTransmissionInfo() {
    }

    /**
     * Constructs a new {@code AgreementTransmissionInfo} instance.
     *
     * @param agreementName   the name of the transmitted agreement
     * @param transmissionDate the date and time when the transmission occurred
     */
    public AgreementTransmissionInfo(String agreementName, LocalDateTime transmissionDate) {
        this.agreementName = agreementName;
        this.transmissionDate = transmissionDate;
    }

    /**
     * Returns the name of the transmitted agreement.
     *
     * @return the agreement name
     */
    public String getAgreementName() {
        return agreementName;
    }

    /**
     * Sets the name of the transmitted agreement.
     *
     * @param agreementName the agreement name to set
     */
    public void setAgreementName(String agreementName) {
        this.agreementName = agreementName;
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

    /**
     * Returns a string representation of this transmission info,
     * containing the agreement name and the transmission date.
     *
     * @return a human-readable representation of this object
     */
    @Override
    public String toString() {
        return "AgreementTransmissionInfo{" +
            "agreementName='" + agreementName + '\'' +
            ", transmissionDate=" + transmissionDate +
            '}';
    }
}
