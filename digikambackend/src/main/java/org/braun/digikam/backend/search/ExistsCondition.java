package org.braun.digikam.backend.search;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
public class ExistsCondition implements ICondition {

    private final boolean isNot;
    private final String sql;
    private final List<ICondition> conditions;

    public ExistsCondition(boolean isNot, String sql) {
        this.isNot = isNot;
        this.sql = sql;
        conditions = new ArrayList<>();
    }
    
    @Override
    public String getCondition() {
        if (isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append((isNot) ? " NOT EXISTS (" : " EXISTS (");
        builder.append(sql);
        builder.append(" WHERE");
        boolean isFirst = true;
        for (ICondition condition : conditions) {
            if (condition.isEmpty()) {
                continue;
            }
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append(Operator.and.getAsString());
            }
            builder.append(" ").append(condition.getCondition());
        }
        return builder.append(")").toString();
    }

    public final void addCondition(ICondition condition) {
        if (!condition.isEmpty()) {
            conditions.add(condition);
        }
    }
    
    @Override
    public int setParameter(Query query, int position) {
        for (ICondition condition : conditions) {
            position = condition.setParameter(query, position);
        }
        return position;
    }

    @Override
    public boolean isEmpty() {
        return conditions.isEmpty();
    }

    @Override
    public int print(StringBuilder result, int position) {
        for (ICondition condition : conditions) {
            position = condition.print(result, position);
        }
        return position;
    }
    
}
