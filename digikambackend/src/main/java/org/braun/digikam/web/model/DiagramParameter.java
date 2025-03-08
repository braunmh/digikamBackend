package org.braun.digikam.web.model;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import java.io.Serializable;


/**
 *
 * @author mbraun
 */
public class DiagramParameter implements Serializable {
    
    private CatDiagram diagram;
    
    private String camera;

    private String make;
    
    private String model;
    
    private String lens;
    
    public void isValid() throws ValidationException {
        if (camera == null || camera.isBlank()) {
            make = null;
            model = null;
        } else {
            String[] parts = camera.split(",");
            make = parts[0].trim();
            if (parts.length > 1) {
                model = parts[1].trim();
            }
        }
        
        if (isEmpty(camera) && isEmpty(lens) && diagram.isEmpty()) {
            throw new ValidationException(null, "Es muss mindestens ein Suchparameter angegeben werden.");
        }
    }

    public CatDiagram getDiagram() {
        if (diagram == null) {
            diagram = new CatDiagram();
        }
        return diagram;
    }

    public void setDiagram(CatDiagram diagram) {
        this.diagram = diagram;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
    }
}
