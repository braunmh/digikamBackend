package org.braun.digikam.backend.api;

import org.braun.digikam.backend.api.factories.ImageApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.braun.digikam.backend.model.Image;
import org.braun.digikam.backend.model.ImageUpdate;
import org.braun.digikam.backend.model.Media;
import org.braun.digikam.backend.model.StatisticKeyword;
import org.braun.digikam.backend.model.StatisticMonth;

import java.util.List;


import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Path("/image")


@io.swagger.annotations.Api(description = "the image API")
public class ImageApi  {
   private final ImageApiService delegate;

   public ImageApi(@Context ServletConfig servletContext) {
      ImageApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("ImageApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (ImageApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = ImageApiServiceFactory.getImageApi();
      }

      this.delegate = delegate;
   }

    @jakarta.ws.rs.GET
    @Path("/find")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Media.class, responseContainer = "List", tags={ "image", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Media.class, responseContainer = "List")
    })
    public Response findImagesByImageAttributes(@ApiParam(value = "List of Keywords") @QueryParam("keywords")  List<Long> keywords,@ApiParam(value = "Keywords-search combined with and-operator") @QueryParam("keywordsOr")  Boolean keywordsOr,@ApiParam(value = "") @QueryParam("creator")  String creator,@ApiParam(value = "") @QueryParam("makeModel")  String makeModel,@ApiParam(value = "") @QueryParam("lens")  String lens,@ApiParam(value = "", allowableValues="Portrait, Landscape") @QueryParam("orientation")  String orientation,@ApiParam(value = "") @QueryParam("date_from")  String dateFrom,@ApiParam(value = "") @QueryParam("date_to")  String dateTo,@ApiParam(value = "") @QueryParam("ratingFrom")  Integer ratingFrom,@ApiParam(value = "") @QueryParam("ratingTo")  Integer ratingTo,@ApiParam(value = "") @QueryParam("isoFrom")  Integer isoFrom,@ApiParam(value = "") @QueryParam("isoTo")  Integer isoTo,@ApiParam(value = "") @QueryParam("exposureTimeFrom")  Double exposureTimeFrom,@ApiParam(value = "") @QueryParam("exposureTimeTo")  Double exposureTimeTo,@ApiParam(value = "") @QueryParam("apertureFrom")  Double apertureFrom,@ApiParam(value = "") @QueryParam("apertureTo")  Double apertureTo,@ApiParam(value = "") @QueryParam("focalLengthFrom")  Integer focalLengthFrom,@ApiParam(value = "") @QueryParam("focalLengthTo")  Integer focalLengthTo,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.findImagesByImageAttributes(keywords, keywordsOr, creator, makeModel, lens, orientation, dateFrom, dateTo, ratingFrom, ratingTo, isoFrom, isoTo, exposureTimeFrom, exposureTimeTo, apertureFrom, apertureTo, focalLengthFrom, focalLengthTo, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/{imageId}")
    
    @Produces({ "image/jpeg" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = byte[].class, tags={ "image", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = byte[].class)
    })
    public Response getImage(@ApiParam(value = "Id of image to return", required = true) @PathParam("imageId") @NotNull  Long imageId,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getImage(imageId, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/getInformation")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Image.class, tags={ "image", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Metadata of Image with Id", response = Image.class)
    })
    public Response getInformationAboutImage(@ApiParam(value = "Id identifing an Image") @QueryParam("imageId")  Long imageId,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getInformationAboutImage(imageId, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/status")
    
    
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Void.class, tags={ "image", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Cache refreshed", response = Void.class)
    })
    public Response imageStatus(@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.imageStatus(securityContext);
    }
    @jakarta.ws.rs.POST
    @Path("/update")
    @Consumes({ "application/json" })
    
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Void.class, tags={ "image", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Update image successful", response = Void.class)
    })
    public Response imageUpdate(@ApiParam(value = "one parameter must be provided", required = true) @NotNull @Valid  ImageUpdate imageUpdate,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.imageUpdate(imageUpdate, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/rate")
    
    
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Void.class, tags={ "image", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Void.class)
    })
    public Response rateImage(@ApiParam(value = "Id of image to rate", required = true) @QueryParam("imageId") @NotNull  Long imageId,@ApiParam(value = "The new rating", required = true) @QueryParam("rating") @NotNull  Integer rating,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.rateImage(imageId, rating, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/scale")
    
    @Produces({ "image/jpeg" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = byte[].class, tags={ "image", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = byte[].class)
    })
    public Response scalesImage(@ApiParam(value = "Id of Image to scale", required = true) @QueryParam("imageId") @NotNull  Long imageId,@ApiParam(value = "new width of scaled Image", required = true) @QueryParam("width") @NotNull  Integer width,@ApiParam(value = "new height of scaled Image", required = true) @QueryParam("height") @NotNull  Integer height,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.scalesImage(imageId, width, height, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/statKeyword")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = StatisticKeyword.class, responseContainer = "List", tags={ "image", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = StatisticKeyword.class, responseContainer = "List")
    })
    public Response statKeyword(@ApiParam(value = "Id of Keyord", required = true) @QueryParam("keywordId") @NotNull  Long keywordId,@ApiParam(value = "Year", required = true) @QueryParam("year") @NotNull  Integer year,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.statKeyword(keywordId, year, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/statMonth")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = StatisticMonth.class, responseContainer = "List", tags={ "image", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = StatisticMonth.class, responseContainer = "List")
    })
    public Response statMonth(@ApiParam(value = "Year for which the Statistic should be calculated. If left an overall Statistic will be calculated.") @QueryParam("year")  Integer year,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.statMonth(year, securityContext);
    }
}
