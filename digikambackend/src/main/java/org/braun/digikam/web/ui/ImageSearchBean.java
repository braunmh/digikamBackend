package org.braun.digikam.web.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import org.braun.digikam.backend.ejb.ImageFacade;
import org.braun.digikam.backend.model.Media;
import org.braun.digikam.backend.search.ConditionParseException;
import org.braun.digikam.web.model.SearchParameter;
import org.braun.digikam.web.model.ValidationException;

/**
 *
 * @author mbraun
 */
@Named("imageSearchBean")
@ViewScoped
public class ImageSearchBean implements Serializable {
    
    private SearchParameter searchParameter;
    
    @Inject
    private ImageFacade facade;
    
    @PostConstruct
    public void init() {
        searchParameter = new SearchParameter();
    }

    public String execute() {
        try {
            getSearchParameter().isValid();
            List<Long> keywords = getSearchParameter().getKeywords().stream().map(k -> k.getId()).collect(Collectors.toList());
            List<Media> result = facade.findImagesByImageAttributesSolr(
                    keywords, 
                    true, 
                    getSearchParameter().getCreator(), 
                    getSearchParameter().getMake(), 
                    getSearchParameter().getModel(), 
                    getSearchParameter().getLens(), 
                    "", //getSearchParameter().getOrientation(), 
                    getSearchParameter().getDate().getFrom().getUncompleteDateTime().toString(), 
                    getSearchParameter().getDate().getTo().getUncompleteDateTime().toString(), 
                    getSearchParameter().getRating().getFrom(), 
                    getSearchParameter().getRating().getTo(), 
                    getSearchParameter().getIso().getFrom(), 
                    getSearchParameter().getIso().getTo(), 
                    getSearchParameter().getExposureTime().getFrom(), 
                    getSearchParameter().getExposureTime().getTo(), 
                    getSearchParameter().getAperture().getFrom(), 
                    getSearchParameter().getAperture().getTo(), 
                    getSearchParameter().getFocalLength().getFrom(), 
                    getSearchParameter().getFocalLength().getTo());
            if (!result.isEmpty()) {
                getSearchParameter().getResult().clear();
                getSearchParameter().getResult().addAll(result);
                FacesContext.getCurrentInstance().addMessage(
                    null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Number of objects found is " + result.size(), ""));
                    return null;
            }
        } catch (ValidationException e) {
            FacesContext.getCurrentInstance().addMessage(
                e.getField(), 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
        } catch (ConditionParseException e) {
            FacesContext.getCurrentInstance().addMessage(
                null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
        }
        return null;
    }
    
    public SearchParameter getSearchParameter() {
        return searchParameter;
    }
    
}
