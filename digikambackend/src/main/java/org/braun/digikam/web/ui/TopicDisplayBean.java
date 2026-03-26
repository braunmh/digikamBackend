package org.braun.digikam.web.ui;

import org.braun.digikam.web.model.TopicDisplayView;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.api.NotFoundException;
import org.braun.digikam.backend.ejb.ImageFacade;
import org.braun.digikam.backend.ejb.TopicFacade;
import org.braun.digikam.backend.model.ImageInternal;
import org.braun.digikam.backend.model.Media;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

/**
 *
 * @author mbraun
 */
@Named("topicDisplayBean")
@ViewScoped
public class TopicDisplayBean implements Serializable {
    
    private static final Logger LOG = LogManager.getLogger();
    
    @Inject 
    private SessionUserBean sessionUserBean;
    
    @Inject
    private ImageFacade imageFacade;
    
    @Inject
    private TopicFacade topicFacade;
    
    private TopicDisplayView view;

    private Long id;
    
    public void onLoad() {
        if (view == null) {
            view = topicFacade.findViewById(id);
        }
    }
    
    public void openTopic(ActionEvent event) {
        setId((Long) event.getComponent().getAttributes().get("nodeId"));
        view = null;
        onLoad();
    }
    
    public void openDetailDialog(ActionEvent event) {
        MediaDetailBean.openDialog(getMediaFromEvent(event), sessionUserBean.getInnerWidth());
    }
    
    public void openEditDialog(ActionEvent event) {
        ImageEditBean.openDialog(getMediaFromEvent(event), sessionUserBean.getInnerWidth());
    }
    
    public void openLocationDialog(ActionEvent event) {
        try {
            ImageInternal img = imageFacade.getMetadata(getMediaFromEvent(event).getId());
            if (img.getLatitude() != null && img.getLongitude() != null) {
                //String locationUrlg = String.format("https://www.google.com/maps/search/?api=1&query=%s%%2C%s", img.getLatitude(), img.getLongitude());
                String locationUrl = String.format("https://www.openstreetmap.org/?mlat=%s&mlon=%s", img.getLatitude(), img.getLongitude());
                PrimeFaces.current().executeScript("window.open('" + locationUrl + "', 'map');");
            } else {
                FacesContext.getCurrentInstance().addMessage(
                null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Das Bild besitzt keine Geo-Daten.", ""));
            }
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
        }
    }
    
    private Media getMediaFromEvent(ActionEvent event) {
        return (Media) event.getComponent().getAttributes().get("media");
    }
    
    public TopicDisplayView getView() {
        return view;
    }

    public void setView(TopicDisplayView view) {
        this.view = view;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setId(String id) {
        if (id == null) {
            this.id = null;
        } else {
            this.id = Long.valueOf(id);
        }
    }
    
    
}
