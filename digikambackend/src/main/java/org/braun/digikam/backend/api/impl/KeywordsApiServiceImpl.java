package org.braun.digikam.backend.api.impl;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.api.KeywordsApiService;
import org.braun.digikam.backend.api.NotFoundException;
import org.braun.digikam.backend.ejb.TagsFacade;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.backend.util.Util;
public class KeywordsApiServiceImpl extends KeywordsApiService {
    
    @Override
    public Response findKeywordsByName(@NotNull String name, SecurityContext securityContext) throws NotFoundException {
        List<Keyword> result = NodeFactory.getInstance().getKeywordByName(name.toLowerCase());
        return Response.ok().entity(result).build();
    }
    @Override
    public Response refreshKeywordsCache(SecurityContext securityContext) throws NotFoundException {
        TagsFacade tagsFacade = Util.EJB.lookup(TagsFacade.class);
        NodeFactory.getInstance().refresh(tagsFacade.findAll());
        return Response.ok().build();
    }

}
