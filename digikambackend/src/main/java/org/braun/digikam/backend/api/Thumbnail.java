package org.braun.digikam.backend.api;

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
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author mbraun
 */
@Entity
@Table(name = "Thumbnail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Thumbnail.findAll", query = "SELECT t FROM Thumbnail t"),
    @NamedQuery(name = "Thumbnail.findByImageid", query = "SELECT t FROM Thumbnail t WHERE t.imageid = :imageid"),
    @NamedQuery(name = "Thumbnail.findByModificationDate", query = "SELECT t FROM Thumbnail t WHERE t.modificationDate = :modificationDate"),
    @NamedQuery(name = "Thumbnail.findByOrientation", query = "SELECT t FROM Thumbnail t WHERE t.orientation = :orientation")})
public class Thumbnail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "imageid")
    private Integer imageid;
    @Column(name = "modificationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @Column(name = "orientation")
    private Integer orientation;
    @Lob
    @Column(name = "data")
    private byte[] data;

    public Thumbnail() {
    }

    public Thumbnail(Integer imageid) {
        this.imageid = imageid;
    }

    public Integer getImageid() {
        return imageid;
    }

    public void setImageid(Integer imageid) {
        this.imageid = imageid;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (imageid != null ? imageid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Thumbnail)) {
            return false;
        }
        Thumbnail other = (Thumbnail) object;
        if ((this.imageid == null && other.imageid != null) || (this.imageid != null && !this.imageid.equals(other.imageid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.braun.digikam.backend.api.Thumbnail[ imageid=" + imageid + " ]";
    }
    
}
