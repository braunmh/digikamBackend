package org.braun.digikam.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.model.Keyword;

/**
 *
 * @author mbraun
 */
public class ImageEditModel implements Serializable {

    private static final Logger LOG = LogManager.getLogger();

    private List<Keyword> keywords;
    
    private String creator;
    
    private String title;
    
    private String description;
    
    private CatRating rating;

    public List<Keyword> getKeywords() {
        if (keywords == null) {
            keywords = new ArrayList<>();
        }
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CatRating getRating() {
        return rating;
    }

    public void setRating(CatRating rating) {
        this.rating = rating;
    }
}
