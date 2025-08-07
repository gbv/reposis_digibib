package de.vzg.reposis.digibib.agreement.pdf.formfiller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;

import de.vzg.reposis.digibib.agreement.model.Agreement;

public class AgreementPdfFormFiller {

    private static final Logger LOGGER = LogManager.getLogger();

    public void fillForm(PDAcroForm acroForm, Agreement agreement) {
        LOGGER.debug(() -> acroForm.getFields().size());
        LOGGER.debug(() -> acroForm.getFields());
    }

}
