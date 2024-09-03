package org.braun.digikam.backend.search.sql;

import java.util.ArrayList;
import java.util.Collection;
import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
public class ListPredicate<T> extends Predicate<T> {

    public ListPredicate(String columnName, Collection<T> values) {
        this.columnName = columnName;
        this.values = new ArrayList<>();
        this.values.addAll(values);
    }
    
    @Override
    public String getPredicate() {
        StringBuilder b = new StringBuilder(columnName);
        b.append(" IN (");
        boolean isFirst = true;
        for (T value : values) {
            if (isFirst) {
                isFirst = false;
            } else {
                b.append(", ");
            }
            b.append(" ?");
        }
        b.append(")");
            
        return b.toString();
    }

    @Override
    public int setParameter(Query query, int position) {
        for (T value : values) {
            query.setParameter(++position, value);
        }
        return position;
    }

    @Override
    public int print(StringBuilder result, int position) {
        result.append("\n P ").append(position).append(" => (");
        for (T value : values) {
            ++position;
            result.append(", ").append(value);
        }
        result.append(")");
        return position;
    }
    
}
