package org.braun.digikam.web.converter;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/**
 *
 * @author mbraun
 */
public class LocalDateTimeConverterTest {
    
    @Test
    public void testLocalTime()  {
        long duration = 205483;
        long millisecondes = duration % 1000;
        duration = duration / 1000;
        long seconds = (duration == 0) ? 0 : duration % 60;
        long minutes = (duration == 0) ? 0 : duration / 60;
        System.out.println(String.format("%d:%02d.%03d Minuten", minutes, seconds, millisecondes));
    }
    
    public void testConverter() {
        LocalDateTime ldt = LocalDateTime.of(2004, 1, 11, 23, 32, 0);
        LocalDateTimeConverter ldtc = new LocalDateTimeConverter();
        String fDateTime = (ldtc.getAsString(null, null, ldt));
        System.out.println(ldtc.getAsObject(null, null, fDateTime));
        System.out.println(ldtc.getAsObject(null, null, "1.4.58"));
        System.out.println(ldtc.getAsObject(null, null, "1.4.58 13"));
        System.out.println(ldtc.getAsObject(null, null, "1.4.11 13"));
        System.out.println(ldtc.getAsObject(null, null, "1.4.11  13:54"));
        System.out.println(ldtc.getAsString(null, null, ldtc.getAsObject(null, null, "1.4.11 13:54:07")));
    }
}
