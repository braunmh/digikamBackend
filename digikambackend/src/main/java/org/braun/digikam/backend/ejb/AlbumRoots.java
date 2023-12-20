package org.braun.digikam.backend.ejb;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 * @author mbraun
 */
@Entity
@Table(name = "AlbumRoots")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlbumRoots.findAll", query = "SELECT a FROM AlbumRoots a"),
    @NamedQuery(name = "AlbumRoots.findById", query = "SELECT a FROM AlbumRoots a WHERE a.id = :id"),
    @NamedQuery(name = "AlbumRoots.findByStatus", query = "SELECT a FROM AlbumRoots a WHERE a.status = :status"),
    @NamedQuery(name = "AlbumRoots.findByType", query = "SELECT a FROM AlbumRoots a WHERE a.type = :type")})
public class AlbumRoots implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "label")
    private String label;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "type")
    private int type;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "identifier")
    private String identifier;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "specificPath")
    private String specificPath;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "albumRoot")
    private Collection<Albums> albumsCollection;

    public AlbumRoots() {
    }

    public AlbumRoots(Integer id) {
        this.id = id;
    }

    public AlbumRoots(Integer id, int status, int type) {
        this.id = id;
        this.status = status;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSpecificPath() {
        return specificPath;
    }

    public void setSpecificPath(String specificPath) {
        this.specificPath = specificPath;
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
        if (!(object instanceof AlbumRoots)) {
            return false;
        }
        AlbumRoots other = (AlbumRoots) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "org.braun.digikam.backend.ejb.AlbumRoots[ id=" + id + " ]";
    }
    
}
