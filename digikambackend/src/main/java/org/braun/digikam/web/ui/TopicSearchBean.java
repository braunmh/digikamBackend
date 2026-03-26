package org.braun.digikam.web.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.ejb.TopicFacade;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.web.model.TopicSearchParameter;
import org.braun.digikam.web.model.TopicView;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author mbraun
 */
@Named("topicSearchBean")
@ViewScoped
public class TopicSearchBean implements Serializable {
    
    private TopicSearchParameter content;
    
    private List<TopicView> topics;
    
    @Inject
    private TopicFacade topicFacade;
    
    @Inject
    private SessionUserBean sessionUserBean;
    
    @PostConstruct
    public void init() {
        content = new TopicSearchParameter();
    }
    
    public String execute() {
        topics = topicFacade.findByAttributes(content.getTitle(), content.getKeyword(), content.getYear());
        return null;
    }
    
    public void openInsertDialog(ActionEvent event) {
        TopicEditBean.openDialog(0L, false, sessionUserBean.getInnerWidth());
    }

    public void openEditDialog(ActionEvent event) {
        Long id = (Long) event.getComponent().getAttributes().get("topicId");
        TopicEditBean.openDialog(id, true, sessionUserBean.getInnerWidth());
    }

    public TopicSearchParameter getContent() {
        return content;
    }

    public void setContent(TopicSearchParameter content) {
        this.content = content;
    }

    public List<TopicView> getTopics() {
        return topics;
    }

    public List<Keyword> completeKeyword(String query) {
        return NodeFactory.getInstance().getKeywordByFullName(query.toLowerCase());
    }

    
}
