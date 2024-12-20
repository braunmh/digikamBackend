package org.braun.digikam.web.model;

import java.util.Comparator;
import org.braun.digikam.backend.model.Media;

/**
 *
 * @author mbraun
 */
public class MediaDateComparator implements Comparator<Media> {

    private final int sortOrder;
    
    public MediaDateComparator(boolean ascending) {
        sortOrder = (ascending) ? 1 : -1;
    }

    @Override
    public int compare(Media o1, Media o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null) {
            return -1 * sortOrder;
        }
        if (o2 == null) {
            return 1 * sortOrder;
        }
        int date = o1.getCreationDate().compareTo(o2.getCreationDate());
        if (date == 0) {
            return (int) (o1.getScore() - o2.getScore());
        }
        return date * sortOrder;
    }
    
}
