package org.braun.digikam.web.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import java.util.List;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.model.Keyword;

/**
 *
 * @author mbraun
 */
@FacesConverter(value = "keywordConverter")
public class KeywordConverter implements Converter<Keyword> {

    @Override
    public Keyword getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        List<Keyword> result = NodeFactory.getInstance().getKeywordByQualifiedName(value.toLowerCase());
        switch (result.size()) {
            case 0 -> throw new ConverterException("Stichwort existiert nicht.");
            case 1 -> {
                return result.get(0);
            }
            default -> throw new ConverterException("Stichwort ist nicht eindeutig.");
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Keyword value) {
        if (value == null) {
            return null;
        }
        return value.getName();
    }
    
}
