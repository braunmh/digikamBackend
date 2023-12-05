package org.braun.digikam.backend.api.factories;

import org.braun.digikam.backend.api.CameraApiService;
import org.braun.digikam.backend.api.impl.CameraApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2022-12-13T12:05:47.063+01:00[Europe/Berlin]")
public class CameraApiServiceFactory {
    private static final CameraApiService service = new CameraApiServiceImpl();

    public static CameraApiService getCameraApi() {
        return service;
    }
}
