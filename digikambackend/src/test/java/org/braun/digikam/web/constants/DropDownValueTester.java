package org.braun.digikam.web.constants;

import org.braun.digikam.web.model.DropDownValue.IntegerValue;
import org.braun.digikam.web.model.DropDownValue.FocalLenthValue;
import org.braun.digikam.web.model.DropDownValue.ExposureValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author mbraun
 */
public class DropDownValueTester {

    @Test
    public void testSingle() {
        try {
           IntegerValue v = DropDownLists.find("400", DropDownLists.ISO_VALUES, IntegerValue.class);
           Assertions.assertTrue(v != null, "Value not found");
           for (ExposureValue e : DropDownLists.EXPOSURE_VALUES) {
               System.out.println(e);
           }
           System.out.println(v);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
