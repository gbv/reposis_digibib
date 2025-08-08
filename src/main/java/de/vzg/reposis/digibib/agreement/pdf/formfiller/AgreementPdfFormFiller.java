package de.vzg.reposis.digibib.agreement.pdf.formfiller;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import de.vzg.reposis.digibib.agreement.model.AgreementContent;
import de.vzg.reposis.digibib.agreement.model.Author;

/**
 * Fills an interactive PDF form (AcroForm) with data from an {@link AgreementContent} instance.
 * <p>
 * This class is responsible for mapping the agreement content (such as title, comments,
 * author details, and license selection) to the appropriate form fields in the PDF template.
 * It also sets the default font and field appearance for consistent formatting.
 */
public class AgreementPdfFormFiller {

    private static final String FONT_NAME = "Helvetica";
    private static final String DEFAULT_FIELD_APPERANCE = "/" + FONT_NAME + " 10 Tf 0 g";

    private static final String TITLE_FIELD_NAME = "Titel Datensatz";
    private static final String COMMENT_FIELD_NAME = "Anmerkungen";
    // private static final String OTHER_LICENSE = "sonstige";

    private static final String AUTHOR_EMAIL_FIELD_NAME = "E-Mail";
    private static final String AUTHOR_PHONE_FIELD_NAME = "Telefon";
    private static final String AUTHOR_INSTITUTE_FIELD_NAME = "Institut";
    private static final String AUTHOR_NAME_FIELD_NAME = "Name Vorname";

    private static final Map<String, String> LICENSE_MAPPING;

    static {
        LICENSE_MAPPING = new HashMap<>();
        LICENSE_MAPPING.put("cc_by_4.0", "Auswahl1");
    }

    /**
     * Fills the given PDF AcroForm with the provided agreement content.
     *
     * @param acroForm the PDF form to populate
     * @param agreementContent the agreement content containing the data to fill in
     * @throws IOException if there is an error accessing or modifying the PDF fields
     * @throws IllegalArgumentException if any expected text field does not exist or has the wrong type
     */
    public void fillForm(PDAcroForm acroForm, AgreementContent agreementContent) throws IOException {
        acroForm.getDefaultResources().put(COSName.getPDFName(FONT_NAME), new PDType1Font(FontName.HELVETICA));

        setTextFieldValue(acroForm, TITLE_FIELD_NAME, agreementContent.getTitle());
        setTextFieldValue(acroForm, COMMENT_FIELD_NAME,
            agreementContent.getComment() + "\nLizenz-Felder sind falsch konfiguriert und werden deshalb ggf. geskippt."
                + "\nSofern lokaler Nutzer, gibt es keine Infos über Institut und Telefon.");

        final Author author = agreementContent.getAuthor();
        setTextFieldValue(acroForm, AUTHOR_EMAIL_FIELD_NAME, author.getEmail());
        setTextFieldValue(acroForm, AUTHOR_PHONE_FIELD_NAME, author.getPhone());
        setTextFieldValue(acroForm, AUTHOR_INSTITUTE_FIELD_NAME, author.getInstitute());
        setTextFieldValue(acroForm, AUTHOR_NAME_FIELD_NAME, author.getName());

        final PDRadioButton radio = (PDRadioButton) acroForm.getField("Group1");
        final String licenseValue = LICENSE_MAPPING.get(agreementContent.getLicense());
        if (radio.getExportValues().contains(licenseValue)) {
            radio.setValue(licenseValue);
        }
    }

    private void setTextFieldValue(PDAcroForm acroForm, String fieldName, String value) {
        final PDField field = acroForm.getField(fieldName);
        if (!(field instanceof PDTextField textField)) {
            throw new IllegalArgumentException("Field '" + fieldName + "' is not PDField or does not exist");
        }
        textField.setDefaultAppearance(DEFAULT_FIELD_APPERANCE);
        try {
            textField.setValue(value);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
