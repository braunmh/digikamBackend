package org.braun.digikam.web.model;

import java.io.Serializable;

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
    public String toString() {
        return "Catalogue{" + "id=" + id + ", name=" + name + ", value=" + value + '}';
    }

}
