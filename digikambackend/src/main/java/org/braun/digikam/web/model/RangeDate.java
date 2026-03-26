package org.braun.digikam.web.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.braun.digikam.common.DateWrapper;

/**
 *
 * @author mbraun
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class RangeDate extends Range<DateWrapper> {

    public RangeDate() {
    }
    
    public RangeDate(DateWrapper from, DateWrapper to) {
        super(from, to);
    }
    
    @Override
    public boolean isValid() {
        if (getFrom() == null || getFrom().isEmpty()) {
            return true;
        }
        if (getTo() == null || getTo().isEmpty()) {
            return true;
        }
        return getFrom().compareTo(getTo()) <= 0;
    }

    @Override
    public boolean isEmpty() {
        return (getFrom() == null || getFrom().isEmpty()) && (getTo() == null || getTo().isEmpty());
    }
    
    @XmlElement
    private DateWrapper to;
    
    @XmlElement
    private DateWrapper from;
    
    @Override
    public DateWrapper getFrom() {
        return from;
    }

    @Override
    public void setFrom(DateWrapper from) {
        this.from = from;
    }

    @Override
    public DateWrapper getTo() {
        return to;
    }

    @Override
    public void setTo(DateWrapper to) {
        this.to = to;
    }
}
