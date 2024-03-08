package org.braun.digikam.backend.ejb;

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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author mbraun
 */
@Entity
@Table(name = "VideoFull")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VideoFull.findAll", query = "SELECT v FROM VideoFull v"),
    @NamedQuery(name = "VideoFull.findById", query = "SELECT v FROM VideoFull v WHERE v.id = :id"),
    @NamedQuery(name = "VideoFull.findByStatus", query = "SELECT v FROM VideoFull v WHERE v.status = :status"),
    @NamedQuery(name = "VideoFull.findByCategory", query = "SELECT v FROM VideoFull v WHERE v.category = :category"),
    @NamedQuery(name = "VideoFull.findByCreationDate", query = "SELECT v FROM VideoFull v WHERE v.creationDate = :creationDate"),
    @NamedQuery(name = "VideoFull.findByFileSize", query = "SELECT v FROM VideoFull v WHERE v.fileSize = :fileSize"),
    @NamedQuery(name = "VideoFull.findByRating", query = "SELECT v FROM VideoFull v WHERE v.rating = :rating"),
    @NamedQuery(name = "VideoFull.findByModificationDate", query = "SELECT v FROM VideoFull v WHERE v.modificationDate = :modificationDate"),
    @NamedQuery(name = "VideoFull.findByOrientation", query = "SELECT v FROM VideoFull v WHERE v.orientation = :orientation"),
    @NamedQuery(name = "VideoFull.findByWidth", query = "SELECT v FROM VideoFull v WHERE v.width = :width"),
    @NamedQuery(name = "VideoFull.findByHeight", query = "SELECT v FROM VideoFull v WHERE v.height = :height"),
    @NamedQuery(name = "VideoFull.findByLatitudeNumber", query = "SELECT v FROM VideoFull v WHERE v.latitudeNumber = :latitudeNumber"),
    @NamedQuery(name = "VideoFull.findByLongitudeNumber", query = "SELECT v FROM VideoFull v WHERE v.longitudeNumber = :longitudeNumber")})
public class VideoFull implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "name")
    private String name;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "root")
    private String root;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
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
    @Column(name = "creationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "fileSize")
    private Integer fileSize;
    @Column(name = "rating")
    private Integer rating;
    @Column(name = "modificationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @Column(name = "orientation")
    private Integer orientation;
    @Column(name = "width")
    private Integer width;
    @Column(name = "height")
    private Integer height;
    @Lob
    @Size(max = 65535)
    @Column(name = "duration")
    private String duration;
    @Lob
    @Size(max = 65535)
    @Column(name = "codec")
    private String codec;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "creator")
    private String creator;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "latitudeNumber")
    private Double latitudeNumber;
    @Column(name = "longitudeNumber")
    private Double longitudeNumber;

    public VideoFull() {
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
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
