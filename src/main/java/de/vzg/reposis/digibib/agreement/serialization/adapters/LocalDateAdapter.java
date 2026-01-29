package de.vzg.reposis.digibib.agreement.serialization.adapters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter to convert between {@link String} and {@link LocalDate} objects.
 * <p>
 * This adapter marshals a {@link LocalDate} to its ISO-8601 string representation
 * (e.g., "2025-08-08") and unmarshals such a string back to a {@link LocalDate}.
 * <p>
 * Uses the {@link DateTimeFormatter#ISO_LOCAL_DATE} formatter internally.
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate unmarshal(String value) {
        return (value == null || value.isEmpty()) ? null : LocalDate.parse(value, FORMATTER);
    }

    @Override
    public String marshal(LocalDate value) {
        return (value == null) ? null : value.format(FORMATTER);
    }
}
