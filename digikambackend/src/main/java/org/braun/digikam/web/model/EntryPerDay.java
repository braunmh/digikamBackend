package org.braun.digikam.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.braun.digikam.backend.model.Media;

/**
 *
 * @author mbraun
 */
public class EntryPerDay implements Serializable {
    
    private Date date;
    private final List<Media> entries;

    public EntryPerDay() {
        entries = new ArrayList<>();
    }

    public List<Media> getEntries() {
        return entries;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
