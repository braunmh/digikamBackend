package org.braun.digikam.web.ui;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.ejb.LabelFacade;
import org.braun.digikam.backend.entity.Label;
import org.braun.digikam.web.component.function.ResourceBundleWrapper;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
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
    
    private Label selected;

    public List<Label> getContent() {
        if (content == null) {
            content = labelFacade.findAll();
        }
        return content;
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
    
    public void deleteLabel(ActionEvent e) {
        Label labelToDelete = (Label) e.getComponent().getAttributes().get("item");
        getContent().removeIf(l -> Objects.equals(l.getId(), labelToDelete.getId()));
        if (labelToDelete.getId() != null && labelToDelete.getId() > 0) {
            labelFacade.remove(labelToDelete);
        }
    }
    
    public void refreshCache() {
        ResourceBundleWrapper.refreshLabel();
    }
    
    public void updateLabel() {
        LOG.info("Update");
        if (isValidSelected()) {
            Label t = labelFacade.findByKeyLanguageCountry(selected.getKey(), null, null);
            if (t != null && !Objects.equals(t.getId(), selected.getId())) {
                setMessage("Schlüssel existiert bereits.");
                return;
            }
            labelFacade.merge(selected);
            PrimeFaces.current().executeScript("PF('labelsWidget').filter(); PF('detailDialogWidget').hide()");
        }
    }

    public void addLabel() {
        LOG.info("Update");
        if (isValidSelected()) {
            Label t = labelFacade.findByKeyLanguageCountry(selected.getKey(), null, null);
            if (t != null) {
                setMessage("Schlüssel existiert bereits.");
                return;
            }
            selected = labelFacade.createNew(selected);
            PrimeFaces.current().executeScript("PF('labelsWidget').filter(); PF('detailDialogWidget').hide()");
        }
     }

    private boolean isValidSelected() {
        if (StringUtils.isBlank(selected.getKey()) || 
            StringUtils.isBlank(selected.getValue())) {
            setMessage("Schlüssel und Wert dürfen nicht leer sein");
            return false;
        }
        return true;
    }
    
    public Label getSelected() {
        return selected;
    }

    public void setSelected(Label selected) {
        this.selected = selected;
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
