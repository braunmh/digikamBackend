package org.braun.digikam.web.model;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mbraun
 */
public class CatExposure extends Catalogue<Double> {
    
    public static final List<CatExposure> values =
        Arrays.asList(new CatExposure().id(0).value(null).name(""),
            new CatExposure().id(1).value(1/8000d).name("1/8000"),
            new CatExposure().id(2).value(1/4000d).name("1/4000"),
            new CatExposure().id(3).value(1/2000d).name("1/2000"),
            new CatExposure().id(4).value(1/1000d).name("1/1000"),
            new CatExposure().id(5).value(1/500d).name("1/500"),
            new CatExposure().id(5).value(1/250d).name("1/250"),
            new CatExposure().id(5).value(1/125d).name("1/125"),
            new CatExposure().id(5).value(1/60d).name("1/60"),
            new CatExposure().id(5).value(1/30d).name("1/30"),
            new CatExposure().id(5).value(1/15d).name("1/15"),
            new CatExposure().id(5).value(1/8d).name("1/8"),
            new CatExposure().id(5).value(1/4d).name("1/4"),
            new CatExposure().id(5).value(1/2d).name("1/1"),
            new CatExposure().id(5).value(1d).name("1"),
            new CatExposure().id(5).value(0.5).name("2"),
            new CatExposure().id(5).value(0.25).name("4"),
            new CatExposure().id(5).value(0.125).name("8"),
            new CatExposure().id(5).value(0.0625).name("16"),
            new CatExposure().id(5).value(0.03125).name("32"),
            new CatExposure().id(5).value(0.00001).name("∞")
        );
    
    @Override
    public CatExposure id(int id) {
        setId(id);
        return this;
    }

    @Override
    public CatExposure name(String name) {
        setName(name);
        return this;
    }

    @Override
    public CatExposure value(Double value) {
        setValue(value);
        return this;
    }
    
    public static String findNearest(double value) {
        CatExposure last = values.get(0);
        if (value == 0) {
            return last.getName();
        }
        
        int i = 0;
        for (CatExposure ce : values) {
            if (i++ == 0) {
                continue;
            }
            if (ce.getValue() == null) {
                continue;
            }
            if (value == ce.getValue()) {
                return ce.getName();
            } else if (value < ce.getValue()) {
                return last.getName();
            }
            last = ce;
        }
        return last.getName();
    }
}
