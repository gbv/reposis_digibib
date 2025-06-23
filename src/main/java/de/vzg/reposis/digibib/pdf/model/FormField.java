package de.vzg.reposis.digibib.pdf.model;

import java.util.Objects;

import de.vzg.reposis.digibib.pdf.serialization.adapters.FormFieldTypeAdapter;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Represents a single field within {@link FormData}.
 */
public class FormField {

    private String name;
    private FormFieldType type;
    private String value;

    /**
     * Returns the name of the PDF field.
     *
     * @return PDF field name
     */
    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the PDF field.
     *
     * @param name PDF field name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the type of the field.
     *
     * @return Field type
     */
    @XmlAttribute(name = "type")
    @XmlJavaTypeAdapter(FormFieldTypeAdapter.class)
    public FormFieldType getType() {
        return type;
    }

    /**
     * Sets the type of the field.
     *
     * @param type Field type
     */
    public void setType(FormFieldType type) {
        this.type = type;
    }

    /**
     * Returns the value of the field to be entered into the PDF.
     *
     * @return Field value
     */
    @XmlValue
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the field to be entered into the PDF.
     *
     * @param value Field value
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, value);
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
        FormField other = (FormField) obj;
        return Objects.equals(name, other.name) && type == other.type && Objects.equals(value, other.value);
    }

    @Override
    public String toString() {
        return "FormField{" +
            "name='" + name + '\'' +
            ", type=" + type +
            ", value='" + value + '\'' +
            '}';
    }
}
