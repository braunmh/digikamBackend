package org.braun.digikam.web.model;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mbraun
 */
public class CatAperture extends Catalogue<Double> {
    
    public static final List<CatAperture> values =
        Arrays.asList(new CatAperture().id(0).value(null).name(""),
            new CatAperture().id(1).value(1.0).name("1"),
            new CatAperture().id(2).value(1.4).name("1.4"),
            new CatAperture().id(3).value(1.7).name("1.7"),
            new CatAperture().id(4).value(2.0).name("2.0"),
            new CatAperture().id(5).value(2.8).name("2.8"),
            new CatAperture().id(6).value(4.0).name("4"),
            new CatAperture().id(7).value(5.6).name("5.6"),
            new CatAperture().id(8).value(8.0).name("8"),
            new CatAperture().id(9).value(11.0).name("11"),
            new CatAperture().id(10).value(16.0).name("16"),
            new CatAperture().id(11).value(22.0).name("22"),
            new CatAperture().id(12).value(32.0).name("32"),
            new CatAperture().id(13).value(45.0).name("45"),
            new CatAperture().id(14).value(1000000d).name("∞")
        );
    
    @Override
    public CatAperture id(int id) {
        setId(id);
        return this;
    }

    @Override
    public CatAperture name(String name) {
        setName(name);
        return this;
    }

    @Override
    public CatAperture value(Double value) {
        setValue(value);
        return this;
    }
    
    public static String findNearest(Double value) {
//        CatAperture last = values.get(0);
        if (value == null || value == 0) {
            return values.get(0).getName();
        }
        return String.valueOf(value);
    }

    @Override
    public CatAperture copy() {
        return new CatAperture().id(getId()).name(getName()).value(getValue());
    }
    
}
