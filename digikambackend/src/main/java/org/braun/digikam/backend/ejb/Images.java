/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.braun.digikam.backend.ejb;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
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
@Table(name = "Images")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Images.findAll", query = "SELECT i FROM Images i"),
    @NamedQuery(name = "Images.findById", query = "SELECT i FROM Images i WHERE i.id = :id"),
    @NamedQuery(name = "Images.findByStatus", query = "SELECT i FROM Images i WHERE i.status = :status"),
    @NamedQuery(name = "Images.findByCategory", query = "SELECT i FROM Images i WHERE i.category = :category"),
    @NamedQuery(name = "Images.findByModificationDate", query = "SELECT i FROM Images i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "Images.findByFileSize", query = "SELECT i FROM Images i WHERE i.fileSize = :fileSize"),
    @NamedQuery(name = "Images.findByUniqueHash", query = "SELECT i FROM Images i WHERE i.uniqueHash = :uniqueHash"),
    @NamedQuery(name = "Images.findByTagHash", query = "SELECT i FROM Images i WHERE i.tagHash = :tagHash"),
    @NamedQuery(name = "Images.findByManualOrder", query = "SELECT i FROM Images i WHERE i.manualOrder = :manualOrder")})
public class Images implements Serializable {

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
    @Column(name = "name")
    private String name;
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
    private BigInteger fileSize;
    @Size(max = 128)
    @Column(name = "uniqueHash")
    private String uniqueHash;
    @Column(name = "tagHash")
    private Integer tagHash;
    @Column(name = "manualOrder")
    private Integer manualOrder;
    @JoinTable(name = "ImageTags", joinColumns = {
        @JoinColumn(name = "imageid", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "tagid", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Tags> tagsCollection;
    @JoinColumn(name = "album", referencedColumnName = "id")
    @ManyToOne
    private Albums album;
    @OneToMany(mappedBy = "icon")
    private Collection<Tags> tagsCollection1;
    @OneToMany(mappedBy = "icon")
    private Collection<Albums> albumsCollection;

    public Images() {
    }

    public Images(Integer id) {
        this.id = id;
    }

    public Images(Integer id, String name, int status, int category) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BigInteger getFileSize() {
        return fileSize;
    }

    public void setFileSize(BigInteger fileSize) {
        this.fileSize = fileSize;
    }

    public String getUniqueHash() {
        return uniqueHash;
    }

    public void setUniqueHash(String uniqueHash) {
        this.uniqueHash = uniqueHash;
    }

    public Integer getTagHash() {
        return tagHash;
    }

    public void setTagHash(Integer tagHash) {
        this.tagHash = tagHash;
    }

    public Integer getManualOrder() {
        return manualOrder;
    }

    public void setManualOrder(Integer manualOrder) {
        this.manualOrder = manualOrder;
    }

    @XmlTransient
    public Collection<Tags> getTagsCollection() {
        return tagsCollection;
    }

    public void setTagsCollection(Collection<Tags> tagsCollection) {
        this.tagsCollection = tagsCollection;
    }

    public Albums getAlbum() {
        return album;
    }

    public void setAlbum(Albums album) {
        this.album = album;
    }

    @XmlTransient
    public Collection<Tags> getTagsCollection1() {
        return tagsCollection1;
    }

    public void setTagsCollection1(Collection<Tags> tagsCollection1) {
        this.tagsCollection1 = tagsCollection1;
    }

    @XmlTransient
    public Collection<Albums> getAlbumsCollection() {
        return albumsCollection;
    }

    public void setAlbumsCollection(Collection<Albums> albumsCollection) {
        this.albumsCollection = albumsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Images)) {
            return false;
        }
        Images other = (Images) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "org.braun.digikam.backend.ejb.Images[ id=" + id + " ]";
    }
    
}
