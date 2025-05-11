package org.braun.digikam.backend.api.impl;

import jakarta.inject.Inject;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.api.KeywordsApiService;
import org.braun.digikam.backend.api.NotFoundException;
import org.braun.digikam.backend.dao.TagsFacade;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.backend.util.Util;
public class KeywordsApiServiceImpl extends KeywordsApiService {
    
    @Inject private TagsFacade tagsFacade;
    
    @Override
    public Response findKeywordsByName(@NotNull String name, SecurityContext securityContext) throws NotFoundException {
        List<Keyword> result = NodeFactory.getInstance().getKeywordByName(name.toLowerCase());
        return Response.ok().entity(result).build();
    }
    @Override
    public Response refreshKeywordsCache(SecurityContext securityContext) throws NotFoundException {
        TagsFacade tagsFacade = getTagsFacade();
        NodeFactory.getInstance().refresh(tagsFacade.findAll());
        return Response.ok().build();
    }

    public TagsFacade getTagsFacade() {
        if (tagsFacade == null) {
            tagsFacade = Util.Cdi.lookup(TagsFacade.class);
        }
        return tagsFacade;
    }

}
