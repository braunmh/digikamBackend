package org.braun.digikam.web.ui;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.CameraFactory;
import org.braun.digikam.backend.CameraLensFactory;
import org.braun.digikam.backend.CreatorFactory;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.api.NotFoundException;
import org.braun.digikam.backend.ejb.ImageFacade;
import org.braun.digikam.backend.model.ImageInternal;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.backend.model.Media;
import org.braun.digikam.backend.search.ConditionParseException;
import org.braun.digikam.web.model.CatAperture;
import org.braun.digikam.web.model.CatExposure;
import org.braun.digikam.web.model.CatFocalLength;
import org.braun.digikam.web.model.CatIso;
import org.braun.digikam.web.model.CatOrientation;
import org.braun.digikam.web.model.CatRating;
import org.braun.digikam.web.model.MediaDateComparator;
import org.braun.digikam.web.model.MediaScoreComparator;
import org.braun.digikam.web.model.SearchParameter;
import org.braun.digikam.web.model.ValidationException;

/**
 *
 * @author mbraun
 */
@Named(value ="imageSearchBean")
@SessionScoped
public class ImageSearchBean implements Serializable {
    
    private static final Logger LOG = LogManager.getLogger();
    
    private SearchParameter searchParameter;
    
    private List<EntryPerMonth> entriesPerMonth;
    private final transient DateTimeFormatter isoDate = DateTimeFormatter.ofPattern("MM.yyyy");
    
    @Inject
    private ImageFacade facade;
    
    @PostConstruct
    public void init() {
        searchParameter = new SearchParameter();
        searchParameter.setAscending(true);
        searchParameter.setKeywordsOr(false);
    }

    public String execute() {
        try {
            getSearchParameter().isValid();
            List<Long> keywords = getSearchParameter().getKeywords().stream().map(k -> k.getId()).collect(Collectors.toList());
            List<Media> result = facade.findImagesByImageAttributesSolr(
                    keywords, 
                    getSearchParameter().isKeywordsOr(), 
                    getSearchParameter().getCreator(), 
                    getSearchParameter().getMake(), 
                    getSearchParameter().getModel(), 
                    getSearchParameter().getLens(), 
                    getSearchParameter().getOrientation().getValue(), 
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
            if (result.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(
                    null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Es wurden keine Bilder gefunden", null));
            } 
            else {
                getSearchParameter().getResult().clear();
                if (getSearchParameter().isKeywordsOr()) {
                    Collections.sort(result, new MediaScoreComparator(getSearchParameter().isAscending()));
                } else {
                    Collections.sort(result, new MediaDateComparator(getSearchParameter().isAscending()));
                }
                getSearchParameter().getResult().addAll(result);
                FacesContext.getCurrentInstance().addMessage(
                    null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Number of objects found is " + result.size(), ""));
                LocalDateTime first = toFirst(result.get(0).getCreationDate());
                LocalDateTime last = toLast(result.get(0).getCreationDate());
                entriesPerMonth = new ArrayList<>();
                entriesPerMonth.add(new EntryPerMonth().date(isoDate.format(first)));
                int current = 0;
                for (Media media : result) {
                    if (media.getCreationDate().compareTo(first) >= 0 
                        && media.getCreationDate().compareTo(last) <= 0) {
                        entriesPerMonth.get(current).getEntries().add(media);
                    } else {
                        current++;
                        first = toFirst(media.getCreationDate());
                        last = toLast(media.getCreationDate());
                        entriesPerMonth.add(new EntryPerMonth().date(isoDate.format(first)));
                        entriesPerMonth.get(current).getEntries().add(media);
                    }
                }
                String outCome = "/search/result.xhtml";
                return outCome;
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
    
    private LocalDateTime toFirst(LocalDateTime value) {
        return value.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }
    
    private LocalDateTime toLast(LocalDateTime value) {
        LocalDateTime tmp = value.withDayOfMonth(1).withHour(23).withMinute(59).withSecond(59).withNano(999);
        return tmp.plusMonths(1).minusDays(1);
    }
    
    public ImageInternal getMetadata(long imageId) {
        try {
            return facade.getMetadata(imageId);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }
    
    public SearchParameter getSearchParameter() {
        return searchParameter;
    }
    
    public List<Keyword> completeKeyword(String query) {
        return NodeFactory.getInstance().getKeywordByFullName(query.toLowerCase());
    }
    
    public List<String> completeCamera(String query) {
        return CameraFactory.getInstance().findByMakerAndModel(query).stream().map(c -> c.getName()).toList();
    }
    
    public List<String> completeCreator(String query) {
        return CreatorFactory.getInstance().findByName(query).stream().map(c -> c.getName()).toList();
    }
    
    public List<String> completeLens(String query) {
        return CameraLensFactory.getInstance().getLensByCameraAndLens(searchParameter.getCamera(), query);
    }
    
    public void changeAction(AjaxBehaviorEvent event) {
        LOG.info("Values changed by autocomplete.ajax. ClientId: {}, Id: {}", 
            event.getComponent().getClientId(), event.getComponent().getId());
    }
    
    public List<CatFocalLength> getFocalLengthValues() {
        return CatFocalLength.values;
    }
    
    public List<CatExposure> getExposureValues() {
        return CatExposure.values;
    }
    
    public List<CatRating> getRatingValues() {
        return CatRating.values;
    }
    
    public List<CatIso> getIsoValues() {
        return CatIso.values;
    }
    
    public List<CatAperture> getApertureValues() {
        return CatAperture.values;
    }
    
    public List<CatOrientation> getOrientationValues() {
        return CatOrientation.values;
    }
    
    public List<EntryPerMonth> getEntriesPerMonth() {
        return entriesPerMonth;
    }
    
    public class EntryPerMonth {

        private final List<Media> entries;
        
        private String date;

        public EntryPerMonth() {
            entries = new ArrayList<>();
        }

        public List<Media> getEntries() {
            return entries;
        }

        public String getDate() {
            return date;
        }
        
        public EntryPerMonth date(String value) {
            date = value;
            return this;
        }
    }
    
}
