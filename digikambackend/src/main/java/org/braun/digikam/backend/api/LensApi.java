package org.braun.digikam.backend.api;

import io.swagger.annotations.ApiParam;
import jakarta.inject.Inject;

import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.validation.constraints.*;
import java.util.List;
import org.braun.digikam.backend.CameraLensFactory;
import org.braun.digikam.backend.ejb.ImageMetadataFacade;
import org.braun.digikam.backend.model.CameraLens;
import org.braun.digikam.backend.util.Util;

@Path("/lens")

@io.swagger.annotations.Api(description = "the lens API")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-05-07T14:01:44.057970519+02:00[Europe/Berlin]")
public class LensApi {

    @Inject
    private ImageMetadataFacade facade;

    public LensApi(@Context ServletConfig servletContext) {
    }

    @jakarta.ws.rs.GET
    @Path("/findByNameAndMakeAndModel")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = String.class, responseContainer = "List", tags={ "lens", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = String.class, responseContainer = "List")
    })
    public Response findLensByNameAndMakeAndModel(@ApiParam(value = "Name of Lens", required = true) @QueryParam("name") @NotNull String name, @ApiParam(value = "Camera (Make and Model)", required = true) @QueryParam("makeAndModel") @NotNull String makeAndModel, @Context SecurityContext securityContext)
            throws NotFoundException {
        List<String> result = CameraLensFactory.getInstance().getLensByCameraAndLens(makeAndModel, name);
        return Response.ok().entity(result).build();
    }

    @jakarta.ws.rs.GET
    @Path("/refresh")
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Void.class, tags = {"lens",})
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Cache refreshed", response = Void.class)
    })
    public Response refreshLensCache(@Context SecurityContext securityContext)
            throws NotFoundException {
        CameraLensFactory.getInstance().refresh(facade.getAllCombinations());
        return Response.ok().build();
    }

    @jakarta.ws.rs.GET
    @Path("/getAllCombination")
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Object.class, responseContainer = "List", tags = {"lens",})
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "get all lens / camera combinations", response = Object.class, responseContainer = "List")
    })
    public Response getAllCombination(@Context SecurityContext securityContext)
            throws NotFoundException {
        List<CameraLens> result = CameraLensFactory.getInstance().getAll();
        return Response.ok().entity(result).build();
    }
    
    public ImageMetadataFacade getFacade() {
        if (facade == null) {
            facade = Util.Cdi.lookup(ImageMetadataFacade.class);
        }
        return facade;
    }
}
