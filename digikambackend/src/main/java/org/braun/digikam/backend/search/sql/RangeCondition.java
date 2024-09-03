package org.braun.digikam.backend.search.sql;

import org.braun.digikam.backend.search.ConditionParseException;
import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
public class RangeCondition<T extends Comparable<T>> implements ICondition {
    
    private enum ConditionType {
        between, from, to, empty;
    }

    private final T fromValue;
    private final T toValue;
    private final String columnName;
    private final ConditionType type;

    public RangeCondition(String columnName, T fromValue, T toValue) throws ConditionParseException {
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.columnName = columnName;
        if (fromValue == null && toValue == null) {
            type = ConditionType.empty;
        } else if (fromValue != null && toValue != null) {
            if (fromValue.compareTo(toValue) > 0) {
                throw new ConditionParseException("toValue must be greater than fromValue");
            }
            type = ConditionType.between;
        } else if (fromValue != null) {
            type = ConditionType.from;
        } else {
            type = ConditionType.to;
        }
    }
    
    @Override
    public String getCondition() {
        switch (type) {
            case from:
                return columnName + " >= ?";
            case to:
                return columnName + " <= ?";
            case between:
                return columnName + " between ? and ?";
            default:
                return "";
        }
    }

    @Override
    public int setParameter(Query query, int position) {
        switch(type) {
            case from:
                query.setParameter(++position, fromValue);
                break;
            case to:
                query.setParameter(++position, toValue);
                break;
            case between:
                query.setParameter(++position, fromValue);
                query.setParameter(++position, toValue);
                break;
        }
        return position;
    }

    @Override
    public boolean isEmpty() {
        return type == ConditionType.empty;
    }

    @Override
    public int print(StringBuilder result, int position) {
        result.append("\n P ").append(position).append(" => (");
        switch(type) {
            case from:
                position++;
                result.append(" ").append(fromValue);
                break;
            case to:
                position++;
                result.append(" ").append(toValue);
                break;
            case between:
                position++;
                result.append(" ").append(fromValue);
                position++;
                result.append(" ").append(toValue);
                break;
        }
        result.append(")");
        return position;
    }
    
}
