package org.braun.digikam.web.ui;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.ejb.ImageFacade;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.backend.model.StatisticKeyword;
import org.braun.digikam.backend.search.ConditionParseException;
import org.braun.digikam.web.model.StatisticKeywordParameter;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author mbraun
 */
@Named(value = "statisticKeywordBean")
@ViewScoped
public class StatisticKeywordBean implements Serializable {
    
    private StatisticKeywordParameter content;

    private List<StatisticKeyword> result;
    
    @Inject
    private ImageFacade facade;
    
    @PostConstruct
    public void init() {
        content = new StatisticKeywordParameter();
    }

    public String execute() {
        if (content.isValid()) {
            try {
                result = facade.statKeyword(content.getKeyword().getId(), content.getYear());
            } catch (ConditionParseException e) {
                
            }
        }
        return null;
    }
    
    public List<Keyword> completeKeyword(String query) {
        return NodeFactory.getInstance().getKeywordByFullName(query.toLowerCase());
    }
    
    public StatisticKeywordParameter getContent() {
        return content;
    }

    public void setContent(StatisticKeywordParameter content) {
        this.content = content;
    }

    public List<StatisticKeyword> getResult() {
        return result;
    }

    public void setResult(List<StatisticKeyword> result) {
        this.result = result;
    }
    
}
