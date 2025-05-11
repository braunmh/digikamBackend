package org.braun.digikam.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.backend.model.Media;

/**
 *
 * @author mbraun
 */
public class SearchParameter implements Serializable {
    
    private CatFocalLength focalLengthFrom;
    private CatFocalLength focalLengthTo;

    private CatExposure exposureFrom;
    private CatExposure exposureTo;
    
    private CatRating ratingFrom;
    private CatRating ratingTo;
    
    private CatIso isoFrom;
    private CatIso isoTo;
    
    private CatAperture apertureFrom;
    private CatAperture apertureTo;
    
    private CatOrientation orientation;
    
    private String camera;
    
    private String creator;
    
    private String make;
    
    private String model;
    
    private String lens;
    
    private RangeInteger rating;
    
    private RangeInteger iso;
    
    private RangeDouble exposureTime;
    
    private RangeDouble aperture;
    
    private RangeInteger focalLength;
    
    private RangeDate date;
    
    private List<Keyword> keywords;
    
    private boolean keywordsOr;
    
    private String format;
    
    private boolean ascending;
    
    private List<Media> result;
    
    private List<String> descTitle;
    
    private boolean video = false;

    public void isValid() throws ValidationException {
        if (camera == null || camera.isBlank()) {
            make = null;
            model = null;
        } else {
            String[] parts = camera.split(",");
            make = parts[0].trim();
            if (parts.length > 1) {
                model = parts[1].trim();
            }
        }

        getFocalLength().setFrom(getFocalLengthFrom().getValue());
        getFocalLength().setTo(getFocalLengthTo().getValue());
        
        getRating().setFrom(getRatingFrom().getValue());
        getRating().setTo(getRatingTo().getValue());
        
        getExposureTime().setFrom(getExposureFrom().getValue());
        getExposureTime().setTo(getExposureTo().getValue());
        
        getAperture().setFrom(getApertureFrom().getValue());
        getAperture().setTo(getApertureTo().getValue());
        
        if (isEmpty(creator) && isEmpty(make) && isEmpty(model) && isEmpty(lens) && isEmpty(format)
        && getRating().isEmpty() && getIso().isEmpty() && getExposureTime().isEmpty()
        && getAperture().isEmpty() && getDate().isEmpty() && getFocalLength().isEmpty()
        && getKeywords().isEmpty() && getDescTitle().isEmpty()) {
            throw new ValidationException(null, "Es muss mindestens ein Suchparameter angegeben werden.");
        }
        if (!getRating().isValid()) {
            throw new ValidationException("rating", "Von muss kleiner bis sein.");
        }
        if (!getIso().isValid()) {
            throw new ValidationException("iso", "Von muss kleiner bis sein.");
        }
        if (!getExposureTime().isValid()) {
            throw new ValidationException("exposureTime", "Von muss kleiner bis sein.");
        }
        if (!getAperture().isValid()) {
            throw new ValidationException("aperture", "Von muss kleiner bis sein.");
        }
        if (!getFocalLength().isValid()) {
            throw new ValidationException("focalLength", "Von muss kleiner bis sein.");
        }
        if (!getDate().isValid()) {
            throw new ValidationException("date", "Von muss kleiner bis sein.");
        }
    }
    
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
    }

    public RangeInteger getRating() {
        if (rating == null) {
            rating = new RangeInteger();
        }
        return rating;
    }

    public RangeInteger getIso() {
        if (iso == null) {
            iso = new RangeInteger();
        }
        return iso;
    }

    public RangeDouble getExposureTime() {
        if (exposureTime == null) {
            exposureTime = new RangeDouble();
        }
        return exposureTime;
    }

    public RangeDouble getAperture() {
        if (aperture == null) {
            aperture = new RangeDouble();
        }
        return aperture;
    }

    public RangeInteger getFocalLength() {
        if (focalLength == null) {
            focalLength = new RangeInteger();
        }
        return focalLength;
    }

    public RangeDate getDate() {
        if (date == null) {
            date = new RangeDate();
        }
        return date;
    }

    public List<Keyword> getKeywords() {
        if (keywords == null) {
            keywords = new ArrayList<>();
        }
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public List<String> getDescTitle() {
        if (descTitle == null) {
            descTitle = new ArrayList<>();
        }
        return descTitle;
    }

    public void setDescTitle(List<String> descTitle) {
        this.descTitle = descTitle;
    }
    
    public List<Media> getResult() {
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public CatFocalLength getFocalLengthFrom() {
        if (focalLengthFrom == null) {
            focalLengthFrom = new CatFocalLength();
        }
        return focalLengthFrom;
    }

    public CatFocalLength getFocalLengthTo() {
        if (focalLengthTo == null) {
            focalLengthTo = new CatFocalLength();
        }
        return focalLengthTo;
    }

    public CatExposure getExposureFrom() {
        if (exposureFrom == null) {
            exposureFrom = new CatExposure();
        }
        return exposureFrom;
    }

    public CatExposure getExposureTo() {
        if (exposureTo == null) {
            exposureTo = new CatExposure();
        }
        return exposureTo;
    }

    public CatRating getRatingFrom() {
        if (ratingFrom == null) {
            ratingFrom = new CatRating();
        }
        return ratingFrom;
    }

    public CatRating getRatingTo() {
        if (ratingTo == null) {
            ratingTo = new CatRating();
        }
        return ratingTo;
    }

    public CatAperture getApertureFrom() {
        if (apertureFrom == null) {
            apertureFrom = new CatAperture();
        }
        return apertureFrom;
    }

    public CatAperture getApertureTo() {
        if (apertureTo == null) {
            apertureTo = new CatAperture();
        }
        return apertureTo;
    }

    public CatIso getIsoFrom() {
        if (isoFrom == null) {
            isoFrom = new CatIso();
        }
        return isoFrom;
    }

    public CatIso getIsoTo() {
        if (isoTo == null) {
            isoTo = new CatIso();
        }
        return isoTo;
    }

    public CatOrientation getOrientation() {
        if (orientation == null) {
            orientation = new CatOrientation();
        }
        return orientation;
    }

    public void setIsoFrom(CatIso isoFrom) {
        this.isoFrom = isoFrom;
    }

    public void setFocalLengthFrom(CatFocalLength focalLengthFrom) {
        this.focalLengthFrom = focalLengthFrom;
    }

    public void setFocalLengthTo(CatFocalLength focalLengthTo) {
        this.focalLengthTo = focalLengthTo;
    }

    public void setExposureFrom(CatExposure exposureFrom) {
        this.exposureFrom = exposureFrom;
    }

    public void setExposureTo(CatExposure exposureTo) {
        this.exposureTo = exposureTo;
    }

    public void setRatingFrom(CatRating ratingFrom) {
        this.ratingFrom = ratingFrom;
    }

    public void setRatingTo(CatRating ratingTo) {
        this.ratingTo = ratingTo;
    }

    public void setRating(RangeInteger rating) {
        this.rating = rating;
    }

    public void setApertureFrom(CatAperture apertureFrom) {
        this.apertureFrom = apertureFrom;
    }

    public void setApertureTo(CatAperture apertureTo) {
        this.apertureTo = apertureTo;
    }

    public void setIso(RangeInteger iso) {
        this.iso = iso;
    }

    public void setExposureTime(RangeDouble exposureTime) {
        this.exposureTime = exposureTime;
    }

    public void setAperture(RangeDouble aperture) {
        this.aperture = aperture;
    }

    public void setFocalLength(RangeInteger focalLength) {
        this.focalLength = focalLength;
    }

    public void setDate(RangeDate date) {
        this.date = date;
    }

    public void setOrientation(CatOrientation orientation) {
        this.orientation = orientation;
    }

    public void setIsoTo(CatIso isoTo) {
        this.isoTo = isoTo;
    }

    public boolean isKeywordsOr() {
        return keywordsOr;
    }

    public void setKeywordsOr(boolean keywordsOr) {
        this.keywordsOr = keywordsOr;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

}
