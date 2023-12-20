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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mbraun
 */
@Entity
@Table(name = "ImageComments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImageComments.findAll", query = "SELECT i FROM ImageComments i"),
    @NamedQuery(name = "ImageComments.findByImageId", query = "SELECT i FROM ImageComments i WHERE i.imageId = :imageId"),
    @NamedQuery(name = "ImageComments.findByType", query = "SELECT i FROM ImageComments i WHERE i.type = :type"),
    @NamedQuery(name = "ImageComments.findByLanguage", query = "SELECT i FROM ImageComments i WHERE i.language = :language"),
    @NamedQuery(name = "ImageComments.findByDate", query = "SELECT i FROM ImageComments i WHERE i.date = :date")})
public class ImageComments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "imageid")
    private Integer imageId;
    @Column(name = "type")
    private Integer type;
    @Size(max = 128)
    @Column(name = "language")
    private String language;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "author")
    private String author;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "comment")
    private String comment;

    public ImageComments() {
    }

    public ImageComments(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ImageComments)) {
            return false;
        }
        ImageComments other = (ImageComments) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "org.braun.digikam.backend.ejb.ImageComments[ id=" + id + " ]";
    }
    
}
