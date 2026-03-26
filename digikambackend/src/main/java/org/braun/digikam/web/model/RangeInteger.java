package org.braun.digikam.web.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 *
 * @author mbraun
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class RangeInteger extends Range<Integer> {

    public RangeInteger() {
    }
    
    public RangeInteger(Integer from, Integer to) {
        super(from, to);
    }

    @XmlElement
    private Integer to;
    
    @XmlElement
    private Integer from;
    
    @Override
    public Integer getFrom() {
        return from;
    }

    @Override
    public void setFrom(Integer from) {
        this.from = from;
    }

    @Override
    public Integer getTo() {
        return to;
    }

    @Override
    public void setTo(Integer to) {
        this.to = to;
    }
    
}
