package org.braun.digikam.web.model;

import java.util.Comparator;
import org.braun.digikam.backend.model.Media;

/**
 * Sortiert zuerst absteigend nach Score und dan in Abhängigkeit der 
 * Sortierreihenfolge nach Datum.
 *
 * @author mbraun
 */
public class MediaScoreComparator implements Comparator<Media> {

    private final int sortOrder;
    
    public MediaScoreComparator(boolean ascending) {
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
        int score = (int) (o2.getScore() - o1.getScore());
        if (score == 0) {
            return sortOrder * o1.getCreationDate().compareTo(o2.getCreationDate());
        }
        return score;
    }
    
}
