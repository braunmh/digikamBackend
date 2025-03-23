package org.braun.digikam.web.ui;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.CameraFactory;
import org.braun.digikam.backend.CreatorFactory;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.StatusFactory;
import org.braun.digikam.backend.ejb.HouseKeepingFacade;
import org.braun.digikam.backend.ejb.ImageCopyrightFacade;
import org.braun.digikam.backend.ejb.ImageMetadataFacade;
import org.braun.digikam.backend.ejb.TagsFacade;
import org.braun.digikam.backend.model.Statistic;
import org.braun.digikam.common.LabelResourceBundle;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author mbraun
 */
@Named("adminBean")
@ViewScoped
public class AdminBean implements Serializable {
    
    private static final Logger LOG = LogManager.getLogger();
    
    private Statistic statistics;
    
    @Inject
    private HouseKeepingFacade houseKeepingFacade;
    
    @Inject
    private ImageCopyrightFacade imageCopyrightFacade;
    
    @Inject private ImageMetadataFacade imageMetadatafacade;
    
    @Inject private TagsFacade tagsFacade;
    
    public boolean isBusy() {
        return StatusFactory.getInstance().getTumbnailGenerationStatus();
    }
    
    public String generate() {
        if (StatusFactory.getInstance().getTumbnailGenerationStatus()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Der Prozess ist bereits angestossen.", "Der Prozess ist bereits angestossen."));
        } else {
            houseKeepingFacade.generateTumbnailsTagImages();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Der Prozess wurde angestossen.", "Der Prozess wurde angestossen."));
        }
        return "/admin/generate.xhtml";
    }
    
    public void refreshCaches() {
        CreatorFactory.getInstance().refresh(imageCopyrightFacade.findAllCreators());
        CameraFactory.getInstance().refresh(imageMetadatafacade.findAllCameras());
        NodeFactory.getInstance().refresh(tagsFacade.findAll());
        
        // Implicit refresh in case resourceBundle has changes
        LabelResourceBundle labelResourceBundle = new LabelResourceBundle();
        labelResourceBundle.refresh();
    }

    public Statistic getStatistics() {
        if (statistics == null) {
            statistics = houseKeepingFacade.getStatistics();
        }
        return statistics;
    }
    
}
