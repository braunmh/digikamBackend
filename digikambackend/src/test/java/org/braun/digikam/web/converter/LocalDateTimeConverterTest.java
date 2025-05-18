package org.braun.digikam.web.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.Test;

/**
 *
 * @author mbraun
 */
public class LocalDateTimeConverterTest {
    
    //@Test
    public void testLocalTime()  {
        long duration = 205483;
        long millisecondes = duration % 1000;
        duration = duration / 1000;
        long seconds = (duration == 0) ? 0 : duration % 60;
        long minutes = (duration == 0) ? 0 : duration / 60;
        System.out.println(String.format("%d:%02d.%03d Minuten", minutes, seconds, millisecondes));
    }
    
    @Test
    public void testConverter() {
        LocalDateTime ldt = LocalDateTime.of(2021, 9, 5, 17, 25, 01);
        Calendar cal = Calendar.getInstance();
        cal.set(ldt.getYear(), ldt.getMonthValue()-1, ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond());
        Date date = cal.getTime();
        System.out.println(date);
    }
}
