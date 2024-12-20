package org.braun.digikam.web.model;

import java.io.Serializable;

/**
 * @param <TYPE> class implements Comparable  
 *
 * @author mbraun
 */
public abstract class Range<TYPE extends Comparable<TYPE>> implements Serializable {
    
    private TYPE from;
    
    private TYPE to;

    public Range() {
    }
    
    public Range(TYPE from, TYPE to) {
        this.from = from;
        this.to = to;
    }

    public TYPE getFrom() {
        return from;
    }

    public void setFrom(TYPE from) {
        this.from = from;
    }

    public TYPE getTo() {
        return to;
    }

    public void setTo(TYPE to) {
        this.to = to;
    }
    
    public boolean isValid() {
        if (from == null) {
            return true;
        }
        if (to == null) {
            return true;
        }
        return from.compareTo(to) <= 0;
    }
    
    public Range<TYPE> from(TYPE value) {
        from = value;
        return this;
    }
    
    public Range<TYPE> to(TYPE value) {
        to = value;
        return this;
    }
    
    public boolean isEmpty() {
        return from == null && to == null;
    } 
}
