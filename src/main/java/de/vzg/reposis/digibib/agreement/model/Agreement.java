package de.vzg.reposis.digibib.agreement.model;

public class Agreement {

    private String agreementName;
    private AgreementContent content;

    public Agreement() {
    }

    public Agreement(String agreementName, AgreementContent content) {
        this.agreementName = agreementName;
        this.content = content;
    }

    public String getAgreementName() {
        return agreementName;
    }

    public void setAgreementName(String agreementName) {
        this.agreementName = agreementName;
    }

    public AgreementContent getContent() {
        return content;
    }

    public void setContent(AgreementContent content) {
        this.content = content;
    }
}
