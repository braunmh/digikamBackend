package org.braun.digikam.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author mbraun
 */
@Embeddable
public class CameraLensPK implements Serializable {
    
    @Column
    private String camera;
    
    @Column
    private String lens;

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.camera);
        hash = 71 * hash + Objects.hashCode(this.lens);
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
        final CameraLensPK other = (CameraLensPK) obj;
        if (!Objects.equals(this.camera, other.camera)) {
            return false;
        }
        return Objects.equals(this.lens, other.lens);
    }
}
