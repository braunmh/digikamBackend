package org.braun.digikam.web.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import java.util.List;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.model.Keyword;

/**
 *
 * @author mbraun
 */
@FacesConverter("keywordConverter")
public class KeywordConverter extends TypedConverter<Keyword> {

    @Override
    public Keyword getAsType(FacesContext context, UIComponent component, String value) {
        List<Keyword> result = NodeFactory.getInstance().getKeywordByName(value.toLowerCase());
        switch (result.size()) {
            case 0:
                throw new ConverterException("Stichwort existiert nicht.");
            case 1:
                return result.get(0);
            default:
                throw new ConverterException("Stichwort ist nicht eindeutig.");
        }
    }

    @Override
    public String getTypeAsString(FacesContext context, UIComponent component, Keyword value) {
        return value.getName();
    }
    
}
