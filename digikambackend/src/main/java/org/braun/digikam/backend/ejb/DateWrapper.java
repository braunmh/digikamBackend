package org.braun.digikam.backend.ejb;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import org.braun.digikam.backend.BadRequestException;
import org.braun.digikam.backend.util.UncompleteDateTime;

/**
 *
 * @author mbraun
 */
public class DateWrapper {

    private UncompleteDateTime udt;

    public DateWrapper(String udt) {
        try {
            this.udt = new UncompleteDateTime(udt);
        } catch (BadRequestException e) {
            this.udt = null;
        }
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
}
