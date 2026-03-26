package org.braun.digikam.web.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * @param <TYPE> class implements Comparable  
 *
 * @author mbraun
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({
    RangeDate.class,
    RangeInteger.class,
    RangeDouble.class
})
public abstract class Range<TYPE extends Comparable<TYPE>> implements Serializable {
    
    public Range() {
    }
    
    public Range(TYPE from, TYPE to) {
        setFrom(from);
        setTo(to);
    }

    public abstract TYPE getFrom();

    public abstract void setFrom(TYPE from);

    public abstract TYPE getTo();

    public abstract void setTo(TYPE to);
    
    public boolean isValid() {
        if (getFrom() == null) {
            return true;
        }
        if (getTo() == null) {
            return true;
        }
        return getFrom().compareTo(getTo()) <= 0;
    }
    
    public Range<TYPE> from(TYPE value) {
        setFrom(value);
        return this;
    }
    
    public Range<TYPE> to(TYPE value) {
        setTo(value);
        return this;
    }
    
    public boolean isEmpty() {
        return getFrom() == null && getTo() == null;
    } 
}
