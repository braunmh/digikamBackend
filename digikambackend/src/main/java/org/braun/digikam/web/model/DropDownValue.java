package org.braun.digikam.web.model;

import java.io.Serializable;

/**
 *
 * @author mbraun
 * @param <TYPE> of value
 */
public abstract class DropDownValue<TYPE, CLASS extends DropDownValue<TYPE, CLASS>> implements Serializable {
    
    private String name;
    
    private TYPE value;

    public DropDownValue() {
    }

    public String getName() {
        return name;
    }

    public TYPE getValue() {
        return value;
    }

    public abstract CLASS name(String value);

    public abstract CLASS value(TYPE value);
    
    public CLASS valueName(TYPE value) {
        this.name = format(value) + getUnit();
        return value(value);
    }
    
    protected abstract String format(TYPE value);
    
    protected String getUnit() {
        return "";
    }
    
    protected void setValue(TYPE value) {
        this.value = value;
    } 

    protected void setName(String value) {
        this.name = value;
    } 

    @Override
    public String toString() {
        return "value=" + value + ", name=" + name;
    }
    
    public static class FocalLenthValue extends DropDownValue<Integer, FocalLenthValue> {

        public FocalLenthValue() {
            super();
        }

        @Override
        protected String format(Integer value) {
            return String.valueOf(value);
        }
        
        @Override
        protected String getUnit() {
            return " m";
        }


        @Override
        public FocalLenthValue value(Integer value) {
            setValue(value);
            return this;
        }

        @Override
        public FocalLenthValue name(String value) {
            setName(value + getUnit());
            return this;
        }
        
    }

    public static class IntegerValue extends DropDownValue<Integer, IntegerValue> {

        public IntegerValue() {
            super();
        }

        @Override
        protected String format(Integer value) {
            return String.valueOf(value);
        }

        @Override
        public IntegerValue name(String value) {
            setName(value);
            return this;
        }

        @Override
        public IntegerValue value(Integer value) {
            setValue(value);
            return this;
        }

    }

    public static class DoubleInversValue extends DropDownValue<Double, DoubleInversValue> {

        public DoubleInversValue() {
            super();
        }

        @Override
        public DoubleInversValue value(Double value) {
            setValue(1d / value);
            return this;
        }

        @Override
        protected String format(Double value) {
            return String.valueOf(value.longValue());
        }

        @Override
        public DoubleInversValue name(String value) {
            setName(value);
            return this;
        }

    }
    
    public static class ExposureValue extends DropDownValue<Double, ExposureValue> {

        @Override
        public ExposureValue name(String value) {
            setName(value);
            return this;
        }

        @Override
        public ExposureValue value(Double value) {
            if (value > 0) {
                setValue(1/value);
            } else {
                setValue(0.0);
            }
            return this;
        }

        @Override
        protected String format(Double value) {
            return getName();
        }
        
    }
}
