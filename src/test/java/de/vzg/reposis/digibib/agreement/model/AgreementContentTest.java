package de.vzg.reposis.digibib.agreement.model;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

public class AgreementContentTest {

    @Test
    public void testBuilder_setsAllFieldsCorrectly() {
        final Author author = new Author();
        final String title = "Test Title";
        final String license = "CC-BY";
        final String doi = "10.1234/abc";
        final String comment = "This is a comment.";
        final LocalDate embargo = LocalDate.now();

        final AgreementContent agreement = new AgreementContent.Builder()
            .author(author)
            .title(title)
            .license(license)
            .doi(doi)
            .comment(comment)
            .embargo(embargo)
            .build();

        assertEquals(author, agreement.getAuthor());
        assertEquals(title, agreement.getTitle());
        assertEquals(license, agreement.getLicense());
        assertEquals(doi, agreement.getDoi());
        assertEquals(comment, agreement.getComment());
        assertEquals(embargo, agreement.getEmbargo());
    }
}
