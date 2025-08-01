package de.vzg.reposis.digibib.agreement.model;

public class Author {

    private String name;
    private String email;
    private String phone;
    private String institute;

    private Author(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.phone = builder.phone;
        this.institute = builder.institute;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getInstitute() {
        return institute;
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
