package de.vzg.reposis.digibib.pdf.model;

import java.util.List;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Represents the data of a form to be filled into a PDF AcroForm.
 * <p>
 * This model corresponds to the XML format <code>&lt;formData&gt;</code>,
 * containing a list of {@link FormField} elements.
 */
@XmlRootElement(name = "formData")
public class FormData {

    private List<FormField> fields;

    /**
     * Returns the list of fields to be filled into the PDF form.
     *
     * @return List of {@link FormField} objects
     */
    @XmlElement(name = "field")
    public List<FormField> getFields() {
        return fields;
    }

    /**
     * Sets the list of fields to be filled into the PDF form.
     *
     * @param fields List of {@link FormField} objects
     */
    public void setFields(List<FormField> fields) {
        this.fields = fields;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fields);
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
        FormData other = (FormData) obj;
        return Objects.equals(fields, other.fields);
    }
}
