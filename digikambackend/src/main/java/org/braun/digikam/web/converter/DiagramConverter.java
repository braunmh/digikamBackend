package org.braun.digikam.web.converter;

import jakarta.faces.convert.FacesConverter;
import java.util.List;
import org.braun.digikam.web.model.CatDiagram;

/**
 *
 * @author mbraun
 */
@FacesConverter(value = "diagramConverter")
public class DiagramConverter extends CatalogueConverter<CatDiagram> {

    @Override
    protected List<CatDiagram> getValues() {
        return CatDiagram.values;
    }

    
}
