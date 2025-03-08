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
public class DiagramView implements Serializable {

    @EmbeddedId
    private PrimaryKey id;

    public PrimaryKey getId() {
        return id;
    }

    public void setId(PrimaryKey id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final DiagramView other = (DiagramView) obj;
        return Objects.equals(this.id, other.id);
    }
    
    @Embeddable
    public static class PrimaryKey implements Serializable {

        @Column
        private String make;
        @Column
        private String model;
        @Column
        private String lens;
        @Column
        private int count;
        @Column
        private Double value;

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getLens() {
            return lens;
        }

        public void setLens(String lens) {
            this.lens = lens;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 79 * hash + Objects.hashCode(this.make);
            hash = 79 * hash + Objects.hashCode(this.model);
            hash = 79 * hash + Objects.hashCode(this.lens);
            hash = 79 * hash + this.count;
            hash = 79 * hash + Objects.hashCode(this.value);
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
            if (this.count != other.count) {
                return false;
            }
            if (!Objects.equals(this.make, other.make)) {
                return false;
            }
            if (!Objects.equals(this.model, other.model)) {
                return false;
            }
            if (!Objects.equals(this.lens, other.lens)) {
                return false;
            }
            return Objects.equals(this.value, other.value);
        }
    }
}
