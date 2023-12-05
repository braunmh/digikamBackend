package org.braun.digikam.backend.api.impl;

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

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2022-12-15T15:30:51.126+01:00[Europe/Berlin]")
public class LensApiServiceImpl extends LensApiService {
    @Override
    public Response findLensByNameAndMakeAndModel( @NotNull String name,  @NotNull String makeAndModel, SecurityContext securityContext) throws NotFoundException {
        ImageMetadataFacade facade = Util.EJB.lookup(ImageMetadataFacade.class);
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
}
