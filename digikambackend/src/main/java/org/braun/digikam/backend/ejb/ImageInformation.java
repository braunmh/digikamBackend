package org.braun.digikam.backend.ejb;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 * @author mbraun
 */
@Entity
@Table(name = "ImageInformation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImageInformation.findByImageId", query = "SELECT i FROM ImageInformation i WHERE i.imageId = :imageId"),
})
public class ImageInformation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "imageid")
    private Integer imageId;
    @Column(name = "rating")
    private Integer rating;
    @Column(name = "creationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "digitizationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date digitizationDate;
    @Column(name = "orientation")
    private Integer orientation;
    @Column(name = "width")
    private Integer width;
    @Column(name = "height")
    private Integer height;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "format")
    private String format;
    @Column(name = "colorDepth")
    private Integer colorDepth;
    @Column(name = "colorModel")
    private Integer colorModel;

    public ImageInformation() {
    }

    public ImageInformation(Integer imageid) {
        this.imageId = imageid;
    }

    public Integer getImageid() {
        return imageId;
    }

    public void setImageid(Integer imageid) {
        this.imageId = imageid;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDigitizationDate() {
        return digitizationDate;
    }

    public void setDigitizationDate(Date digitizationDate) {
        this.digitizationDate = digitizationDate;
    }

    public Integer getOrientation() {
        return orientation;
    }

    public void setOrientation(Integer orientation) {
        this.orientation = orientation;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getColorDepth() {
        return colorDepth;
    }

    public void setColorDepth(Integer colorDepth) {
        this.colorDepth = colorDepth;
    }

    public Integer getColorModel() {
        return colorModel;
    }

    public void setColorModel(Integer colorModel) {
        this.colorModel = colorModel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (imageId != null ? imageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ImageInformation)) {
            return false;
        }
        ImageInformation other = (ImageInformation) object;
        return (!this.imageId.equals(other.imageId));
    }

    @Override
    public String toString() {
        return "org.braun.digikam.backend.ejb.ImageInformation[ imageid=" + imageId + " ]";
    }
    
}
