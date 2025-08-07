package de.vzg.reposis.digibib.agreement.model;

import java.time.LocalDate;
import java.util.Objects;

import de.vzg.reposis.digibib.agreement.xml.util.LocalDateAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "content")
@XmlAccessorType(XmlAccessType.FIELD)
public class AgreementContent {

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
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate embargo;

    public AgreementContent() {
    }

    private AgreementContent(Builder builder) {
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

    public LocalDate getEmbargo() {
        return embargo;
    }

    public void setEmbargo(LocalDate embargo) {
        this.embargo = embargo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, comment, doi, embargo, license, title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AgreementContent other = (AgreementContent) obj;
        return Objects.equals(author, other.author) && Objects.equals(comment, other.comment)
            && Objects.equals(doi, other.doi) && Objects.equals(embargo, other.embargo)
            && Objects.equals(license, other.license) && Objects.equals(title, other.title);
    }

    public static class Builder {

        private Author author;
        private String title;
        private String license;
        private String doi;
        private String comment;
        private LocalDate embargo;

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

        public Builder embargo(LocalDate embargo) {
            this.embargo = embargo;
            return this;
        }

        public AgreementContent build() {
            return new AgreementContent(this);
        }
    }

}
