package org.braun.digikam.backend;

import org.braun.digikam.backend.api.ApiException;

/**
 *
 * @author mbraun
 */
public class BadRequestException extends ApiException {
    
    public BadRequestException(String message) {
        super(400, message);
    }
}
