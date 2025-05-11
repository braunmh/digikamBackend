package org.braun.digikam.web.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author mbraun
 * @param <T> Type of Value
 */
public abstract class Catalogue<T extends Comparable<T>> implements Serializable, Comparable<Catalogue<T>> {
    
    private int id;
    
    private String name;
    
    private T value;

    public Catalogue() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract Catalogue<T> id(int id);
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract Catalogue<T> name(String name);
    
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
    
    public abstract Catalogue<T> value(T value);
    
    @Override
    public int compareTo(Catalogue<T> o) {
        if (value == null && (o == null || o.getValue() == null)) {
            return 0;
        }
        if (o == null || o.getValue() == null) {
            return 1;
        }
        
        if (value == null) {
            return -1;
        }
        
        return value.compareTo(o.getValue());
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Catalogue<?> other = (Catalogue<?>) obj;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public String toString() {
        return "Catalogue{" + "id=" + id + ", name=" + name + ", value=" + value + '}';
    }

    public boolean isEmpty() {
        return id == 0;
    }
        
    public abstract Catalogue<T> copy();
}
