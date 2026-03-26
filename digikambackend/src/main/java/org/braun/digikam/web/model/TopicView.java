package org.braun.digikam.web.model;

import java.io.Serializable;
import java.util.Date;
import org.braun.digikam.backend.model.Keyword;

/**
 *
 * @author mbraun
 */
public class TopicView implements Serializable {

    private Long id;
    private Keyword keyword;
    private String title;
    private String content;
    private Integer year;
    private SearchParameter searchParameter;
    private Date begin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TopicView id(Long value) {
        this.id = value;
        return this;
    }
    
    public Keyword getKeyword() {
        if (keyword == null) {
            keyword = new Keyword();
        }
        return keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    public TopicView keyword(Keyword value) {
        this.keyword = value;
        return this;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TopicView title(String value) {
        title = value;
        return this;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TopicView content(String value) {
        content = value;
        return this;
    }
    
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public TopicView year(Integer value) {
        year = value;
        return this;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }
    
    public TopicView begin(Date value) {
        begin = value;
        return this;
    }
    
    public SearchParameter getSearchParameter() {
        if (searchParameter == null) {
            searchParameter = new SearchParameter();
        }
        return searchParameter;
    }

    public void setSearchParameter(SearchParameter searchParameter) {
        this.searchParameter = searchParameter;
    }
    
    
}
