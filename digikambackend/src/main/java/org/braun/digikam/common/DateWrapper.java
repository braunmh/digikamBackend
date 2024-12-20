package org.braun.digikam.common;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import org.braun.digikam.backend.BadRequestException;

/**
 *
 * @author mbraun
 */
public class DateWrapper implements Comparable<DateWrapper> {

    private final UncompleteDateTime udt;

    public DateWrapper() {
        this(null);
    }
    
    public DateWrapper(String udt) {
        UncompleteDateTime temp;
        try {
            temp = new UncompleteDateTime(udt);
        } catch (BadRequestException e) {
            temp = null;
        }
        this.udt = temp;
    }

    public boolean isEmpty() {
        return udt == null || isEmpty(udt.getYear());
    }

    public Date getLowerBound() {
        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.YEAR, udt.getYear());
        if (isEmpty(udt.getMonth())) {
            temp.set(Calendar.MONTH, 0);
        } else {
            temp.set(Calendar.MONTH, udt.getMonth() - 1);
        }

        if (isEmpty(udt.getDay())) {
            temp.set(Calendar.DATE, 1);
        } else {
            temp.set(Calendar.DATE, udt.getDay());
        }

        if (isEmpty(udt.getHour())) {
            temp.set(Calendar.HOUR_OF_DAY, 0);
        } else {
            temp.set(Calendar.HOUR_OF_DAY, udt.getHour());
        }

        if (isEmpty(udt.getMinute())) {
            temp.set(Calendar.MINUTE, 0);
        } else {
            temp.set(Calendar.MINUTE, udt.getMinute());
        }

        if (isEmpty(udt.getSecond())) {
            temp.set(Calendar.SECOND, 0);
        } else {
            temp.set(Calendar.SECOND, udt.getSecond());
        }

        return temp.getTime();
    }

    public UncompleteDateTime getUncompleteDateTime() {
        return udt;
    }
    
    public Date getUpperBound() {
        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.YEAR, udt.getYear());
        if (isEmpty(udt.getMonth())) {
            temp.set(Calendar.MONTH, 11);
        } else {
            temp.set(Calendar.MONTH, udt.getMonth() - 1);
        }

        if (isEmpty(udt.getDay())) {
            temp.add(Calendar.MONTH, 1);
            temp.add(Calendar.DATE, -1);
        } else {
            temp.set(Calendar.DATE, udt.getDay());
        }

        if (isEmpty(udt.getHour())) {
            temp.set(Calendar.HOUR_OF_DAY, 23);
        } else {
            temp.set(Calendar.HOUR_OF_DAY, udt.getHour());
        }

        if (isEmpty(udt.getMinute())) {
            temp.set(Calendar.MINUTE, 59);
        } else {
            temp.set(Calendar.MINUTE, udt.getMinute());
        }

        if (isEmpty(udt.getSecond())) {
            temp.set(Calendar.SECOND, 59);
        } else {
            temp.set(Calendar.SECOND, udt.getSecond());
        }

        return temp.getTime();
    }

    public String getUpperBoundZdtFormatted() {
        return dateToZdtFormatted(getUpperBound());
    }
    
    public String getLowerBoundZdtFormatted() {
        return dateToZdtFormatted(getLowerBound());
    }
    
    private String dateToZdtFormatted(Date date) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return zdt.format(DateTimeFormatter.ISO_INSTANT);
    }
    
    private boolean isEmpty(Integer i) {
        return i == null || i == 0;
    }

    @Override
    public int compareTo(DateWrapper o) {
        if ((o == null || o.isEmpty()) && isEmpty()) {
            return 0;
        }
        if (o == null || o.isEmpty()) {
            return 1;
        }
        if (isEmpty()) {
            return -1;
        }
        return getUncompleteDateTime().toString().compareTo( o.getUncompleteDateTime().toString());
    }
}
