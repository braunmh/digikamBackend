package org.braun.digikam.backend.search.sql;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
public abstract class Predicate<T> implements Serializable {
   
   protected String columnName;
   
   protected List<T> values;
   
   protected boolean isNot;

   public String getColumnName() {
      return columnName;
   }

   public void setColumnName(String columnName) {
      this.columnName = columnName;
   }

   public boolean isIsNot() {
      return isNot;
   }

   public void setIsNot(boolean isNot) {
      this.isNot = isNot;
   }
   
   public List<T> getValues() {
      return values;
   }
   
   public abstract String getPredicate();
   
   public abstract int setParameter(Query query, int position);
   
   public abstract int print(StringBuilder result, int position);
}
