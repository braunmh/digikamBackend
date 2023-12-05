package org.braun.digikam.backend.search;

import java.text.ParseException;

/**
 *
 * @author mbraun
 */
public class ConditionParseException extends ParseException {
    
    public ConditionParseException(String msg) {
        super(msg, 0);
    }
}
