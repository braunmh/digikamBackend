package org.braun.digikam.backend.search;

/**
 *
 * @author mbraun
 */
public class TextCondition extends Condition<String> {
    
    public TextCondition(String columnName, String value) throws ConditionParseException {
        super(columnName, value, String.class);
    }

    @Override
    protected Predicate newLikePredicate(String columnName, String value) {
        if (!value.startsWith("*")) {
            value = "*" + value;
        }
        if (!value.endsWith("*")) {
            value = value + "*";
        }
        return new LikePredicate(columnName, value);
    }

    @Override
    protected Predicate<String> newSimplePredicate(String columnName, String value) throws ConditionParseException {
        return newLikePredicate(columnName, value);
    }
 
}
