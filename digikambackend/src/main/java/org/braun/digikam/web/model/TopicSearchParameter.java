package org.braun.digikam.web.model;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import org.braun.digikam.backend.model.Keyword;

/**
 *
 * @author mbraun
 */
public class TopicSearchParameter implements Serializable {
    
    
    private Integer year;
    
    private Keyword keyword;
    
    private String title;
    
    public boolean isValid() {
        return (year != null && year > 0)
                || (keyword != null && keyword.getId() > 0)
                || (StringUtils.isNotBlank(title));
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
}
