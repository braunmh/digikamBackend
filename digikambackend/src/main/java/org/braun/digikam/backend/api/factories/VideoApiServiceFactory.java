package org.braun.digikam.backend.api.factories;

import org.braun.digikam.backend.api.VideoApiService;
import org.braun.digikam.backend.api.impl.VideoApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-03-01T16:23:06.936844939+01:00[Europe/Berlin]")
public class VideoApiServiceFactory {
    private static final VideoApiService service = new VideoApiServiceImpl();

    public static VideoApiService getVideoApi() {
        return service;
    }
}
