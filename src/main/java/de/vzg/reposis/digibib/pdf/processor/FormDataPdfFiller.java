package de.vzg.reposis.digibib.pdf.processor;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import de.vzg.reposis.digibib.agreement.exceptions.AgreementException;
import de.vzg.reposis.digibib.pdf.model.FormData;
import de.vzg.reposis.digibib.pdf.model.FormField;

/**
 * Utility class for processing and filling PDF forms.
 * <p>
 * This class provides functionality to populate fields in a {@link PDAcroForm} using data from an
 * {@link FormData} object. It supports text fields, checkboxes, and radio buttons.
 * <p>
 * The class also ensures that text fields use a default font and appearance (Helvetica, size 10).
 */
public class FormDataPdfFiller {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String FONT_NAME = "Helvetica";
    private static final String DEFAULT_FIELD_APPERANCE = "/" + FONT_NAME + " 10 Tf 0 g";

    /**
     * Fills the provided PDF form with the given agreement form data.
     *
     * @param acroForm the {@link PDAcroForm} representing the PDF form to fill
     * @param formData the {@link FormData} containing field names, types, and values
     * @throws IOException if an I/O error occurs while setting the field values
     * @throws PdfFormProcessorException if a required PDF field is missing or a value is invalid
     */
    public void fillForm(PDAcroForm acroForm, FormData formData) throws IOException {
        LOGGER.debug("Filling acro form with form data...");
        acroForm.getDefaultResources().put(COSName.getPDFName(FONT_NAME), new PDType1Font(FontName.HELVETICA));

        for (FormField field : formData.getFields()) {
            PDField pdField = acroForm.getField(field.getName());

            if (pdField != null) {
                final String value = field.getValue();
                switch (field.getType()) {
                    case TEXT:
                        if (!(pdField instanceof PDTextField textField)) {
                            throw new AgreementException(
                                "Field '" + field.getName() + "' is not PDTextField.");
                        }
                        textField.setDefaultAppearance(DEFAULT_FIELD_APPERANCE);
                        pdField.setValue(value);
                        break;
                    case CHECKBOX:
                        pdField.setValue(Boolean.parseBoolean(field.getValue()) ? "Yes" : "Off");
                        break;
                    case RADIO:
                        if (!(pdField instanceof PDRadioButton radioButton)) {
                            throw new AgreementException(
                                "Field '" + field.getName() + "' is not PDRadioButton.");
                        }
                        if (radioButton.getOnValues().contains(value)) {
                            pdField.setValue(value);
                        } else {
                            throw new PdfFormProcessorException(
                                "Radio '" + field.getName() + "' has no value '" + value + "'");
                        }
                        break;
                }
            } else {
                throw new PdfFormProcessorException("PDF field '" + field.getName() + "' not found");
            }
        }
    }
}
