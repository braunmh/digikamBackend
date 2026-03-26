package org.braun.digikam.web.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mbraun
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class CatOrientation extends Catalogue<String> {
    
    public static final List<CatOrientation> values =
        Arrays.asList(
            new CatOrientation().id(0).value(null).name(""),
            new CatOrientation().id(1).value("Landscape").name("Querformat"),
            new CatOrientation().id(5).value("Portrait").name("Hochformat")
        );
    
    @XmlAttribute
    private String value;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public CatOrientation id(int id) {
        setId(id);
        return this;
    }

    @Override
    public CatOrientation name(String name) {
        setName(name);
        return this;
    }

    @Override
    public CatOrientation value(String value) {
        setValue(value);
        return this;
    }

    @Override
    public CatOrientation copy() {
        return new CatOrientation().id(getId()).name(getName()).value(getValue());
    }
}
