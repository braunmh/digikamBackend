package org.braun.digikam.backend.api;

import org.braun.digikam.backend.api.factories.KeywordsApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.braun.digikam.backend.model.Keyword;

import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.validation.constraints.*;

@Path("/keywords")


@io.swagger.annotations.Api(description = "the keywords API")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-05-07T14:01:44.057970519+02:00[Europe/Berlin]")
public class KeywordsApi  {
   private final KeywordsApiService delegate;

   public KeywordsApi(@Context ServletConfig servletContext) {
      KeywordsApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("KeywordsApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (KeywordsApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = KeywordsApiServiceFactory.getKeywordsApi();
      }

      this.delegate = delegate;
   }

    @jakarta.ws.rs.GET
    @Path("/findByName")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Keyword.class, responseContainer = "List", tags={ "keywords", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Keyword.class, responseContainer = "List")
    })
    public Response findKeywordsByName(@ApiParam(value = "", required = true) @QueryParam("name") @NotNull  String name,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.findKeywordsByName(name, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/refresh")
    
    
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Void.class, tags={ "keywords", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Cache refreshed", response = Void.class)
    })
    public Response refreshKeywordsCache(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.refreshKeywordsCache(securityContext);
    }
}
