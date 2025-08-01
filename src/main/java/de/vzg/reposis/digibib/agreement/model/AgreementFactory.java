package de.vzg.reposis.digibib.agreement.model;

import org.mycore.datamodel.metadata.MCRObject;

public class AgreementFactory {

    private AgreementFactory() {
    }

    public static Agreement fromObject(MCRObject obj) {
        final Author author = new Author.Builder()
            .name(extractAuthorName(obj))
            .email(extractAuthorEmail(obj))
            .phone(extractAuthorPhone(obj))
            .institute(extractAuthorInstitute(obj))
            .build();

        return new Agreement.Builder()
            .license(extractLicense(obj))
            .author(author)
            .build();
    }

    private static String extractLicense(MCRObject object) {
        return "license";
    }

    private static String extractAuthorName(MCRObject object) {
        return "authorName";
    }

    private static String extractAuthorEmail(MCRObject object) {
        return "authorEmail";
    }

    private static String extractAuthorPhone(MCRObject object) {
        return "authorPhone";
    }

    private static String extractAuthorInstitute(MCRObject object) {
        return "authorInstitute";
    }
}
