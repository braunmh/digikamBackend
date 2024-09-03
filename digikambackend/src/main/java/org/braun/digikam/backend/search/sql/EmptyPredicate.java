package org.braun.digikam.backend.search.sql;

import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
public class EmptyPredicate<T> extends Predicate<T> {

    public EmptyPredicate(String columnName) {
        this.columnName = columnName;
    }
    
    @Override
    public String getPredicate() {
        return columnName + " is null";
    }

    @Override
    public int setParameter(Query query, int position) {
        return position;
    }

    @Override
    public int print(StringBuilder result, int position) {
        return position;
    }
    
}
