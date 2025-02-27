package org.braun.digikam.backend.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author mbraun
 */
@Entity
@Table(name = "ImageMetadata")
@XmlRootElement
public class ImageMetadata implements Serializable {
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "imageid")
    private Long imageId;

    @Column(name = "make")
    private String make;
    
    @Column(name = "model")
    private String model;
    
    @Column(name = "lens")
    private String lens;
    
    @Column(name = "aperture")
    private Double aperture;
    
    @Column(name = "focalLength")
    private Double focalLength;
    
    @Column(name = "focalLength35")
    private Double focalLength35;
    
    @Column(name = "exposureTime")
    private Double exposureTime;
    
    @Column(name = "sensitivity")
    private Integer sensitivity;

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
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

    public Double getAperture() {
        return aperture;
    }

    public void setAperture(Double aperture) {
        this.aperture = aperture;
    }

    public Double getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(Double focalLength) {
        this.focalLength = focalLength;
    }

    public Double getFocalLength35() {
        return focalLength35;
    }

    public void setFocalLength35(Double focalLength35) {
        this.focalLength35 = focalLength35;
    }

    public Double getExposureTime() {
        return exposureTime;
    }

    public void setExposureTime(Double exposureTime) {
        this.exposureTime = exposureTime;
    }

    public Integer getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(Integer sensitivity) {
        this.sensitivity = sensitivity;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (imageId != null ? imageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ImageMetadata)) {
            return false;
        }
        ImageMetadata other = (ImageMetadata) object;
        return (!this.imageId.equals(other.imageId));
    }

}
