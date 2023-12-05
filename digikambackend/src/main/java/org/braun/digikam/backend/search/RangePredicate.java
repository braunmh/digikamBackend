package org.braun.digikam.backend.search;

import java.util.ArrayList;
import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
public class RangePredicate<T> extends Predicate<T> {

    public RangePredicate() {
        super();
        T t = null;
        values = new ArrayList<>();
        values.add(t);
        values.add(t);
    }
    
    public RangePredicate(String columnName, T fromValue, T toValue) {
        this();
        setColumnName(columnName);
        setFromValue(fromValue);
        setToValue(toValue);
    }
    
    @Override
    public String getPredicate() {
        return columnName + " BETWEEN ? and ?";
    }

    @Override
    public int setParameter(Query query, int position) {
        query.setParameter(++position, getFromValue());
        query.setParameter(++position, getToValue());
        return position;
    }

    @Override
    public int print(StringBuilder result, int position) {
        result.append("\n P ").append(position).append(" => (");
        position++;
        result.append(" ").append(getFromValue());
        position++;
        result.append(" ").append(getToValue());
        return position;
    }

    public void setFromValue(T fromValue) {
        values.set(0, fromValue);
    }

    public void setToValue(T toValue) {
        values.set(1, toValue);
    }
    
    public T getFromValue() {
        return values.get(0);
    }
    
    public T getToValue() {
        return values.get(1);
    }
    
}
