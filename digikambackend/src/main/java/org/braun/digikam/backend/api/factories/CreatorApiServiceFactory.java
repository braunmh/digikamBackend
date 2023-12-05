package org.braun.digikam.backend.api.factories;

import org.braun.digikam.backend.api.CreatorApiService;
import org.braun.digikam.backend.api.impl.CreatorApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2022-12-13T12:05:47.063+01:00[Europe/Berlin]")
public class CreatorApiServiceFactory {
    private static final CreatorApiService service = new CreatorApiServiceImpl();

    public static CreatorApiService getCreatorApi() {
        return service;
    }
}
