package org.braun.digikam.backend.model;

import java.util.Date;
import org.apache.solr.client.solrj.beans.Field;

/**
 *
 * @author mbraun
 */
public class MediaSolr extends AbstractSolr {
    
    @Field
    private String id;
    
    @Field
    private String name;

    @Field
    private int type;
    
    @Field
    private Date creationDate;
    
    @Field
    private double score;

    @Field
    private int width;

    @Field
    private int height;
    
    @Field 
    private int orientation;

    public Media toMedia() {
        return new Media()
            .id(Long.valueOf(id))
            .image(type == 1)
            .creationDate(dateToLocalDateTime(creationDate))
            .name(name)
            .width(width)
            .height(height)
            .score(score)
            .orientation(orientation);
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
