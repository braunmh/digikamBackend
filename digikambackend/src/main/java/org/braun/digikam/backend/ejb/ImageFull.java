package org.braun.digikam.backend.ejb;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mbraun
 */
@Entity
@Table(name = "ImageFull")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImageFull.findById", query = "SELECT i FROM ImageFull i WHERE i.id = :id"),
})
public class ImageFull implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "name")
    private String name;
    
    @Lob
    @Column(name = "root")
    private String root;
    
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "relativePath")
    private String relativePath;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "category")
    private int category;
    @Column(name = "modificationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @Column(name = "fileSize")
    private Integer fileSize;
    @Column(name = "rating")
    private Integer rating;
    @Column(name = "creationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "orientation")
    private Integer orientation;
    @Column(name = "width")
    private Integer width;
    @Column(name = "height")
    private Integer height;
    @Lob
    @Column(name = "make")
    private String make;
    @Lob
    @Column(name = "model")
    private String model;
    @Lob
    @Column(name = "lens")
    private String lens;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "aperture")
    private Double aperture;
    @Column(name = "focalLength")
    private Double focalLength;
    @Column(name = "focalLength35")
    private Double focalLength35;
    @Column(name = "exposureTime")
    private Double exposureTime;
    @Column(name = "exposureProgram")
    private Integer exposureProgram;
    @Column(name = "exposureMode")
    private Integer exposureMode;
    @Column(name = "sensitivity")
    private Integer sensitivity;
    @Lob
    @Column(name = "creator")
    private String creator;
    @Column(name = "latitudeNumber")
    private Double latitudeNumber;
    @Column(name = "longitudeNumber")
    private Double longitudeNumber;

    public ImageFull() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
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

    public Integer getExposureProgram() {
        return exposureProgram;
    }

    public void setExposureProgram(Integer exposureProgram) {
        this.exposureProgram = exposureProgram;
    }

    public Integer getExposureMode() {
        return exposureMode;
    }

    public void setExposureMode(Integer exposureMode) {
        this.exposureMode = exposureMode;
    }

    public Integer getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(Integer sensitivity) {
        this.sensitivity = sensitivity;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Double getLatitudeNumber() {
        return latitudeNumber;
    }

    public void setLatitudeNumber(Double latitudeNumber) {
        this.latitudeNumber = latitudeNumber;
    }

    public Double getLongitudeNumber() {
        return longitudeNumber;
    }

    public void setLongitudeNumber(Double longitudeNumber) {
        this.longitudeNumber = longitudeNumber;
    }
    
}
