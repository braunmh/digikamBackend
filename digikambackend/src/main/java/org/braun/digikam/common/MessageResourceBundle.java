package org.braun.digikam.common;

import org.braun.extrb.AbstractExtResourceBundle;

/**
 *
 * @author mbraun
 */
public class MessageResourceBundle extends AbstractExtResourceBundle {

    public static final String BASE_NAME = "org.braun.digikam.common.MessageResourceBundle";

    public MessageResourceBundle() {
        super(new MessageResourceProvider(), BASE_NAME);
    }

    @Override
    public void refresh() {
        MessageResourceBundle.clearCache();
    }
    
}
