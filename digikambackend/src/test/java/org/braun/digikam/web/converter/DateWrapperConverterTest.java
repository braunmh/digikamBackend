package org.braun.digikam.web.converter;

import jakarta.faces.convert.ConverterException;
import org.braun.digikam.common.DateWrapper;
import org.junit.jupiter.api.Test;

/**
 *
 * @author mbraun
 */
public class DateWrapperConverterTest {
    
    @Test
    public void testAsObject() {
        String [] values = new String[] {
        "1.4.77 23:", "24", "-.24", "12.24", "12.1998", "1.4.77", "1.4.25",
        "24 23", "-.24 1.7", "12.24 -.-.45", "12.1998", "1.4.25 23:43:34",
        "13.24", "30.2.23"
        };
        DateWrapperConverter converter = new DateWrapperConverter();
        for (String in : values) {
            try {
                DateWrapper dateWrapper = (DateWrapper) converter.getAsObject(null, null, in);
                System.out.println(in + ", " + dateWrapper.getUncompleteDateTime() +  ", " + converter.getAsString(null, null, dateWrapper));
            } catch (ConverterException e) {
                System.out.println(in + ", " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }
    
}
