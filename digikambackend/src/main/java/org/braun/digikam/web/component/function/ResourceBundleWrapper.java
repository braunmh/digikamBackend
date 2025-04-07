package org.braun.digikam.web.component.function;

import jakarta.faces.context.FacesContext;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.braun.digikam.common.LabelResourceBundle;
import org.braun.digikam.common.MessageResourceBundle;

/**
 *
 * @author mbraun
 */
public class ResourceBundleWrapper {

    private ResourceBundleWrapper() {
    }

    public static String label(String key) {
        return valueFromResourceBundle(LabelResourceBundle.BASE_NAME, key);
    }

    public static String message(String key) {
        return valueFromResourceBundle(MessageResourceBundle.BASE_NAME, key);
    }

    @SuppressWarnings("null")
    private static String valueFromResourceBundle(String baseName, String key) {
        ResourceBundle rb = null;
        try {
            rb = ResourceBundle.getBundle(baseName);
        } catch (MissingResourceException e) {
            switch (baseName) {
                case LabelResourceBundle.BASE_NAME:
                    rb = new LabelResourceBundle();
                    break;
                case MessageResourceBundle.BASE_NAME:
                    rb = new MessageResourceBundle();
                default:
                    throw e;
            }
        }
        return rb.getString(key);
    }
    
    public static void refreshLabel() {
        new LabelResourceBundle().refresh();
    }

    public static void refreshMessage() {
        new MessageResourceBundle().refresh();
    }
}
