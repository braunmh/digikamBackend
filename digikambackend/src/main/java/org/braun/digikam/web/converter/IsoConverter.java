package org.braun.digikam.web.converter;

import jakarta.faces.convert.FacesConverter;
import java.util.List;
import org.braun.digikam.web.model.CatIso;

/**
 *
 * @author mbraun
 */
@FacesConverter(value = "isoConverter")
public class IsoConverter extends CatalogueConverter<CatIso> {

    @Override
    protected List<CatIso> getValues() {
        return CatIso.values;
    }

    
}
