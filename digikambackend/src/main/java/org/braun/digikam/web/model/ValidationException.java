package org.braun.digikam.web.model;

/**
 *
 * @author mbraun
 */
public class ValidationException extends Exception {

    private final String field;
    private final String message;
    private final String[] parameter;

    public ValidationException(String field, String message, String... parameter) {
        this.field = field;
        this.message = message;
        this.parameter = parameter;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public String[] getParameter() {
        return parameter;
    }
}
