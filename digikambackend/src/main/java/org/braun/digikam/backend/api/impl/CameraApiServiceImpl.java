package org.braun.digikam.backend.api.impl;

import jakarta.inject.Inject;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.braun.digikam.backend.CameraFactory;
import org.braun.digikam.backend.api.*;
import org.braun.digikam.backend.dao.ImageMetadataFacade;
import org.braun.digikam.backend.model.Camera;
import org.braun.digikam.backend.util.Util;

public class CameraApiServiceImpl extends CameraApiService {
    
    @Inject private ImageMetadataFacade facade;
    
    @Override
    public Response findCamerasByMakerAndModel( @NotNull String makeAndModel, SecurityContext securityContext) throws NotFoundException {
        List<Camera> result = CameraFactory.getInstance().findByMakerAndModel(makeAndModel);
        return Response.ok().entity(result).build();
    }

    @Override
    public Response refreshCameraCache(SecurityContext securityContext) throws NotFoundException {
        ImageMetadataFacade facade = getFacade();
        CameraFactory.getInstance().refresh(facade.findAllCameras());
        return Response.ok().build();
    }

    public ImageMetadataFacade getFacade() {
        if (facade == null) {
            facade = Util.Cdi.lookup(ImageMetadataFacade.class);
        }
        return facade;
    }
}
