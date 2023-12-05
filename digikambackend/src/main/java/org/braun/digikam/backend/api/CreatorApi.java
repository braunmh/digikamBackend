package org.braun.digikam.backend.api;

import org.braun.digikam.backend.api.CreatorApiService;
import org.braun.digikam.backend.api.factories.CreatorApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import org.braun.digikam.backend.model.Creator;

import java.util.Map;
import java.util.List;
import org.braun.digikam.backend.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Path("/creator")


@io.swagger.annotations.Api(description = "the creator API")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-05-07T14:01:44.057970519+02:00[Europe/Berlin]")
public class CreatorApi  {
   private final CreatorApiService delegate;

   public CreatorApi(@Context ServletConfig servletContext) {
      CreatorApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("CreatorApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (CreatorApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = CreatorApiServiceFactory.getCreatorApi();
      }

      this.delegate = delegate;
   }

    @jakarta.ws.rs.GET
    @Path("/findByName")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Find Creator of an Image by Name", notes = "", response = Creator.class, responseContainer = "List", tags={ "creator", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Creator.class, responseContainer = "List"),
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid status value", response = Void.class)
    })
    public Response findCreatorsByName(@ApiParam(value = "", required = true) @QueryParam("name") @NotNull  String name,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.findCreatorsByName(name, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/refresh")
    
    
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Void.class, tags={ "creator", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Cache refreshed", response = Void.class)
    })
    public Response refreshCreatorCache(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.refreshCreatorCache(securityContext);
    }
}
