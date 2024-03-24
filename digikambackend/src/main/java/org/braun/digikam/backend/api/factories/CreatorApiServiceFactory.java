package org.braun.digikam.backend.api.factories;

import org.braun.digikam.backend.api.CreatorApiService;
import org.braun.digikam.backend.api.impl.CreatorApiServiceImpl;

public class CreatorApiServiceFactory {
    private static final CreatorApiService service = new CreatorApiServiceImpl();

    public static CreatorApiService getCreatorApi() {
        return service;
    }
}
