package org.braun.digikam.web.ui;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.ejb.LabelFacade;
import org.braun.digikam.backend.entity.Label;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author mbraun
 */
@Named("labelBean")
@ViewScoped
public class LabelBean implements Serializable {

    private static final Logger LOG = LogManager.getLogger();

    @Inject
    private LabelFacade labelFacade;
    
    private List<Label> content;

    private List<Label> selectedLabels;
    
    public List<Label> getContent() {
        if (content == null) {
            content = labelFacade.findAll();
        }
        return content;
    }

    public List<Label> getSelectedLabels() {
        return selectedLabels;
    }

    public void setSelectedLabels(List<Label> selectedLabels) {
        this.selectedLabels = selectedLabels;
    }

    public void edit(RowEditEvent<Label> event) {
        Label label = event.getObject();
        if (StringUtils.isBlank(label.getKey()) || 
            StringUtils.isBlank(label.getValue())) {
            setMessage("Schlüssel und Wert dürfen nicht leer sein");
            return;
        }
        Label t = labelFacade.findByKeyLanguageCountry(label.getKey(), label.getLanguage(), label.getCountry());
        if (t != null && !t.getId().equals(label.getId())) {
            setMessage("Label mit " + event.getObject().getKey() + " existiert bereits");
            return;
        }
        if (label.getId() < 0) {
            label.setId(null);
            label = labelFacade.createNew(label);
            for (Label l : getContent()) {
                if (Objects.equals(label.getKey(), l.getKey())
                            && Objects.equals(label.getCountry(), l.getCountry())
                            && Objects.equals(label.getLanguage(), l.getLanguage())) {
                    l.setId(label.getId());
                    break;
                }
            }
        } else { 
            labelFacade.merge(label);
        }
        setMessage("Label " + event.getObject().getKey() + " geändert");
    }
    
    public void delete() {
        if (selectedLabels != null && !selectedLabels.isEmpty()) {
            Iterator<Label> iter = getContent().iterator();
            List<Long> ids = selectedLabels.stream().map(l -> l.getId()).toList();
            while (iter.hasNext()) {
                Label curLabel = iter.next();
                if (ids.contains(curLabel.getId())) {
                    iter.remove();
                    if (curLabel.getId() > 0) {
                        labelFacade.remove(curLabel);
                    }
                }
            }
        }
    }
    
    private void setMessage(String msg) {
        FacesMessage facesMessage = new FacesMessage(msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }
    
    private long tempId = 0;
    public void add() {
        getContent().add(new Label(--tempId));
    }
    
}
