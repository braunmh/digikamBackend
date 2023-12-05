package org.braun.digikam.backend.api.factories;

import org.braun.digikam.backend.api.KeywordsApiService;
import org.braun.digikam.backend.api.impl.KeywordsApiServiceImpl;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2022-12-13T12:05:47.063+01:00[Europe/Berlin]")
public class KeywordsApiServiceFactory {
    private static final KeywordsApiService service = new KeywordsApiServiceImpl();

    public static KeywordsApiService getKeywordsApi() {
        return service;
    }
}
