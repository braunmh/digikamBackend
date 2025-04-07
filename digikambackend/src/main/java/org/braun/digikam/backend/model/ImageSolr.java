package org.braun.digikam.backend.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.solr.client.solrj.beans.Field;
import org.braun.digikam.backend.NodeFactory;

/**
 *
 * @author mbraun
 */
public class ImageSolr extends AbstractSolr {

    @Field
    private String id;
    
    @Field
    private String creator;

    @Field
    private int orientation;

    @Field
    private Double exposureTime;

    @Field
    private Double focalLength35;

    @Field
    private String lens;

    @Field
    private Date creationDate;

    @Field
    private Double aperture;

    @Field
    private Integer width;

    @Field
    private String model;

    @Field
    private String make;

    @Field
    private Integer height;

    @Field
    private Double focalLength;

    @Field
    private String location;

    @Field
    private Integer iso;

    @Field
    private Integer rating;

    @Field
    private List<Long> keywordIds;

    @Field
    private String title;

    @Field
    private int type;

    @Field
    private String description;

    @Field
    private String path;

    @Field
    private String name;
    
    @Field int format;
    
    public ImageSolr() {
        type = 1;
    }
    
    public ImageSolr(ImageInternal image) {
        aperture = image.getAperture();
        creationDate = (image.getCreationDate() == null) 
            ? null
            : localDateTimeToDate(image.getCreationDate());
        creator = image.getCreator();
        description = image.getDescription();
        exposureTime = image.getExposureTime();
        focalLength = image.getFocalLength();
        focalLength35 = image.getFocalLength35();
        height = image.getHeight();
        width = image.getWidth();
        id = String.valueOf(image.getId());
        iso = image.getIso();
        keywordIds = (image.getKeywords() == null || image.getKeywords().isEmpty()) 
            ? keywordIds = Collections.emptyList()
            : image.getKeywords().stream().map(Keyword::getId).collect(Collectors.toList());
        lens = image.getLens();
        location = getGeoLocation(image.getLatitude(), image.getLongitude());
        name = image.getName();
        make = image.getMake();
        model = image.getModel();
        orientation = (image.getOrientationTechnical() == null) ? 0 : image.getOrientationTechnical().getAngle();
        path = getPathFromImage(image.getRoot(), image.getRelativePath(), image.getName());
        rating = image.getRating();
        title = image.getTitle();
        type = 1;
        format = (width == null || height == null) ? 0 : (width > height) ? 0 : 90;
    }

    public ImageSolr(VideoInternal video) {
        creationDate = (video.getCreationDate() == null) 
            ? null
            : localDateTimeToDate(video.getCreationDate());
        creator = video.getCreator();
        description = video.getDescription();
        height = video.getHeight();
        id = String.valueOf(video.getId());
        keywordIds = (video.getKeywords() == null || video.getKeywords().isEmpty()) 
            ? keywordIds = Collections.emptyList()
            : video.getKeywords().stream().map(Keyword::getId).collect(Collectors.toList());
        location = getGeoLocation(video.getLatitude(), video.getLongitude());
        name = video.getName();
        orientation = (video.getOrientationTechnical() == null) ? 0 : video.getOrientationTechnical().getAngle();
        path = getPathFromImage(video.getRoot(), video.getRelativePath(), video.getName());
        rating = video.getRating();
        title = video.getTitle();
        type = 2;
        width = video.getWidth();
        format = (width == null || height == null) ? 0 : (width > height) ? 0 : 90;
    }

    public ImageInternal getImageInternal() {
        ImageInternal image = new ImageInternal()
            .aperture(aperture)
            .creator(creator)
            .creationDate(dateToLocalDateTime(creationDate))
            .description(description)
            .exposureTime(exposureTime)
            .focalLength(focalLength)
            .focalLength35(focalLength35)
            .height(height)
            .id(Long.valueOf(id))
            .iso(iso)
            .keywords(null)
            .lens(lens)
            .make(make)
            .model(model)
            .name(name)
            .orientationTechnical(orientation)
            .rating(rating)
            .title(title)
            .width(width);
            
        if (path != null) {
            String[] parts = path.split("/");
            image.setRoot("/" + parts[1] + "/" + parts[2]);
            image.setRelativePath("/" + String.join("/", Arrays.asList(Arrays.copyOfRange(parts, 3, parts.length - 1))));
        }
        if (location != null) {
            String[] parts = location.split(",");
            image.setLatitude(stringToDouble(parts[0]));
            image.setLongitude(stringToDouble(parts[1]));
        }
        if (keywordIds == null || keywordIds.isEmpty()) {
            image.setKeywords(Collections.emptyList());
        } else {
            List<Keyword> result = new ArrayList<>();
            for (Long idk : keywordIds) {
                result.add(NodeFactory.getInstance().getKeywordById(idk));
            }
            image.setKeywords(result);
        }

        return image;
    }
    
    private String getPathFromImage(String root, String relativePath, String name) {
        int endRoot = root.indexOf('&');
        StringBuilder temp = new StringBuilder();
        String relPath = (endRoot > 0) 
            ? root.substring(0, endRoot)
            : root;
        temp.append(relPath);
        temp.append(relativePath).append('/').append(name);
        return temp.toString();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public Double getExposureTime() {
        return exposureTime;
    }

    public void setExposureTime(Double exposureTime) {
        this.exposureTime = exposureTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getFocalLength35() {
        return focalLength35;
    }

    public void setFocalLength35(Double focalLength35) {
        this.focalLength35 = focalLength35;
    }

    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Double getAperture() {
        return aperture;
    }

    public void setAperture(Double aperture) {
        this.aperture = aperture;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Double getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(Double focalLength) {
        this.focalLength = focalLength;
    }

    public Integer getIso() {
        return iso;
    }

    public void setIso(Integer iso) {
        this.iso = iso;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<Long> getKeywordIds() {
        return keywordIds;
    }

    public void setKeywordIds(List<Long> keywordIds) {
        this.keywordIds = keywordIds;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

}
