package org.braun.digikam.backend.search;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
public class CompoundCondition implements ICondition {
    
    private final List<ICondition> conditions;
    private final Operator operator;

    public CompoundCondition() {
        this(Operator.or);
    }
    
    public CompoundCondition(Operator operator) {
        this.conditions = new ArrayList<>();
        this.operator = operator;
    }

    public CompoundCondition(ICondition... conditions) {
        this(Operator.or, conditions);
    }
    public CompoundCondition(Operator operator, ICondition... conditions) {
        this(operator);
        for (ICondition c : conditions) {
            addCondition(c);
        }
    }

    public final void addCondition(ICondition condition) {
        if (!condition.isEmpty()) {
            conditions.add(condition);
        }
    }
    
    @Override
    public String getCondition() {
        if (isEmpty()) {
            return "";
        }
        
        StringBuilder builder = new StringBuilder(" (");
        boolean isFirst = true;
        for (ICondition condition : conditions) {
            if (condition.isEmpty()) {
                continue;
            }
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append(operator.getAsString());
            }
            builder.append(" ").append(condition.getCondition());
        }
        return builder.append(")").toString();
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
        if (!conditions.stream().noneMatch(c -> !c.isEmpty())) {
            return false;
        }
        return true;
    }

    @Override
    public int print(StringBuilder result, int position) {
        for (ICondition condition : conditions) {
            position = condition.print(result, position);
        }
        return position;
    }
    
}
