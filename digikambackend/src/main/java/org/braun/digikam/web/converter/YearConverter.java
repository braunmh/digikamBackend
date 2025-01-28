package org.braun.digikam.web.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import java.util.Calendar;

/**
 *
 * @author mbraun
 */
@FacesConverter(value = "yearConverter")
public class YearConverter implements Converter<Integer> {

    @Override
    public Integer getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        if (value.length() == 2 || value.length() == 4) {
            try {
                Calendar now = Calendar.getInstance();
                int year4 = now.get(Calendar.YEAR);
                int year = Integer.parseInt(value);
                if (year < 100) {
                    int year2 = year4 % 100;
                    year = (year > year2) ? year + 1900 : year + 2000;
                }
                if (year > year4) {
                    throw new ConverterException("Jahresangaben in der Zukunft sind nicht zulässig");
                }
                return year;
            } catch (NumberFormatException e) {
                throw new ConverterException("Für Jahresangaben sind nur Zahlen zulässig");
            }
        } else {
            throw new ConverterException("Die Jahresangabe muss entweder 2 oder 4-stellig sein!");
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Integer value) {
        return (value == null) ? null : String.valueOf(value);
    }

}
