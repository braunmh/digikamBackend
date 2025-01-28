package org.braun.digikam.web.model;

import java.io.Serializable;
import org.braun.digikam.backend.model.Keyword;

/**
 *
 * @author mbraun
 */
public class StatisticKeywordParameter implements Serializable {
    
    
    private Integer year;
    
    private Keyword keyword;
    
    public boolean isValid() {
        return year != null && year > 0 && keyword != null && keyword.getId() > 0;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }
    
}
