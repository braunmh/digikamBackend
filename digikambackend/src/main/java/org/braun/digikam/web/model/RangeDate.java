package org.braun.digikam.web.model;

import org.braun.digikam.common.DateWrapper;

/**
 *
 * @author mbraun
 */
public class RangeDate extends Range<DateWrapper> {

    public RangeDate() {
    }
    
    public RangeDate(DateWrapper from, DateWrapper to) {
        super(from, to);
    }
    
    @Override
    public boolean isValid() {
        if (getFrom() == null || getFrom().isEmpty()) {
            return true;
        }
        if (getTo() == null || getTo().isEmpty()) {
            return true;
        }
        return getFrom().compareTo(getTo()) <= 0;
    }

    @Override
    public boolean isEmpty() {
        return (getFrom() == null || getFrom().isEmpty()) && (getTo() == null || getTo().isEmpty());
    }
    
}
