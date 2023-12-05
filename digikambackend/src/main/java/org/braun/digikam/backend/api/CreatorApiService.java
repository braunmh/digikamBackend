package org.braun.digikam.backend.api;

import org.braun.digikam.backend.api.*;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import org.braun.digikam.backend.model.Creator;

import java.util.List;
import org.braun.digikam.backend.api.NotFoundException;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-05-07T14:01:44.057970519+02:00[Europe/Berlin]")
public abstract class CreatorApiService {
    public abstract Response findCreatorsByName( @NotNull String name,SecurityContext securityContext) throws NotFoundException;
    public abstract Response refreshCreatorCache(SecurityContext securityContext) throws NotFoundException;
}