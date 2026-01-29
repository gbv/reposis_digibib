package de.vzg.reposis.digibib.agreement.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AuthorTest {

    @Test
    public void testBuilder_setsAllFieldsCorrectly() {
        final String name = "Max Mustermann";
        final String email = "max@example.com";
        final String phone = "0123456789";
        final String institute = "Test Institute";

        final Author author = new Author.Builder()
            .name(name)
            .email(email)
            .phone(phone)
            .institute(institute)
            .build();

        assertEquals(name, author.getName());
        assertEquals(email, author.getEmail());
        assertEquals(phone, author.getPhone());
        assertEquals(institute, author.getInstitute());
    }

}
