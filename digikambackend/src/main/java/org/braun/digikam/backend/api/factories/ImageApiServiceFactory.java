package org.braun.digikam.backend.api.factories;

import org.braun.digikam.backend.api.ImageApiService;
import org.braun.digikam.backend.api.impl.ImageApiServiceImpl;

public class ImageApiServiceFactory {
    private static final ImageApiService service = new ImageApiServiceImpl();

    public static ImageApiService getImageApi() {
        return service;
    }
}
