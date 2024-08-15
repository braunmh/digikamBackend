package org.braun.digikam.backend.entity;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mbraun
 */
@Entity
@Table(name = "ImageCopyright")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImageCopyright.findAll", query = "SELECT i FROM ImageCopyright i")})
public class ImageCopyright implements Serializable {

    @Lob
    @javax.validation.constraints.Size(max = 2147483647)
    @Column(name = "property")
    private String property;
    @Lob
    @javax.validation.constraints.Size(max = 2147483647)
    @Column(name = "value")
    private String value;
    @Lob
    @javax.validation.constraints.Size(max = 2147483647)
    @Column(name = "extraValue")
    private String extraValue;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "imageid", referencedColumnName = "id") // the names of the columns (foreign key in ImageComments and primary key in Images
    private Images image;
    
    public ImageCopyright() {
    }

    public ImageCopyright(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ImageCopyright)) {
            return false;
        }
        ImageCopyright other = (ImageCopyright) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "org.braun.digikam.backend.ejb.ImageCopyright[ id=" + id + " ]";
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }

    public Images getImage() {
        return image;
    }

    public void setImage(Images image) {
        this.image = image;
    }
    
}
