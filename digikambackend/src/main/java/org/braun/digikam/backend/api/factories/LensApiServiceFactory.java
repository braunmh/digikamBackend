package org.braun.digikam.backend.api.factories;

import org.braun.digikam.backend.api.LensApiService;
import org.braun.digikam.backend.api.impl.LensApiServiceImpl;

public class LensApiServiceFactory {
    private static final LensApiService service = new LensApiServiceImpl();

    public static LensApiService getLensApi() {
        return service;
    }
}
