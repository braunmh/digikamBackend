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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Set;
import jakarta.persistence.JoinTable;

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
    })
public class Images implements Serializable {

    private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "id", nullable = false)
   private Integer id;
   @Column(name = "album")
   private Integer album;
   @Basic(optional = false)
   @NotNull
   @Lob
   @Size(min = 1, max = 2147483647)
   @Column(name = "name", nullable = false, length = 2147483647)
   private String name;
   @Basic(optional = false)
   @NotNull
   @Column(name = "status", nullable = false)
   private int status;
   @Basic(optional = false)
   @NotNull
   @Column(name = "category", nullable = false)
   private int category;
   @Column(name = "modificationDate")
   @Temporal(TemporalType.TIMESTAMP)
   private Date modificationDate;
   @Column(name = "fileSize")
   private Integer fileSize;
   @Size(max = 128)
   @Column(name = "uniqueHash", length = 128)
   private String uniqueHash;
   @Column(name = "tagHash")
   private Integer tagHash;
   
   @OneToMany (mappedBy = "image") // mappedBy is the name of Attribute in class ImageComments; not the name of the column
   private Collection<ImageComments> comments;

   @OneToMany (mappedBy = "image")
   private Collection<ImageCopyright> copyrights;
   
   @ManyToMany
   @JoinTable(
           name="ImageTags",
      joinColumns={@JoinColumn(name="imageid", referencedColumnName="id")},
      inverseJoinColumns={@JoinColumn(name="tagid", referencedColumnName="id")}
   )
   private Set<Tags> tags;

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

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
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

    public Integer getAlbum() {
        return album;
    }

    public void setAlbum(Integer album) {
        this.album = album;
    }

    public Collection<ImageComments> getComments() {
        return comments;
    }

    public void setComments(Collection<ImageComments> comments) {
        this.comments = comments;
    }

    public Collection<ImageCopyright> getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(Collection<ImageCopyright> copyrights) {
        this.copyrights = copyrights;
    }

    public Set<Tags> getTags() {
        return tags;
    }

    public void setTags(Set<Tags> tags) {
        this.tags = tags;
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
