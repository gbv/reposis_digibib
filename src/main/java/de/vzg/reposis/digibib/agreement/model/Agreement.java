package de.vzg.reposis.digibib.agreement.model;

import java.util.Date;

public class Agreement {

    private String name;
    private Author author;
    private String title;
    private String license;
    private String doi;
    private String comment;
    private Date embargo;

    private Agreement(Builder builder) {
        this.name = builder.name;
        this.title = builder.title;
        this.license = builder.license;
        this.doi = builder.doi;
        this.comment = builder.comment;
        this.embargo = builder.embargo;
        this.author = builder.author;
    }

    public String name() {
        return name;
    }

    public Author author() {
        return author;
    }

    public String title() {
        return title;
    }

    public String license() {
        return license;
    }

    public String doi() {
        return doi;
    }

    public String comment() {
        return comment;
    }

    public Date embargo() {
        return embargo;
    }

    public static class Builder {

        private String name;
        private Author author;
        private String title;
        private String license;
        private String doi;
        private String comment;
        private Date embargo;

        public Builder() {
        }

        public Builder author(String name) {
            this.name = name;
            return this;
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
