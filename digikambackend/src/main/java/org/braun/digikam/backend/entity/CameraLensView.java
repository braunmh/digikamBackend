package org.braun.digikam.backend.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author mbraun
 */
@Entity
public class CameraLensView implements Serializable{
    
    @EmbeddedId
    private CameraLensPK id;

    public CameraLensPK getId() {
        return id;
    }

    public void setId(CameraLensPK id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CameraLensView other = (CameraLensView) obj;
        return Objects.equals(this.id, other.id);
    }
    
}
