package org.braun.digikam.backend.api.impl;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.braun.digikam.backend.CameraFactory;
import org.braun.digikam.backend.api.*;
import org.braun.digikam.backend.ejb.ImageMetadataFacade;
import org.braun.digikam.backend.model.Lens;
import org.braun.digikam.backend.util.Util;

public class LensApiServiceImpl extends LensApiService {
    
    @Inject ImageMetadataFacade facade;
    
    @Override
    public Response findLensByNameAndMakeAndModel( @NotNull String name,  @NotNull String makeAndModel, SecurityContext securityContext) throws NotFoundException {
        ImageMetadataFacade facade = getFacade();
        CameraFactory.CameraEntry entry = CameraFactory.getInstance().getByName(makeAndModel);
        List<String> lenses = facade.getLens(entry.getMake(), entry.getModel());
        List<Lens> result = new ArrayList<>();
        name = name.toLowerCase();
        for (String l : lenses) {
            if (l.toLowerCase().contains(name)) {
                result.add(new Lens().name(l));
            }
        }
        return Response.ok().entity(result).build();
    }
    
    @Override
    public Response refreshLensCache(SecurityContext securityContext) throws NotFoundException {
        return Response.ok().build();
    }

    public ImageMetadataFacade getFacade() {
        if (facade == null) {
            facade = Util.Cdi.lookup(ImageMetadataFacade.class);
        }
        return facade;
    }
    
    
}
