package de.vzg.reposis.digibib.agreement.model;

/**
 * Represents an agreement, consisting of a name and its associated content.
 * <p>
 * An {@code Agreement} acts as the container for the descriptive name
 * (e.g., the type or identifier of the agreement) and the detailed
 * {@link AgreementContent} holding all relevant metadata and author
 * information.
 */
public class Agreement {

    private String agreementName;
    private AgreementContent content;

    /**
     * Creates an empty {@code Agreement} instance.
     */
    public Agreement() {
    }

    /**
     * Creates a new {@code Agreement} with the specified name and content.
     *
     * @param agreementName the name of the agreement (e.g., template or type)
     * @param content the content of the agreement
     */
    public Agreement(String agreementName, AgreementContent content) {
        this.agreementName = agreementName;
        this.content = content;
    }

    /**
     * Returns the agreement's name.
     *
     * @return the agreement name
     */
    public String getAgreementName() {
        return agreementName;
    }

    /**
     * Sets the agreement's name.
     *
     * @param agreementName the new name for the agreement
     */
    public void setAgreementName(String agreementName) {
        this.agreementName = agreementName;
    }

    /**
     * Returns the content of the agreement.
     *
     * @return the agreement content
     */
    public AgreementContent getContent() {
        return content;
    }

    /**
     * Sets the content of the agreement.
     *
     * @param content the new agreement content
     */
    public void setContent(AgreementContent content) {
        this.content = content;
    }
}
