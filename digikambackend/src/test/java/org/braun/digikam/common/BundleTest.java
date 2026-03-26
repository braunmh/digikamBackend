package org.braun.digikam.common;

import java.util.Map;
import java.util.ResourceBundle;
import org.braun.digikam.web.component.function.ResourceBundleWrapper;
import org.junit.jupiter.api.Test;

/**
 *
 * @author mbraun
 */
public class BundleTest {
    
    @Test
    public void work() {
        try {
            String creator = ResourceBundleWrapper.label("search.creator");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
