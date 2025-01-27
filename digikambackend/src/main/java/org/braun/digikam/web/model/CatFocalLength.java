package org.braun.digikam.web.model;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mbraun
 */
public class CatFocalLength extends Catalogue<Integer> {
    
    public static final List<CatFocalLength> values =
        Arrays.asList(new CatFocalLength().id(0).value(null).name(""),
            new CatFocalLength().id(1).value(1).name("1 mm"),
            new CatFocalLength().id(2).value(2).name("2 mm"),
            new CatFocalLength().id(3).value(5).name("5 mm"),
            new CatFocalLength().id(4).value(10).name("10 mm"),
            new CatFocalLength().id(5).value(15).name("15 mm"),
            new CatFocalLength().id(6).value(20).name("20 mm"),
            new CatFocalLength().id(7).value(25).name("25 mm"),
            new CatFocalLength().id(8).value(30).name("30 mm"),
            new CatFocalLength().id(9).value(35).name("35 mm"),
            new CatFocalLength().id(10).value(50).name("50 mm"),
            new CatFocalLength().id(11).value(60).name("60 mm"),
            new CatFocalLength().id(12).value(70).name("70 mm"),
            new CatFocalLength().id(13).value(80).name("80 mm"),
            new CatFocalLength().id(14).value(90).name("90 mm"),
            new CatFocalLength().id(15).value(100).name("100 mm"),
            new CatFocalLength().id(16).value(200).name("200 mm"),
            new CatFocalLength().id(17).value(300).name("300 mm"),
            new CatFocalLength().id(18).value(400).name("400 mm"),
            new CatFocalLength().id(19).value(500).name("500 mm"),
            new CatFocalLength().id(20).value(700).name("700 mm"),
            new CatFocalLength().id(21).value(1000).name("1000 mm"),
            new CatFocalLength().id(22).value(15).name("∞")
        );
    
    @Override
    public CatFocalLength id(int id) {
        setId(id);
        return this;
    }

    @Override
    public CatFocalLength name(String name) {
        setName(name);
        return this;
    }

    @Override
    public CatFocalLength value(Integer value) {
        setValue(value);
        return this;
    }
}
