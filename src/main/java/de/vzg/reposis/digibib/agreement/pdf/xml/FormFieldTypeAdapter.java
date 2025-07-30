package de.vzg.reposis.digibib.agreement.pdf.xml;

import de.vzg.reposis.digibib.agreement.model.FormFieldType;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter to convert between {@link FormFieldType} and its XML representation.
 */
public class FormFieldTypeAdapter extends XmlAdapter<String, FormFieldType> {

    @Override
    public FormFieldType unmarshal(String v) throws Exception {
        return FormFieldType.valueOf(v.toUpperCase());
    }

    @Override
    public String marshal(FormFieldType v) throws Exception {
        return v.name().toLowerCase();
    }
}
