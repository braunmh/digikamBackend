package org.braun.digikam.web.converter;

import jakarta.faces.component.PartialStateHolder;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;

/**
 * Typisierter FacesConverter
 *
 * @author mbraun
 */
public abstract class TypedConverter<TYPE> implements Converter, PartialStateHolder {

    private boolean transientFlag;

    @Override
    public final Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null) {
            return null;
        }
        if (value.isBlank()) {
            return null;
        }
        return getAsType(context, component, value);
    }

    @Override
    public final String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        return getTypeAsString(context, component, (TYPE) value);
    }

    public abstract TYPE getAsType(FacesContext context, UIComponent component, String value);

    public abstract String getTypeAsString(FacesContext context, UIComponent component, TYPE value);

    @Override
    public boolean isTransient() {
        return (transientFlag);
    }

    @Override
    public void setTransient(boolean transientFlag) {
        this.transientFlag = transientFlag;
    }

    private boolean initialState;

    @Override
    public void markInitialState() {
        initialState = true;
    }

    @Override
    public boolean initialStateMarked() {
        return initialState;
    }

    @Override
    public void clearInitialState() {
        initialState = false;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[0];
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
    }
    
}
