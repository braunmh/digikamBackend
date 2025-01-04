package org.braun.digikam.web.ui;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import org.braun.digikam.backend.StatusFactory;
import org.braun.digikam.backend.ejb.HouseKeepingFacade;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author mbraun
 */
@Named("adminBean")
@ViewScoped
public class AdminBean implements Serializable {
    
    @Inject
    private HouseKeepingFacade facade;
    
    public boolean isBusy() {
        return StatusFactory.getInstance().getTumbnailGenerationStatus();
    }
    
    public String generate() {
        if (StatusFactory.getInstance().getTumbnailGenerationStatus()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Der Prozess ist bereits angestossen.", "Der Prozess ist bereits angestossen."));
        } else {
            facade.generateTumbnailsTagImages();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Der Prozess wurde angestossen.", "Der Prozess wurde angestossen."));
        }
        return "/admin/generate.xhtml";
    }
}
