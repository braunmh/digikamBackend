package org.braun.digikam.backend.api;

import org.braun.digikam.backend.api.factories.LensApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.braun.digikam.backend.model.Lens;

import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.validation.constraints.*;

@Path("/lens")


@io.swagger.annotations.Api(description = "the lens API")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-05-07T14:01:44.057970519+02:00[Europe/Berlin]")
public class LensApi  {
   private final LensApiService delegate;

   public LensApi(@Context ServletConfig servletContext) {
      LensApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("LensApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (LensApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = LensApiServiceFactory.getLensApi();
      }

      this.delegate = delegate;
   }

    @jakarta.ws.rs.GET
    @Path("/findByNameAndMakeAndModel")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Lens.class, responseContainer = "List", tags={ "lens", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Lens.class, responseContainer = "List")
    })
    public Response findLensByNameAndMakeAndModel(@ApiParam(value = "Name of Lens", required = true) @QueryParam("name") @NotNull  String name,@ApiParam(value = "Camera (Make and Model)", required = true) @QueryParam("makeAndModel") @NotNull  String makeAndModel,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.findLensByNameAndMakeAndModel(name, makeAndModel, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/refresh")
    
    
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Void.class, tags={ "lens", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Cache refreshed", response = Void.class)
    })
    public Response refreshLensCache(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.refreshLensCache(securityContext);
    }
}
