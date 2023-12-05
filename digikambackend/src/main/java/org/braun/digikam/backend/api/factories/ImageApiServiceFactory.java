package org.braun.digikam.backend.api.factories;

import org.braun.digikam.backend.api.ImageApiService;
import org.braun.digikam.backend.api.impl.ImageApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2022-12-16T20:26:10.127+01:00[Europe/Berlin]")
public class ImageApiServiceFactory {
    private static final ImageApiService service = new ImageApiServiceImpl();

    public static ImageApiService getImageApi() {
        return service;
    }
}
