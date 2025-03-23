package org.braun.digikam;

import java.util.ResourceBundle;

/**
 *
 * @author mbraun
 */
public class Schlampe {

    public static void main(String... args) {
        try {
        ResourceBundle bundle = ResourceBundle.getBundle("test.label");
        if (bundle != null) {
            System.out.println("Heureka");
            System.out.println(bundle.getString("search.camera"));
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
