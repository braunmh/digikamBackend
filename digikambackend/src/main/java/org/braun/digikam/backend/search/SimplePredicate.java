package org.braun.digikam.backend.search;

import java.util.ArrayList;
import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
public class SimplePredicate<T> extends Predicate<T> {

    private Operator operator;
    
    public SimplePredicate() {
        super();
        values = new ArrayList();
        values.add(null);
    }

    public SimplePredicate(String columnName, T value, Operator operator) {
        this();
        this.columnName = columnName;
        this.operator = operator;
    }
    
    @Override
    public String getPredicate() {
        if (operator.hasValue()) {
            return columnName + operator.getAsString() + " ?";
        } else {
            return columnName + operator.getAsString();
        }
    }

    @Override
    public int setParameter(Query query, int position) {
        query.setParameter(++position, getValue());
        return position;
    }

    @Override
    public int print(StringBuilder result, int position) {
        result.append("\n P ").append(position);
        position++;
        result.append(" =>").append(getValue());
        return position;
    } 

    public T getValue() {
        return values.get(0);
    }
    
    public void setValue(T value) {
        values.set(0, value);
    }
}
