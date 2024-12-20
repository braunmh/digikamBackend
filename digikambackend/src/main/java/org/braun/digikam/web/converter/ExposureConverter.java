package org.braun.digikam.web.converter;

import jakarta.faces.convert.FacesConverter;
import java.util.List;
import org.braun.digikam.web.model.CatExposure;

/**
 *
 * @author mbraun
 */
@FacesConverter(value = "exposureConverter")
public class ExposureConverter extends CatalogueConverter<CatExposure> {

    @Override
    protected List<CatExposure> getValues() {
        return CatExposure.values;
    }

    
}
