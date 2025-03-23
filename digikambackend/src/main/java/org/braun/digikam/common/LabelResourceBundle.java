package org.braun.digikam.common;

import org.braun.extrb.AbstractExtResourceBundle;

/**
 *
 * @author mbraun
 */
public class LabelResourceBundle extends AbstractExtResourceBundle {

    public static final String BASE_NAME = "org.braun.digikam.common.LabelResourceBundle";
    
    public LabelResourceBundle() {
        super(new LabelResourceProvider(), BASE_NAME);
    }
    
    @Override
    public void refresh() {
        LabelResourceBundle.clearCache();
    }
}
