package org.braun.digikam.backend.api;

import org.braun.digikam.backend.api.factories.CameraApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.braun.digikam.backend.model.Camera;


import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.validation.constraints.*;

@Path("/camera")


@io.swagger.annotations.Api(description = "the camera API")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-05-07T14:01:44.057970519+02:00[Europe/Berlin]")
public class CameraApi  {
   private final CameraApiService delegate;

   public CameraApi(@Context ServletConfig servletContext) {
      CameraApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("CameraApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (CameraApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = CameraApiServiceFactory.getCameraApi();
      }

      this.delegate = delegate;
   }

    @jakarta.ws.rs.GET
    @Path("/findByNameAndModel")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Camera.class, responseContainer = "List", tags={ "camera", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Camera.class, responseContainer = "List")
    })
    public Response findCamerasByMakerAndModel(@ApiParam(value = "Maker of Camera", required = true) @QueryParam("makeAndModel") @NotNull  String makeAndModel,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.findCamerasByMakerAndModel(makeAndModel, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/refresh")
    
    
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Void.class, tags={ "camera", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Cache refreshed", response = Void.class)
    })
    public Response refreshCameraCache(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.refreshCameraCache(securityContext);
    }
}
