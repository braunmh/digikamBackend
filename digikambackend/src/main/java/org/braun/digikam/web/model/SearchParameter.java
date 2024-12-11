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
    
    private List<Media> result;

    public void isValid() throws ValidationException {
        if (isEmpty(creator) && isEmpty(make) && isEmpty(model) && isEmpty(lens)
        && getRating().isEmpty() && getIso().isEmpty() && getExposureTime().isEmpty()
        && getAperture().isEmpty() && getDate().isEmpty() && getFocalLength().isEmpty()
        && getKeywords().isEmpty()) {
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

    public List<Media> getResult() {
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

}
