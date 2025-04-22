package org.braun.digikam.web.ui;

import org.primefaces.PrimeFaces;

/**
 *
 * @author mbraun
 */
public interface DialogBean {
    
    void onload();
    
    default void close() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
    
    default void close(String returnValue) {
        PrimeFaces.current().dialog().closeDynamic(returnValue);
    }
    
}
