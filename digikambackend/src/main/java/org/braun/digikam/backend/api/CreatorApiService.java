package org.braun.digikam.backend.api;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-05-07T14:01:44.057970519+02:00[Europe/Berlin]")
public abstract class CreatorApiService {
    public abstract Response findCreatorsByName( @NotNull String name,SecurityContext securityContext) throws NotFoundException;
    public abstract Response refreshCreatorCache(SecurityContext securityContext) throws NotFoundException;
}
