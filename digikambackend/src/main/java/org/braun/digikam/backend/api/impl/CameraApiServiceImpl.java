package org.braun.digikam.backend.api.impl;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.braun.digikam.backend.CameraFactory;
import org.braun.digikam.backend.api.*;
import org.braun.digikam.backend.ejb.ImageMetadataFacade;
import org.braun.digikam.backend.model.Camera;
import org.braun.digikam.backend.util.Util;

public class CameraApiServiceImpl extends CameraApiService {
    @Override
    public Response findCamerasByMakerAndModel( @NotNull String makeAndModel, SecurityContext securityContext) throws NotFoundException {
        List<Camera> result = CameraFactory.getInstance().findByMakerAndModel(makeAndModel);
        return Response.ok().entity(result).build();
    }

    @Override
    public Response refreshCameraCache(SecurityContext securityContext) throws NotFoundException {
        ImageMetadataFacade facade = Util.EJB.lookup(ImageMetadataFacade.class);
        CameraFactory.getInstance().refresh(facade.findAllCameras());
        return Response.ok().build();
    }
}
