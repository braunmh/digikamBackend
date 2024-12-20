package org.braun.digikam.web.model;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mbraun
 */
public class CatOrientation extends Catalogue<String> {
    
    public static final List<CatOrientation> values =
        Arrays.asList(
            new CatOrientation().id(0).value(null).name("--"),
            new CatOrientation().id(1).value("Landscape").name("Querformat"),
            new CatOrientation().id(5).value("Portrait").name("Hochformat")
        );
    
    @Override
    public CatOrientation id(int id) {
        setId(id);
        return this;
    }

    @Override
    public CatOrientation name(String name) {
        setName(name);
        return this;
    }

    @Override
    public CatOrientation value(String value) {
        setValue(value);
        return this;
    }
}
