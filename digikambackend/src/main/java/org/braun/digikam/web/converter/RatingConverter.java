package org.braun.digikam.web.converter;

import jakarta.faces.convert.FacesConverter;
import java.util.List;
import org.braun.digikam.web.model.CatRating;

/**
 *
 * @author mbraun
 */
@FacesConverter(value = "ratingConverter")
public class RatingConverter extends CatalogueConverter<CatRating> {

    @Override
    protected List<CatRating> getValues() {
        return CatRating.values;
    }

    
}
