package org.braun.digikam.backend.search;

import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
public class SimpleCondition<T> implements ICondition {

    private final String columnName;
    private final T value;
    private final Operator operator;

    public SimpleCondition(String columnName, T value, Operator operator) {
        this.columnName = columnName;
        this.value = value;
        this.operator = operator;
    }
    
    public SimpleCondition(String columnName, T value) {
        this(columnName, value, Operator.Equals);
    }
    
    
    @Override
    public String getCondition() {
        return columnName + operator.getAsString() + " ?";
    }

    @Override
    public int setParameter(Query query, int position) {
        query.setParameter(++position, value);
        return position;
    }

    @Override
    public boolean isEmpty() {
        return value == null;
    }

    @Override
    public int print(StringBuilder result, int position) {
        result.append("\n P ").append(position).append(" => (");
        result.append(value).append(")");
        position++;
        return position;
    }
    
}
