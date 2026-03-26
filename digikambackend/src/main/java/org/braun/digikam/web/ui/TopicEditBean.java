package org.braun.digikam.web.ui;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.ejb.TopicFacade;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.web.model.CatRating;
import org.braun.digikam.web.model.EntryPerDay;
import org.braun.digikam.web.model.TopicDisplayView;
import org.braun.digikam.web.model.TopicView;
import org.braun.digikam.web.model.ValidationException;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DialogFrameworkOptions;


/**
 *
 * @author mbraun
 */
@Named("topicEditBean")
@ViewScoped
public class TopicEditBean implements DialogBean, Serializable {

    private static final Logger LOG = LogManager.getLogger();
    
    private TopicView content;
    
    private Long id;
    
    private boolean update;
    
    @Inject
    private TopicFacade topicFacade;
    
    public void save() {
        if (isValid()) {
            if (content.getId() == null) {
                topicFacade.insert(content);
            } else {
                topicFacade.update(content);
            }
        }
    }

    public void generate() {
        if (content.getContent() == null || content.getContent().isBlank()) {
            StringBuilder text = new StringBuilder();
            TopicDisplayView view = topicFacade.findViewById(content.getId());
            int i = 0;
            for (EntryPerDay epd : view.getEntries()) {
                String date = new SimpleDateFormat("dd.MM.YYYY").format(epd.getDate());
                text.append("<p>Tag ")
                        .append(i + 1)
                        .append(" (<a shape=\" rect\" href=\" #section").append(i)
                        .append("\" target=\" _blank\" rel=\" nofollow noopener noreferrer\">")
                        .append(date)
                        .append("</a>)</p>");
                i++;
            }
            content.setContent(text.toString());
        }
    }
    
    private boolean isValid() {
        if (StringUtils.isBlank(content.getTitle())) {
            addErrorMessage("title", "Titel muss angegeben werden.");
        }
        if (content.getYear() == 0) {
            addErrorMessage("year", "Jahr muss angegeben werden.");
        }
        try {
            content.getSearchParameter().isValid();
        } catch (ValidationException e) {
            addErrorMessage(e.getField(), e.getMessage());
        }
        return  true;
    }
    
    private void addErrorMessage(String fieldname, String msg) {
        FacesContext.getCurrentInstance().addMessage(fieldname, new FacesMessage(msg));
    }
    
    @Override
    public void onload() {
        if (content == null) {
            if (isUpdate()) {
                content = topicFacade.findById(id);
            } else {
                content = new TopicView();
            }
        }
    }

    @Override
    public void close() {
        PrimeFaces.current().dialog().closeDynamic(true);
    }
    
    public static void openDialog(Long id, boolean update, int innerWidth) {
        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
            .modal(true)
            .fitViewport(true)
            .responsive(true)
            .resizable(true)
            .draggable(false)
            .closeOnEscape(true)
            .width("90%")
            .contentWidth("100%")
            .onShow("hideLoaderWidgetByDialogFramework()")
            .build();

        PrimeFaces.current().dialog().openDynamic("/admin/topicEditDialog", options, 
            DialogParameters.builder()
            .parameter(DialogParameters.Parameter.builder("id").add(id))
            .parameter(DialogParameters.Parameter.builder("update").add(update))
            .build());
    }

    public List<Keyword> completeKeyword(String query) {
        return NodeFactory.getInstance().getKeywordByFullName(query.toLowerCase());
    }

    public List<CatRating> getRatingValues() {
        return CatRating.values;
    }
    
    public TopicView getContent() {
        return content;
    }

    public void setContent(TopicView content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

}
