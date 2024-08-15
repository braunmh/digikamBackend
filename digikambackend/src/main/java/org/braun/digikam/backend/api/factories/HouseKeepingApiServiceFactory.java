package org.braun.digikam.backend.api.factories;

import org.braun.digikam.backend.api.HouseKeepingApiService;
import org.braun.digikam.backend.api.impl.HouseKeepingApiServiceImpl;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-07-27T14:44:22.996590086+02:00[Europe/Berlin]")
public class HouseKeepingApiServiceFactory {
    private static final HouseKeepingApiService service = new HouseKeepingApiServiceImpl();

    public static HouseKeepingApiService getHouseKeepingApi() {
        return service;
    }
}
