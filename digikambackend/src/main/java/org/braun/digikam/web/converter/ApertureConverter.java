package org.braun.digikam.web.converter;

import jakarta.faces.convert.FacesConverter;
import java.util.List;
import org.braun.digikam.web.model.CatAperture;

/**
 *
 * @author mbraun
 */
@FacesConverter(value = "apertureConverter")
public class ApertureConverter extends CatalogueConverter<CatAperture> {

    @Override
    protected List<CatAperture> getValues() {
        return CatAperture.values;
    }

    
}
