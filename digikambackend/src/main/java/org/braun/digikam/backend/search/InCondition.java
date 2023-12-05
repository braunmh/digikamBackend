package org.braun.digikam.backend.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import jakarta.persistence.Query;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author mbraun
 */
public class InCondition<T> implements ICondition {

    private final String columnName;
    private final List<T> values;
    private final String sql;
    private final List<ICondition> conditions;

    public InCondition(String columnName, @NotNull Collection<T> values) {
        this.columnName = columnName;
        this.values = new ArrayList<>();
        this.values.addAll(values);
        sql = null;
        conditions = null;
    }

    public InCondition(String columnName, @NotNull T... values) {
        this(columnName, Arrays.asList(values));
    }

    public InCondition(String columnName, SubSelect subSelect) {
        this.columnName = columnName;
        values = Collections.emptyList();
        sql = subSelect.getSql();
        conditions = new ArrayList<>();
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
        StringBuilder builder = new StringBuilder(columnName);
        builder.append(" IN (");

        if (values.isEmpty()) {
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
        } else {
            boolean isFirst = true;
            for (int i=0; i < values.size(); i++) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    builder.append(", ");
                }
                builder.append("?");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public int setParameter(Query query, int position) {
        if (values.isEmpty()) {
            for (ICondition condition : conditions) {
                position = condition.setParameter(query, position);
            }
        } else {
            for (T value : values) {
                query.setParameter(++position, value);
            }
        }
        return position;
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty() && sql == null;
    }

    @Override
    public int print(StringBuilder result, int position) {
        result.append("\n P").append(position).append(" => (");
        if (values.isEmpty()) {
            for (ICondition condition : conditions) {
                position = condition.print(result, position);
            }
        } else {
            for (T value : values) {
                ++position;
                result.append(", ").append(value);
                   
            }
        }
        result.append(")");
        return position;
    }

    public static class SubSelect {

        private final String sql;

        public SubSelect(String sql) {
            this.sql = sql;
        }

        public String getSql() {
            return sql;
        }
    }
}
