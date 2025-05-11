package org.braun.digikam.web.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mbraun
 */
@FacesConverter(value = "localDateTimeConverter")
public class LocalDateTimeConverter implements Converter<LocalDateTime> {

    private static final int[] monthes = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    @Override
    public LocalDateTime getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || StringUtils.isBlank(value)) {
            return null;
        }
        String[] parts = StringUtils.normalizeSpace(value).split(" ");
        int [] date;
        int[] time;
        switch (parts.length) {
            case 1 -> {
                date = parseDate(parts[0]);
                time = new int[] {12,0,0};
            }
            case 2 -> {
                date = parseDate(parts[0]);
                time = parseTime(parts[1]);
            }
            default -> {
                throw new ConverterException("Ungültige Datumsangabe");
            }
        }
        return LocalDateTime.of(date[0], date[1], date[2], time[0], time[1], time[2]);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return getDateTimeFormatter().format(value);
    }
    
    private int[] parseDate(String value) {
        List<String> parts = getSplittedValues(value, "\\.");
        int year;
        int month;
        int day;
        if (parts.size() != 3) {
            throw new ConverterException("Ungültige Datums Angabe.");
        }
        int[] date = new int[3];
        day = parseNumber(parts.get(0), 1, 31, "Tag");
        month = parseNumber(parts.get(1), 1, 12, "Monat");
        year = parseNumber(parts.get(2), 0, 9999, "Jahr");
        if (day > monthes[month - 1]) {
            throw new ConverterException("Diese Tagesangabe ist für diesen Monat nicht zulässig");
        }
        if (month == 2 && year % 4 != 0 && day > 28) {
            throw new ConverterException("Diese Tagesangabe ist für diesen Monat nicht zulässig");
        }
        if (year < 100) {
            Calendar today = Calendar.getInstance();
            int y23 = today.get(Calendar.YEAR) % 100;
            int y01 = today.get(Calendar.YEAR) / 100;
            if (y23 < year) {
                y01--;
            }
            year = y01 * 100 + year;
        }
        date[0] = year;
        date[1] = month;
        date[2] = day;
        return date;
    }

    private int[] parseTime(String value) {
        List<String> parts = getSplittedValues(value, ":");
        int hour = 12;
        int minute = 0;
        int second = 0;
        int[] time = new int[3];
        
        switch (parts.size()) {
            case 1 -> hour = parseNumber(parts.get(0), 0, 23, "Stunde");
            case 2 -> {
                hour = parseNumber(parts.get(0), 0, 23, "Stunde");
                minute = parseNumber(parts.get(1), 0, 59, "Minute");
            }
            case 3 -> {
                hour = parseNumber(parts.get(0), 0, 23, "Stunde");
                minute = parseNumber(parts.get(1), 0, 59, "Minute");
                second = parseNumber(parts.get(2), 0, 59, "Sekunde");
            }
            default -> throw new ConverterException("Ungültige Zeitangabe hh.mm.ss");
        }
        time[0] = hour;
        time[1] = minute;
        time[2] = second;
        return time;
    }

    private int parseNumber(String value, int from, int to, String type) {
        try {
            int i = Integer.parseInt(value);
            if (i < from || i > to) {
                throw new ConverterException("Ungültiger Wertebreich für " + type);
            }
            return i;
        } catch (NumberFormatException e) {
            throw new ConverterException("Ungültige Angabe für " + type);
        }
    }

    private List<String> getSplittedValues(String value, String token) {
        String[] x = value.split(token);
        List<String> parts = new ArrayList<>();
        for (String s : x) {
            if (s != null) {
                parts.add(s);
            }
        }
        return parts;
    }
    
    private DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    }
}
