package org.braun.digikam.web.model;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mbraun
 */
public class CatDiagram extends Catalogue<String> {

    public static final int EXPOSURE_TIME = 1;
    public static final int FOCALLENGTH = 3;
    public static final int APERTURE = 2;
    public static final int ISO = 4;
    
    public static final List<CatDiagram> values =
        Arrays.asList(
            new CatDiagram().id(0).value(null).name(""),
            new CatDiagram().id(EXPOSURE_TIME).value("exposureTime").name("Belichtungszeit"),
            new CatDiagram().id(APERTURE).value("apertue").name("Blende"),
            new CatDiagram().id(FOCALLENGTH).value("focalLength").name("Brennweite"),
            new CatDiagram().id(ISO).value("iso").name("ISO")
        );

    @Override
    public CatDiagram id(int id) {
        setId(id);
        return this;
    }

    @Override
    public CatDiagram name(String name) {
        setName(name);
        return this;
    }

    @Override
    public CatDiagram value(String value) {
        setValue(value);
        return this;
    }

    @Override
    public CatDiagram copy() {
        return new CatDiagram().id(getId()).name(getName()).value(getValue());
    }
    
    
}
