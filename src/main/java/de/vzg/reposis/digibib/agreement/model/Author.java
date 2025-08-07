package de.vzg.reposis.digibib.agreement.model;

import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

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

    public Author() {
    }

    private Author(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.phone = builder.phone;
        this.institute = builder.institute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInstitute() {
        return institute;
    }

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

    public static class Builder {
        private String name;
        private String email;
        private String phone;
        private String institute;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder institute(String institute) {
            this.institute = institute;
            return this;
        }

        public Author build() {
            return new Author(this);
        }
    }

}
