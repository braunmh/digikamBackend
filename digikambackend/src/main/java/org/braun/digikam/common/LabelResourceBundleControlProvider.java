package org.braun.digikam.common;

import org.braun.extrb.ExtResourceBundleControlProvider;
import org.braun.extrb.spi.ExtResourceProvider;

/**
 *
 * @author mbraun
 */
public class LabelResourceBundleControlProvider extends ExtResourceBundleControlProvider {

    @Override
    protected ExtResourceProvider getExtResourceProviderClass(String baseName) {
        if (LabelResourceBundle.BASE_NAME.equals(baseName)) {
            return new LabelResourceProvider();
        } else {
            return null;
        }
    }
    
}
