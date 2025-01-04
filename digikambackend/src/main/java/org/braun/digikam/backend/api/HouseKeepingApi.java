package org.braun.digikam.backend.api;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.braun.digikam.backend.model.StatusResponse;
import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import java.util.concurrent.Future;
import org.braun.digikam.backend.StatusFactory;
import org.braun.digikam.backend.ejb.HouseKeepingFacade;
@Stateless
@Named
@Path("/houseKeeping")
@io.swagger.annotations.Api(description = "the houseKeeping API")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-07-27T14:44:22.996590086+02:00[Europe/Berlin]")
public class HouseKeepingApi {

    @Inject
    private HouseKeepingFacade houseKeepingFacade;
    
    public HouseKeepingApi() { }
    public HouseKeepingApi(@Context ServletConfig servletContext) {
    }

    @GET
    @Path("/generateThumbnails")

    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Void.class, tags = {"houseKeeping",})
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Generation of Thumbnails submitted", response = Void.class)
    })
    public Response generateThumbnails(@Context SecurityContext securityContext)
        throws NotFoundException {
        if (!StatusFactory.getInstance().getTumbnailGenerationStatus()) {
            Future<Integer> cnt = houseKeepingFacade.generateTumbnailsTagImages();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/generationStatus")

    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = StatusResponse.class, tags = {"houseKeeping",})
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "return wether the generation of thumbnails is currently in process", response = StatusResponse.class)
    })
    public Response generationStatus(@Context SecurityContext securityContext)
        throws NotFoundException {
        StatusResponse response = new StatusResponse().status(StatusFactory.getInstance().getTumbnailGenerationStatus());
        return Response.ok().entity(response).build();
    }
}
