package de.vzg.reposis.digibib.agreement.model;

import java.time.LocalDate;
import java.util.Objects;

import de.vzg.reposis.digibib.agreement.xml.util.LocalDateAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Represents the content details of an {@link Agreement}.
 * <p>
 * An {@code AgreementContent} contains metadata such as the author, title,
 * license information, DOI, additional comments, and an optional embargo date.
 * <p>
 * This class supports XML serialization/deserialization through Jakarta XML Bind
 * annotations. The {@link LocalDate} embargo field is adapted for XML processing
 * via {@link de.vzg.reposis.digibib.agreement.xml.util.LocalDateAdapter}.
 */
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

    /**
     * Creates an empty {@code AgreementContent} instance.
     * Required for JAXB deserialization.
     */
    public AgreementContent() {
    }

    /**
     * Creates a new {@code AgreementContent} instance from the provided builder.
     *
     * @param builder the builder containing agreement content data
     */
    private AgreementContent(Builder builder) {
        this.title = builder.title;
        this.license = builder.license;
        this.doi = builder.doi;
        this.comment = builder.comment;
        this.embargo = builder.embargo;
        this.author = builder.author;
    }

    /**
     * Returns the author associated with the agreement.
     *
     * @return the author information
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * Sets the author associated with the agreement.
     *
     * @param author the author information
     */
    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * Returns the title of the agreement's subject (e.g., a dataset or publication).
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the agreement's subject.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the license identifier for the content.
     *
     * @return the license identifier
     */
    public String getLicense() {
        return license;
    }

    /**
     * Sets the license identifier for the content.
     *
     * @param license the license identifier
     */
    public void setLicense(String license) {
        this.license = license;
    }

    /**
     * Returns the DOI (Digital Object Identifier) of the content.
     *
     * @return the DOI string
     */
    public String getDoi() {
        return doi;
    }

    /**
     * Sets the DOI of the content.
     *
     * @param doi the DOI string
     */
    public void setDoi(String doi) {
        this.doi = doi;
    }

    /**
     * Returns additional comments related to the agreement.
     *
     * @return the comment text
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets additional comments related to the agreement.
     *
     * @param comment the comment text
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns the embargo date, if applicable.
     *
     * @return the embargo date or {@code null} if none
     */
    public LocalDate getEmbargo() {
        return embargo;
    }

    /**
     * Sets the embargo date.
     *
     * @param embargo the embargo date
     */
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
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AgreementContent other = (AgreementContent) obj;
        return Objects.equals(author, other.author) &&
            Objects.equals(comment, other.comment) &&
            Objects.equals(doi, other.doi) &&
            Objects.equals(embargo, other.embargo) &&
            Objects.equals(license, other.license) &&
            Objects.equals(title, other.title);
    }

    /**
     * Builder for creating immutable {@link AgreementContent} instances.
     */
    public static class Builder {

        private Author author;
        private String title;
        private String license;
        private String doi;
        private String comment;
        private LocalDate embargo;

        /**
         * Creates a new empty {@code Builder} instance.
         */
        public Builder() {
        }

        /**
         * Sets the author for the agreement content.
         *
         * @param author the author information
         * @return this builder instance
         */
        public Builder author(Author author) {
            this.author = author;
            return this;
        }

        /**
         * Sets the title of the agreement's subject.
         *
         * @param title the title
         * @return this builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the license identifier.
         *
         * @param license the license identifier
         * @return this builder instance
         */
        public Builder license(String license) {
            this.license = license;
            return this;
        }

        /**
         * Sets the DOI.
         *
         * @param doi the DOI string
         * @return this builder instance
         */
        public Builder doi(String doi) {
            this.doi = doi;
            return this;
        }

        /**
         * Sets additional comments.
         *
         * @param comment the comment text
         * @return this builder instance
         */
        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * Sets the embargo date.
         *
         * @param embargo the embargo date
         * @return this builder instance
         */
        public Builder embargo(LocalDate embargo) {
            this.embargo = embargo;
            return this;
        }

        /**
         * Builds a new {@link AgreementContent} instance using the configured values.
         *
         * @return a new {@code AgreementContent}
         */
        public AgreementContent build() {
            return new AgreementContent(this);
        }
    }
}
