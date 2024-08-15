package org.braun.digikam.backend.api.impl;

import org.braun.digikam.backend.api.*;
import org.braun.digikam.backend.api.NotFoundException;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-07-27T14:44:22.996590086+02:00[Europe/Berlin]")
public class HouseKeepingApiServiceImpl extends HouseKeepingApiService {
    @Override
    public Response generateThumbnails(SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response generationStatus(SecurityContext securityContext) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
