package org.braun.digikam.backend.search;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
public class Sql {
    private final List<ICondition> conditions;
    private final StringBuilder sqlStatement;
    private final List<String> orderClause;
    private final List<SortOrder> orderSort;
    private final List<String> groupClause;
    
    public Sql(String sqlStatement) {
        this.sqlStatement = new StringBuilder(sqlStatement);
        conditions = new ArrayList<>();
        orderClause = new ArrayList<>();
        orderSort = new ArrayList<>();
        groupClause = new ArrayList<>();
    }
    
    public void addOrderClause(String columnName, SortOrder sortOrder) {
        orderClause.add(columnName);
        orderSort.add(sortOrder);
    }
    
    public void addOrderClause(String columnName) {
        orderClause.add(columnName);
        orderSort.add(SortOrder.Default);
    }

    public void addGroupClause(String columnName) {
        groupClause.add(columnName);
    }
    
    public void addJoin(String joinCondition) {
        sqlStatement.append(" ").append(joinCondition);
    }
    
    public void addCondition(ICondition condition) {
        conditions.add(condition);
    }
    
    public Query buildQuery(EntityManager em, Class<?> clazz) {
        Query query = (clazz == null) 
            ? em.createNativeQuery(buildSqlStatement(false).toString())
            : em.createNativeQuery(buildSqlStatement(false).toString(), clazz);
        int position = 0;
        for (ICondition c : conditions) {
            position = c.setParameter(query, position);
        }
        return query;
    }
    
    private StringBuilder buildSqlStatement(boolean isCount) {
        StringBuilder sb = new StringBuilder(sqlStatement);
        if (!conditions.isEmpty()) {
            sb.append(" WHERE ");
            boolean isFirst = true;
            for (ICondition c : conditions) {
                if (c.isEmpty()) {
                    continue;
                }
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append(" AND ");
                }
                sb.append(c.getCondition());
            }
        }
        if (isCount) {
            return sb;
        }
        if (!groupClause.isEmpty()) {
            sb.append(" group by");
            boolean isFirst = true;
            for (String g : groupClause) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append(", ");
                }
                sb.append(" ").append(g);
            }
        }
        if (!orderClause.isEmpty()) {
            sb.append(" order by");
            boolean isFirst = true;
            
            for (int i = 0; i < orderClause.size(); i++) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append(", ");
                }
                
                sb.append(" ").append(orderClause.get(i));
                switch(orderSort.get(i)) {
                    case Descending:
                        sb.append(" DESC");
                        break;
                    case Ascending:
                        sb.append(" ASC");
                }
            }
        }
        
        return sb;
    }

    @Override
    public String toString() {
        StringBuilder sb = buildSqlStatement(false);
        int position = 0;
        for (ICondition c : conditions) {
            position = c.print(sb, position);
        }
        return sb.toString();
    }
    
}
