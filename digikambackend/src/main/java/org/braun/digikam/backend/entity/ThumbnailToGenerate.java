package org.braun.digikam.backend.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author mbraun
 */
@Entity
public class ThumbnailToGenerate implements Serializable {

    private static final long serialVersionUID = 1L;

    public ThumbnailToGenerate() {
    }

    public ThumbnailToGenerate(Long id) {
        this.id = id;
    }

    @Id
    private Long id;

    @Column(name = "modificationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @Column(name = "orientation")
    private int orientation;
    @Lob
    @Column(name = "data")
    private byte[] data;

    @Column(name = "root")
    String root;
    @Column(name = "name")
    String name;
    @Column(name = "relativePath")
    private String relativePath;

    @Column(name = "width")
    private int width;
    @Column(name = "height")
    private int height;

    @Column(name = "imageId")
    private Long imageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getPath() {
        int endRoot = root.indexOf('&');
        StringBuilder path = new StringBuilder();
        path.append((endRoot > 0) ? root.subSequence(0, endRoot) : root);
        path.append(relativePath).append('/').append(name);
        return path.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ThumbnailToGenerate other = (ThumbnailToGenerate) obj;
        return !(!Objects.equals(this.id, other.id) && (this.id == null || !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return String.format("Entity ThumbnailToGenerate[%s, %s, %s]",id, name, relativePath);
    }

    public Thumbnail getThumbnail() {
        Thumbnail t = new Thumbnail(id);
        if (imageId == null) {
            t.setData(null);
        } else {
            t.setData(new byte[0]);
        }
        t.setModificationDate(modificationDate);
        t.setOrientation(orientation);
        return t;
    }
}
