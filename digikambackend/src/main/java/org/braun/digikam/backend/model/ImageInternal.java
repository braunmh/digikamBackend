package org.braun.digikam.backend.model;

import java.time.LocalDateTime;
import java.util.List;
import org.braun.digikam.backend.graphics.Orientation;

/**
 *
 * @author mbraun
 */
public class ImageInternal extends Image {
    
    transient private Orientation orientationTechnical;

    public Orientation getOrientationTechnical() {
        return orientationTechnical;
    }

    public void setOrientationTechnical(Orientation orientationTechnical) {
        this.orientationTechnical = orientationTechnical;
    }
    
    public ImageInternal orientationTechnical(int angle) {
        orientationTechnical = Orientation.parse(angle);
        return this;
    }

    @Override
    public ImageInternal name(String name) {
        setName(name);
        return this;
    }

    @Override
    public ImageInternal relativePath(String relativePath) {
        setRelativePath(relativePath);
        return this;
    }

    @Override
    public ImageInternal description(String description) {
        setDescription(description);
        return this;
    }

    @Override
    public ImageInternal title(String title) {
        setTitle(title);
        return this;
    }

    @Override
    public ImageInternal keywords(List<Keyword> keywords) {
        setKeywords(keywords);
        return this;
    }

    @Override
    public ImageInternal rating(Integer rating) {
        setRating(rating);
        return this;
    }

    @Override
    public ImageInternal iso(Integer iso) {
        setIso(iso);
        return this;
    }

    @Override
    public ImageInternal longitude(Double longitude) {
        setLongitude(longitude);
        return this;
    }

    @Override
    public ImageInternal focalLength(Double focalLength) {
        setFocalLength(focalLength);
        return this;
    }

    @Override
    public ImageInternal height(Integer height) {
        setHeight(height);
        return this;
    }

    @Override
    public ImageInternal make(String make) {
        setMake(make);
        return this;
    }

    @Override
    public ImageInternal id(Integer id) {
        setId(id);
        return this;
    }

    @Override
    public ImageInternal model(String model) {
        setModel(model);
        return this;
    }

    @Override
    public ImageInternal width(Integer width) {
        setWidth(width);
        return this;
    }

    @Override
    public ImageInternal aperture(Double aperture) {
        setAperture(aperture);
        return this;
    }

    @Override
    public ImageInternal creationDate(LocalDateTime creationDate) {
        setCreationDate(creationDate);
        return this;
    }

    @Override
    public ImageInternal lens(String lens) {
        setLens(lens);
        return this;
    }

    @Override
    public ImageInternal focalLength35(Double focalLength35) {
        setFocalLength35(focalLength35);
        return this;
    }

    @Override
    public ImageInternal latitude(Double latitude) {
        setLatitude(latitude);
        return this;
    }

    @Override
    public ImageInternal exposureTime(Double exposureTime) {
        setExposureTime(exposureTime);
        return this;
    }

    @Override
    public ImageInternal orientation(OrientationEnum orientation) {
        setOrientation(orientation);
        return this;
    }

    @Override
    public ImageInternal creator(String creator) {
        setCreator(creator);
        return this;
    }
}
