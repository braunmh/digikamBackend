package org.braun.digikam.backend.search.sql;

import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
public abstract class FunctionCondition implements ICondition {

    protected final String columnName;
    protected final Object[] values;
    protected final Operator operator;

    public FunctionCondition(String columnName, Operator operator, Object... values) {
        this.columnName = columnName;
        this.values = values;
        this.operator = operator;
    }
    
    @Override
    public String getCondition() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int setParameter(Query query, int position) {
        for (Object object : values) {
            query.setParameter(++position, object);
        }
        return position;
    }

    @Override
    public boolean isEmpty() {
        return values == null || values.length == 0;
    }

    @Override
    public int print(StringBuilder result, int position) {
        result.append("\n P ").append(position).append(" => (");
        for (Object value : values) {
            ++position;
            result.append(", ").append(value);
        }
        result.append(")");
        return position;
    }
    
    public static class NumToDsInterval extends FunctionCondition {
        public static enum IntervalUnit {
            
            Day() {
                @Override
                public String getDbValue() {
                    return "DAY";
                }
              
            },
            Hour() {
                @Override
                public String getDbValue() {
                    return "HOUR";
                }
              
            },
            Minute() {
                @Override
                public String getDbValue() {
                    return "MINUTE";
                }
              
            },
            Second() {
                @Override
                public String getDbValue() {
                    return "SECOND";
                }
              
            },
            MONTH() {
                @Override
                public String getDbValue() {
                    return "MONTH";
                }
              
            };
            
            public abstract String getDbValue();
        }
        
        private final IntervalUnit intervalUnit;
        private final String operand;

        public NumToDsInterval(String columnName, Operator operator, double values, IntervalUnit intervalUnit) {
            super(columnName, operator, values);
            this.intervalUnit = intervalUnit;
            this.operand = null;
        }

        @Override
        public String getCondition() {
            return columnName + operator.getAsString() + ((null == operand) ? " " : " " + operand)
                + "NUMTODSINTERVAL(?, '" + intervalUnit.getDbValue() + "')";
        }
        
    }
}
