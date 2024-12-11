package org.braun.digikam.web.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.FacesConverter;
import java.util.Optional;
import org.braun.digikam.web.constants.DropDownLists;
import org.braun.digikam.web.model.DropDownValue;
import org.braun.digikam.web.model.DropDownValue.FocalLenthValue;

/**
 *
 * @author mbraun
 */
@FacesConverter("focalLengthConverter")
public class FocalLenghtConverter extends TypedConverter<Integer> {

    @Override
    public Integer getAsType(FacesContext context, UIComponent component, String value) {
        FocalLenthValue focalLength = DropDownLists.find(value, DropDownLists.FOCAL_LENGTH_VALUES, DropDownValue.FocalLenthValue.class);
        return (focalLength == null) ? null : focalLength.getValue();
    }

    @Override
    public String getTypeAsString(FacesContext context, UIComponent component, Integer value) {
        Optional<FocalLenthValue> focalLength = DropDownLists.FOCAL_LENGTH_VALUES.stream().filter(f -> f.getValue().equals(value)).findFirst();
        return (focalLength.isPresent()) ? focalLength.get().getName() : "";
    }
    
}
