package org.braun.digikam.backend.api;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-05-07T14:01:44.057970519+02:00[Europe/Berlin]")
public abstract class LensApiService {
    public abstract Response findLensByNameAndMakeAndModel( @NotNull String name, @NotNull String makeAndModel,SecurityContext securityContext) throws NotFoundException;
    public abstract Response refreshLensCache(SecurityContext securityContext) throws NotFoundException;
    public abstract Response getAllCombination(SecurityContext securityContext) throws NotFoundException;
}
