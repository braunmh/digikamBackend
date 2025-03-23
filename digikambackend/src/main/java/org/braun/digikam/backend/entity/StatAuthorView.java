package org.braun.digikam.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author mbraun
 */
@Entity
public class StatAuthorView implements Serializable {
    
    @EmbeddedId
    private PrimaryKey id;
    
    @Column
    private int count;
    
    @Embeddable
    public static class PrimaryKey implements Serializable {
        
        @Column
        private String creator;
        
        @Column
        private String model;
        
        @Column
        private String make;

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + Objects.hashCode(this.creator);
            hash = 79 * hash + Objects.hashCode(this.model);
            hash = 79 * hash + Objects.hashCode(this.make);
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
            final PrimaryKey other = (PrimaryKey) obj;
            if (!Objects.equals(this.creator, other.creator)) {
                return false;
            }
            if (!Objects.equals(this.model, other.model)) {
                return false;
            }
            return Objects.equals(this.make, other.make);
        }
    }

    public PrimaryKey getId() {
        return id;
    }

    public void setId(PrimaryKey id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
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
        final StatAuthorView other = (StatAuthorView) obj;
        return Objects.equals(this.id, other.id);
    }
}
