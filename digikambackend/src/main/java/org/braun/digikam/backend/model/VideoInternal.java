package org.braun.digikam.backend.model;

import java.time.LocalDateTime;
import java.util.List;
import org.braun.digikam.backend.graphics.Orientation;

/**
 *
 * @author mbraun
 */
public class VideoInternal extends Video {
    
    transient private Orientation orientationTechnical;
    transient private int orientationExif;

    public Orientation getOrientationTechnical() {
        return orientationTechnical;
    }

    public void setOrientationTechnical(Orientation orientationTechnical) {
        this.orientationTechnical = orientationTechnical;
    }
    
    public VideoInternal orientationTechnical(int angle) {
        orientationTechnical = Orientation.parse(angle);
        return this;
    }

    public int getOrientationExif() {
        return orientationExif;
    }

    public void setOrientationExif(int exif) {
        this.orientationExif = exif;
    }
    
    public VideoInternal orientationExif(int exif) {
        orientationExif = exif;
        return this;
    }

    @Override
    public VideoInternal name(String name) {
        setName(name);
        return this;
    }

    @Override
    public VideoInternal root(String name) {
        setRoot(name);
        return this;
    }
    
    @Override
    public VideoInternal relativePath(String relativePath) {
        setRelativePath(relativePath);
        return this;
    }

    @Override
    public VideoInternal description(String description) {
        setDescription(description);
        return this;
    }

    @Override
    public VideoInternal title(String title) {
        setTitle(title);
        return this;
    }

    @Override
    public VideoInternal keywords(List<Keyword> keywords) {
        setKeywords(keywords);
        return this;
    }

    @Override
    public VideoInternal rating(Integer rating) {
        setRating(rating);
        return this;
    }

    @Override
    public VideoInternal duration(Integer duration) {
        setDuration(duration);
        return this;
    }

    @Override
    public VideoInternal longitude(Double longitude) {
        setLongitude(longitude);
        return this;
    }

    @Override
    public VideoInternal height(Integer height) {
        setHeight(height);
        return this;
    }

    @Override
    public VideoInternal id(Long id) {
        setId(id);
        return this;
    }

    @Override
    public VideoInternal width(Integer width) {
        setWidth(width);
        return this;
    }

    @Override
    public VideoInternal creationDate(LocalDateTime creationDate) {
        setCreationDate(creationDate);
        return this;
    }

    @Override
    public VideoInternal latitude(Double latitude) {
        setLatitude(latitude);
        return this;
    }

    @Override
    public VideoInternal orientation(OrientationEnum orientation) {
        setOrientation(orientation);
        return this;
    }

    @Override
    public VideoInternal creator(String creator) {
        setCreator(creator);
        return this;
    }
}
