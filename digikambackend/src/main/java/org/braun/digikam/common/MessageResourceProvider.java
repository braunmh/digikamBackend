package org.braun.digikam.common;

import org.braun.extrb.spi.ExtResourceProvider;
import org.braun.extrb.spi.IExtResourceEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.braun.digikam.backend.dao.LabelFacade;
import org.braun.digikam.backend.entity.Label;
import org.braun.digikam.backend.util.Util;

/**
 *
 * @author mbraun
 */
public class MessageResourceProvider implements ExtResourceProvider {

    private LabelFacade facade;
    
    @Override
    public List<IExtResourceEntry> getAll(String basename, Locale locale) {
        List<Label> temp = getFacade().findAll(locale);
        List<IExtResourceEntry> res = new ArrayList<>(temp.size());
        
        for (Label entry : getFacade().findAll(locale)) {
            res.add(entry);
        }
        return res;
    }

    @Override
    public Date lastModified(String basename, Locale locale) {
        return getFacade().getLastModified(locale);
    }

    @Override
    public long getTimeToLive(String baseName, Locale locale) {
        return 60000; // 1 Minuten (10 * 60 * 1000)
    }

    private LabelFacade getFacade() {
        if (facade == null) {
            facade = Util.Cdi.lookup(LabelFacade.class);
        }
        return facade;
    }
}
