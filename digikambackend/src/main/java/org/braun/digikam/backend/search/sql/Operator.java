package org.braun.digikam.backend.search.sql;

/**
 *
 * @author mbraun
 */
public enum Operator {

    Equals() {
        @Override
        public String getAsString() {
            return " =";
        }

        @Override
        public boolean hasValue() {
            return true;
        }
    },
    NotEquals() {
        @Override
        public String getAsString() {
            return " !=";
        }

        @Override
        public boolean hasValue() {
            return true;
        }
    },
    LessEquals() {
        @Override
        public String getAsString() {
            return " <=";
        }

        @Override
        public boolean hasValue() {
            return true;
        }
    },
    GreaterEquals() {
        @Override
        public String getAsString() {
            return " >=";
        }

        @Override
        public boolean hasValue() {
            return true;
        }
    },
    Less() {
        @Override
        public String getAsString() {
            return " <";
        }

        @Override
        public boolean hasValue() {
            return true;
        }
    },
    Greater() {
        @Override
        public String getAsString() {
            return " >";
        }

        @Override
        public boolean hasValue() {
            return true;
        }
    },
    IsNull() {
        @Override
        public String getAsString() {
            return " is null";
        }

        @Override
        public boolean hasValue() {
            return false;
        }
    },
    IsNotNull() {
        @Override
        public String getAsString() {
            return " is not null";
        }

        @Override
        public boolean hasValue() {
            return false;
        }
    },
    and() {
        @Override
        public String getAsString() {
            return " AND";
        }

        @Override
        public boolean hasValue() {
            return false;
        }
    },
    or() {
        @Override
        public String getAsString() {
            return " OR";
        }

        @Override
        public boolean hasValue() {
            return false;
        }
    };

    public abstract String getAsString();

    public abstract boolean hasValue();
}
