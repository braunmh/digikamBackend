package org.braun.digikam.backend.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
public class Thumbnail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "imageid")
    private Long imageid;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @Column(name = "modificationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    @Column(name = "orientation")
    private Integer orientation;

    public Thumbnail() {
    }

    public Thumbnail(Long imageid) {
        this.imageid = imageid;
    }

    public Long getImageid() {
        return imageid;
    }

    public void setImageid(Long imageid) {
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
        return !((this.imageid == null && other.imageid != null)
            || (this.imageid != null && !this.imageid.equals(other.imageid)));
    }

    @Override
    public String toString() {
        return "org.braun.digikam.backend.api.Thumbnail[ imageid=" + imageid + " ]";
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
    
}
