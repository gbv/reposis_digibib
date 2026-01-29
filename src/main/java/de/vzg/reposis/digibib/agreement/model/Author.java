package de.vzg.reposis.digibib.agreement.model;

import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Represents an author.
 * <p>
 * An {@code Author} contains personal and institutional information such as
 * name, email, phone number, and affiliated institute. The class supports
 * XML serialization via Jakarta XML Bind annotations.
 */
@XmlRootElement(name = "author")
@XmlAccessorType(XmlAccessType.FIELD)
public class Author {

    @XmlElement
    private String name;

    @XmlElement
    private String email;

    @XmlElement
    private String phone;

    @XmlElement
    private String institute;

    /**
     * Creates an empty {@code Author} instance.
     * Required for JAXB deserialization.
     */
    public Author() {
    }

    private Author(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.phone = builder.phone;
        this.institute = builder.institute;
    }

    /**
     * Returns the author's name.
     *
     * @return the full name of the author
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the author's name.
     *
     * @param name the full name of the author
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the author's email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the author's email address.
     *
     * @param email the email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the author's phone number.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the author's  the author's phone number.
     *
     * @param phone the phone name
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the author's institute or organization.
     *
     * @return the institute name
     */
    public String getInstitute() {
        return institute;
    }

    /**
     * Sets the author's institute or organization.
     *
     * @param institute the institute name
     */
    public void setInstitute(String institute) {
        this.institute = institute;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, institute, name, phone);
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
        Author other = (Author) obj;
        return Objects.equals(email, other.email) && Objects.equals(institute, other.institute)
            && Objects.equals(name, other.name) && Objects.equals(phone, other.phone);
    }

    /**
     * Builder for creating immutable {@link Author} instances.
     */
    public static class Builder {
        private String name;
        private String email;
        private String phone;
        private String institute;

        /**
         * Sets the author's full name.
         *
         * @param name the full name
         * @return this builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the author's email address.
         *
         * @param email the email address
         * @return this builder instance
         */
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the author's phone number.
         *
         * @param phone the phone number
         * @return this builder instance
         */
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        /**
         * Sets the author's institute or organization.
         *
         * @param institute the institute name
         * @return this builder instance
         */
        public Builder institute(String institute) {
            this.institute = institute;
            return this;
        }

        /**
         * Builds a new {@link Author} instance using the configured values.
         *
         * @return a new {@code Author}
         */
        public Author build() {
            return new Author(this);
        }
    }

}
