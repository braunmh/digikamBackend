package org.braun.digikam.backend.search;

import jakarta.persistence.Query;

/**
 * Für die Angabe eines Join-Condition innerhalb der where-clause
 * @author mbraun
 */
public class JoinCondition implements ICondition {
    
    private final String columnName1;
    private final String columnName2;
    private final Operator operator;

    public JoinCondition(Operator operator, String columnName1, String columnName2) {
        this.columnName1 = columnName1;
        this.columnName2 = columnName2;
        this.operator = operator;
    }

    @Override
    public String getCondition() {
        return columnName1 + operator.getAsString() + " " + columnName2;
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
