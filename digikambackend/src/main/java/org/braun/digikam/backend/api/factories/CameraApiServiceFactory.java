package org.braun.digikam.backend.api.factories;

import org.braun.digikam.backend.api.CameraApiService;
import org.braun.digikam.backend.api.impl.CameraApiServiceImpl;

public class CameraApiServiceFactory {
    private static final CameraApiService service = new CameraApiServiceImpl();

    public static CameraApiService getCameraApi() {
        return service;
    }
}
