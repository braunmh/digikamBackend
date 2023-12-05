package org.braun.digikam.backend.invoker;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@ApplicationPath(RestResourceRoot.APPLICATION_PATH)
public class RestApplication extends Application {

   @Override
   public Set<Class<?>> getClasses() {
      Set<Class<?>> resources = new java.util.HashSet<>();
      addRestResourceClasses(resources);
      return resources;
   }


   @Override
   public Map<String, Object> getProperties() {
      return Collections.emptyMap();
   }

   private void addRestResourceClasses(Set<Class<?>> resources) {
      resources.add(org.braun.digikam.backend.api.CameraApi.class);
      resources.add(org.braun.digikam.backend.api.CreatorApi.class);
      resources.add(org.braun.digikam.backend.api.ImageApi.class);
      resources.add(org.braun.digikam.backend.api.KeywordsApi.class);
      resources.add(org.braun.digikam.backend.api.LensApi.class);
   }
}
