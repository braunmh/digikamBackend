package org.braun.digikam.web.converter;

import jakarta.faces.convert.FacesConverter;
import java.util.List;
import org.braun.digikam.web.model.CatFocalLength;

/**
 *
 * @author mbraun
 */
@FacesConverter(value = "focalLengthConverter")
public class FocalLengthConverter extends CatalogueConverter<CatFocalLength> {

    @Override
    protected List<CatFocalLength> getValues() {
        return CatFocalLength.values;
    }

    
}
