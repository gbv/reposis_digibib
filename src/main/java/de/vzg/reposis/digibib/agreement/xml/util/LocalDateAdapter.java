package de.vzg.reposis.digibib.agreement.xml.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

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
