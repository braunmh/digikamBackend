package org.braun.digikam.backend.api.factories;

import org.braun.digikam.backend.api.LensApiService;
import org.braun.digikam.backend.api.impl.LensApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2022-12-15T15:30:51.126+01:00[Europe/Berlin]")
public class LensApiServiceFactory {
    private static final LensApiService service = new LensApiServiceImpl();

    public static LensApiService getLensApi() {
        return service;
    }
}
