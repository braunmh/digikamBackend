package org.braun.digikam.backend.search;

import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
public class EmptyCondition implements ICondition {
    
    private final Operator operator;
    private final String columnName;

    public EmptyCondition(String columnName, Operator operator) {
        this.operator = operator;
        this.columnName = columnName;
    }
    
    

    @Override
    public String getCondition() {
        return columnName + operator.getAsString();
    }

    @Override
    public int setParameter(Query query, int position) {
        return position;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int print(StringBuilder result, int position) {
        return position;
    }
    
}
