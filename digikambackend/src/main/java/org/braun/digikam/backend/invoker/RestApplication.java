package org.braun.digikam.backend.invoker;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationPath(RestResourceRoot.APPLICATION_PATH)
public class RestApplication extends Application {

    private static final List<Class<?>> RESOURCES = Arrays.asList(
        org.braun.digikam.backend.api.CameraApi.class,
        org.braun.digikam.backend.api.CreatorApi.class,
        org.braun.digikam.backend.api.ImageApi.class,
        org.braun.digikam.backend.api.KeywordsApi.class,
        org.braun.digikam.backend.api.LensApi.class,
        org.braun.digikam.backend.api.VideoApi.class);
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>(RESOURCES);
        return resources;
    }

    @Override
    public Map<String, Object> getProperties() {
        return Collections.emptyMap();
    }

}
