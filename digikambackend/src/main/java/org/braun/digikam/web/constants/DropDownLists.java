package org.braun.digikam.web.constants;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.braun.digikam.web.model.DropDownValue;
import org.braun.digikam.web.model.DropDownValue.IntegerValue;
import org.braun.digikam.web.model.DropDownValue.FocalLenthValue;
import org.braun.digikam.web.model.DropDownValue.ExposureValue;

/**
 *
 * @author mbraun
 */
public final class DropDownLists {

    private DropDownLists() {
    }
    
    public static final List<IntegerValue> ISO_VALUES =
            Arrays.asList(
                new IntegerValue().valueName(0),
                new IntegerValue().valueName(50),
                new IntegerValue().valueName(64),
                new IntegerValue().valueName(100),
                new IntegerValue().valueName(200),
                new IntegerValue().valueName(400),
                new IntegerValue().valueName(800),
                new IntegerValue().valueName(1600),
                new IntegerValue().valueName(3200),
                new IntegerValue().valueName(6400),
                new IntegerValue().valueName(12800),
                new IntegerValue().valueName(25600),
                new IntegerValue().valueName(1000000));
    
    public static final List<FocalLenthValue> FOCAL_LENGTH_VALUES =
            Arrays.asList(
                new FocalLenthValue().valueName(0),
                new FocalLenthValue().valueName(1),
                new FocalLenthValue().valueName(2),
                new FocalLenthValue().valueName(5),
                new FocalLenthValue().valueName(10),
                new FocalLenthValue().valueName(15),
                new FocalLenthValue().valueName(20),
                new FocalLenthValue().valueName(25),
                new FocalLenthValue().valueName(30),
                new FocalLenthValue().valueName(35),
                new FocalLenthValue().valueName(50),
                new FocalLenthValue().valueName(60),
                new FocalLenthValue().valueName(70),
                new FocalLenthValue().valueName(80),
                new FocalLenthValue().valueName(90),
                new FocalLenthValue().valueName(100),
                new FocalLenthValue().valueName(150),
                new FocalLenthValue().valueName(200),
                new FocalLenthValue().valueName(300),
                new FocalLenthValue().valueName(400),
                new FocalLenthValue().valueName(500),
                new FocalLenthValue().valueName(700),
                new FocalLenthValue().valueName(1000),
                new FocalLenthValue().value(1000000).name(" ∞"));
    
    public static final List<ExposureValue> EXPOSURE_VALUES =
            Arrays.asList(
                new ExposureValue().value(0.0).name("0"),
                new ExposureValue().value(8000d).name("1/8000"),
                new ExposureValue().value(4000d).name("1/4000"),
                new ExposureValue().value(2000d).name("1/2000"),
                new ExposureValue().value(1000d).name("1/1000"),
                new ExposureValue().value(500d).name("1/500"),
                new ExposureValue().value(250d).name("1/250"),
                new ExposureValue().value(125d).name("1/125"),
                new ExposureValue().value(60d).name("1/60"),
                new ExposureValue().value(30d).name("1/30"),
                new ExposureValue().value(15d).name("1/15"),
                new ExposureValue().value(8d).name("1/8"),
                new ExposureValue().value(4d).name("1/4"),
                new ExposureValue().value(2d).name("1/2"),
                new ExposureValue().value(1d).name("1"),
                new ExposureValue().value(0.5).name("2"),
                new ExposureValue().value(0.25).name("4"),
                new ExposureValue().value(0.125).name("8"),
                new ExposureValue().value(0.0625).name("16"),
                new ExposureValue().value(0.03125).name("32"),
                new ExposureValue().value(0.00001).name("∞"));
    
    public static <C extends DropDownValue<?, C>> C find (String value, List<C> values, Class<C> clazz) {
        Optional<C> entry = values.stream().filter(d -> value.equals(d.getName())).findFirst();
        return (entry.isPresent()) ? entry.get() : null;
    }
}
