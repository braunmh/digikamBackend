package org.braun.digikam.backend.search;

import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
public interface ICondition {

    public static final char ESCAPE_CHARACTER = '\\';
    
   String getCondition();
   
   int setParameter(Query query, int position);
   
   boolean isEmpty();
   
   int print(StringBuilder result, int position);
   
}
