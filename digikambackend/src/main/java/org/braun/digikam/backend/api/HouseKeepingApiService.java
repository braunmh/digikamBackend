package org.braun.digikam.backend.api;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-07-27T14:44:22.996590086+02:00[Europe/Berlin]")
public abstract class HouseKeepingApiService {
    public abstract Response generateThumbnails(SecurityContext securityContext) throws NotFoundException;
    public abstract Response generationStatus(SecurityContext securityContext) throws NotFoundException;
    public abstract Response getStatistic(SecurityContext securityContext) throws NotFoundException;
}
