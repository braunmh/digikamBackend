package org.braun.digikam.backend.api.impl;

import jakarta.inject.Inject;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.braun.digikam.backend.CreatorFactory;
import org.braun.digikam.backend.api.*;
import org.braun.digikam.backend.dao.ImageCopyrightDao;
import org.braun.digikam.backend.model.Creator;
import org.braun.digikam.backend.util.Util;

public class CreatorApiServiceImpl extends CreatorApiService {
    
    @Inject ImageCopyrightDao facade;
    @Override
    public Response findCreatorsByName( @NotNull String name, SecurityContext securityContext) throws NotFoundException {
        List<Creator> creators = CreatorFactory.getInstance().findByName(name);
        return Response.ok().entity(creators).build();
    }
    
    @Override
    public Response refreshCreatorCache(SecurityContext securityContext) throws NotFoundException {
        ImageCopyrightDao facade = getFacade();
        CreatorFactory.getInstance().refresh(facade.findAllCreators());
        return Response.ok().build();
    }

    public ImageCopyrightDao getFacade() {
        if (facade == null) {
            facade = Util.Cdi.lookup(ImageCopyrightDao.class);
        }
        return facade;
    }
}
