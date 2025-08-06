package de.vzg.reposis.digibib.agreement.model;

import java.util.Date;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name = "agreement")
@XmlAccessorType(XmlAccessType.FIELD)
public class Agreement {

    @XmlElement
    private Author author;

    @XmlElement
    private String title;

    @XmlElement
    private String license;

    @XmlElement
    private String doi;

    @XmlElement
    private String comment;

    @XmlElement
    @XmlSchemaType(name = "date")
    private Date embargo;

    public Agreement() {
    }

    private Agreement(Builder builder) {
        this.title = builder.title;
        this.license = builder.license;
        this.doi = builder.doi;
        this.comment = builder.comment;
        this.embargo = builder.embargo;
        this.author = builder.author;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getEmbargo() {
        return embargo;
    }

    public void setEmbargo(Date embargo) {
        this.embargo = embargo;
    }

    public static class Builder {

        private Author author;
        private String title;
        private String license;
        private String doi;
        private String comment;
        private Date embargo;

        public Builder() {
        }

        public Builder author(Author author) {
            this.author = author;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder license(String license) {
            this.license = license;
            return this;
        }

        public Builder doi(String doi) {
            this.doi = doi;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder embargo(Date embargo) {
            this.embargo = embargo;
            return this;
        }

        public Agreement build() {
            return new Agreement(this);
        }
    }
}
