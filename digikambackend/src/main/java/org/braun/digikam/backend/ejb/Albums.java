/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.braun.digikam.backend.ejb;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mbraun
 */
@Entity
@Table(name = "Albums")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Albums.findAll", query = "SELECT a FROM Albums a"),
    @NamedQuery(name = "Albums.findById", query = "SELECT a FROM Albums a WHERE a.id = :id"),
    @NamedQuery(name = "Albums.findByDate", query = "SELECT a FROM Albums a WHERE a.date = :date"),
    @NamedQuery(name = "Albums.findByModificationDate", query = "SELECT a FROM Albums a WHERE a.modificationDate = :modificationDate")})
public class Albums implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "relativePath")
    private String relativePath;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "caption")
    private String caption;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "collection")
    private String collection;
    @Column(name = "modificationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @OneToMany(mappedBy = "album")
    private Collection<Images> imagesCollection;
    @JoinColumn(name = "albumRoot", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AlbumRoots albumRoot;
    @JoinColumn(name = "icon", referencedColumnName = "id")
    @ManyToOne
    private Images icon;

    public Albums() {
    }

    public Albums(Integer id) {
        this.id = id;
    }

    public Albums(Integer id, String relativePath) {
        this.id = id;
        this.relativePath = relativePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    @XmlTransient
    public Collection<Images> getImagesCollection() {
        return imagesCollection;
    }

    public void setImagesCollection(Collection<Images> imagesCollection) {
        this.imagesCollection = imagesCollection;
    }

    public AlbumRoots getAlbumRoot() {
        return albumRoot;
    }

    public void setAlbumRoot(AlbumRoots albumRoot) {
        this.albumRoot = albumRoot;
    }

    public Images getIcon() {
        return icon;
    }

    public void setIcon(Images icon) {
        this.icon = icon;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Albums)) {
            return false;
        }
        Albums other = (Albums) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "org.braun.digikam.backend.ejb.Albums[ id=" + id + " ]";
    }
    
}
