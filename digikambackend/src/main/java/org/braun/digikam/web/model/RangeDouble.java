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
public class RangeDouble extends Range<Double> {

    public RangeDouble() {
    }
    
    public RangeDouble(Double from, Double to) {
        super(from, to);
    }
    
    @XmlElement
    private Double to;
    
    @XmlElement
    private Double from;
    
    @Override
    public Double getFrom() {
        return from;
    }

    @Override
    public void setFrom(Double from) {
        this.from = from;
    }

    @Override
    public Double getTo() {
        return to;
    }

    @Override
    public void setTo(Double to) {
        this.to = to;
    }
}
