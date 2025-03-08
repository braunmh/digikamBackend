package org.braun.digikam.web.component.diagram;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponentBase;
/**
 *
 * @author mbraun
 */
@FacesComponent(Diagram.COMPONENT_TYPE)
public class Diagram extends UIComponentBase {
    public static final String COMPONENT_FAMILY = "org.braun.component";
    public static final String COMPONENT_TYPE = "org.braun.component.Diagram";

    public Diagram() {
        super();
        setRendererType(COMPONENT_TYPE);
    }

    protected enum PropertyKeys {
        model, height, width
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    public Integer getHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.height, null);
    }
 
    public void setHeight(Integer value) {
        getStateHelper().put(PropertyKeys.height, value);
    }
    
    public Integer getWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.width, null);
    }
 
    public void setWidth(Integer value) {
        getStateHelper().put(PropertyKeys.width, value);
    }
    
    public DiagramModel getModel() {
        return (DiagramModel) getStateHelper().eval(PropertyKeys.model, null);
    }
 
    public void setModel(Integer value) {
        getStateHelper().put(PropertyKeys.model, value);
    }

    @Override
    public String toString() {
        return "height: " + getHeight() + ", width: " +getWidth() + ", model: " + getModel();
    }

}
