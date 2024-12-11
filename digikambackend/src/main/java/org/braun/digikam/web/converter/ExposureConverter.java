package org.braun.digikam.web.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.FacesConverter;
import java.util.Optional;
import org.braun.digikam.web.constants.DropDownLists;
import org.braun.digikam.web.model.DropDownValue;
import org.braun.digikam.web.model.DropDownValue.ExposureValue;

/**
 *
 * @author mbraun
 */
@FacesConverter("exposureConverter")
public class ExposureConverter extends TypedConverter<Double> {

    @Override
    public Double getAsType(FacesContext context, UIComponent component, String value) {
        ExposureValue exposure = DropDownLists.find(value, DropDownLists.EXPOSURE_VALUES, DropDownValue.ExposureValue.class);
        return (exposure == null) ? null : exposure.getValue();
    }

    @Override
    public String getTypeAsString(FacesContext context, UIComponent component, Double value) {
        Optional<ExposureValue> exposure = DropDownLists.EXPOSURE_VALUES.stream().filter(f -> f.getValue().equals(value)).findFirst();
        return (exposure.isPresent()) ? exposure.get().getName() : "";
    }
    
}
