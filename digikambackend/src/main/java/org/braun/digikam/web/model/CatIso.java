package org.braun.digikam.web.model;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mbraun
 */
public class CatIso extends Catalogue<Integer> {
    
    public static final List<CatIso> values =
        Arrays.asList(new CatIso().id(0).value(null).name(""),
            new CatIso().id(1).value(50).name("50"),
            new CatIso().id(2).value(64).name("64"),
            new CatIso().id(3).value(100).name("100"),
            new CatIso().id(4).value(200).name("200"),
            new CatIso().id(4).value(400).name("400"),
            new CatIso().id(4).value(800).name("800"),
            new CatIso().id(4).value(1600).name("1600"),
            new CatIso().id(4).value(3200).name("3200"),
            new CatIso().id(4).value(6400).name("6400"),
            new CatIso().id(4).value(12800).name("12800"),
            new CatIso().id(4).value(25600).name("25600"),
            new CatIso().id(5).value(5).name("1000000")
        );
    
    @Override
    public CatIso id(int id) {
        setId(id);
        return this;
    }

    @Override
    public CatIso name(String name) {
        setName(name);
        return this;
    }

    @Override
    public CatIso value(Integer value) {
        setValue(value);
        return this;
    }
}
