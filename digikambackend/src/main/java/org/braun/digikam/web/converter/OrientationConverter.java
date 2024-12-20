package org.braun.digikam.web.converter;

import jakarta.faces.convert.FacesConverter;
import java.util.List;
import org.braun.digikam.web.model.CatOrientation;

/**
 *
 * @author mbraun
 */
@FacesConverter(value = "orientationConverter")
public class OrientationConverter extends CatalogueConverter<CatOrientation> {

    @Override
    protected List<CatOrientation> getValues() {
        return CatOrientation.values;
    }

    
}
