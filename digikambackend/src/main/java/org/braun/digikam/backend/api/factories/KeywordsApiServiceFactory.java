package org.braun.digikam.backend.api.factories;

import org.braun.digikam.backend.api.KeywordsApiService;
import org.braun.digikam.backend.api.impl.KeywordsApiServiceImpl;

public class KeywordsApiServiceFactory {
    private static final KeywordsApiService service = new KeywordsApiServiceImpl();

    public static KeywordsApiService getKeywordsApi() {
        return service;
    }
}
