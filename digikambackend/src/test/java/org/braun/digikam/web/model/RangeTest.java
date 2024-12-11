package org.braun.digikam.web.model;

import org.braun.digikam.common.DateWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author mbraun
 */
public class RangeTest {
    
    @Test
    public void testRange() {
        SearchParameter sp = new SearchParameter();
        sp.getIso().from(100).to(400);
        Assertions.assertTrue(sp.getIso().isValid(), "from > to");
        sp.getDate().from(new DateWrapper("2023----")).to(new DateWrapper("2022----"));
        Assertions.assertFalse(sp.getDate().isValid(), "from > to");
        sp.getDate().from(new DateWrapper("2022----")).to(new DateWrapper("20220630"));
        Assertions.assertTrue(sp.getDate().isValid(), "from > to");
    }
}
