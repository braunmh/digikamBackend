package org.braun.digikam.backend.api;

import org.braun.digikam.backend.api.factories.VideoApiServiceFactory;

import io.swagger.annotations.ApiParam;

import java.io.File;
import org.braun.digikam.backend.model.ImageUpdate;
import org.braun.digikam.backend.model.Media;
import org.braun.digikam.backend.model.Video;

import java.util.List;

import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Path("/video")


@io.swagger.annotations.Api(description = "the video API")
public class VideoApi  {
   private final VideoApiService delegate;

   public VideoApi(@Context ServletConfig servletContext) {
      VideoApiService delegate = null;

      if (servletContext != null) {
         String implClass = servletContext.getInitParameter("VideoApi.implementation");
         if (implClass != null && !"".equals(implClass.trim())) {
            try {
               delegate = (VideoApiService) Class.forName(implClass).newInstance();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }

      if (delegate == null) {
         delegate = VideoApiServiceFactory.getVideoApi();
      }

      this.delegate = delegate;
   }

    @jakarta.ws.rs.GET
    @Path("/find")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Media.class, responseContainer = "List", tags={ "video", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Media.class, responseContainer = "List")
    })
    public Response findVideosByAttributes(@ApiParam(value = "List of Keywords") @QueryParam("keywords") @Valid  List<Integer> keywords,@ApiParam(value = "") @QueryParam("creator")  String creator,@ApiParam(value = "", allowableValues="Portrait, Landscape") @QueryParam("orientation")  String orientation,@ApiParam(value = "") @QueryParam("date_from")  String dateFrom,@ApiParam(value = "") @QueryParam("date_to")  String dateTo,@ApiParam(value = "") @QueryParam("ratingFrom")  Integer ratingFrom,@ApiParam(value = "") @QueryParam("ratingTo")  Integer ratingTo,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.findVideosByAttributes(keywords, creator, orientation, dateFrom, dateTo, ratingFrom, ratingTo, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/getInformation")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Video.class, tags={ "video", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Metadata of Video with Id", response = Video.class)
    })
    public Response getInformationAboutVideo(@ApiParam(value = "Id identifing an Video") @QueryParam("videoId")  Integer videoId,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getInformationAboutVideo(videoId, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/thumbnail/{videoId}")
    
    @Produces({ "image/jpeg" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = byte[].class, tags={ "video", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = byte[].class)
    })
    public Response getThumbnail(@ApiParam(value = "Id of video to return an thumbnail", required = true) @PathParam("videoId") @NotNull  Integer videoId,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getThumbnail(videoId, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/stream/{videoId}")
    
    @Produces({ "application/octet-stream" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = File.class, tags={ "video", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "the content of an video as stream", response = File.class)
    })
    public Response getVideoStream(@ApiParam(value = "Id of video to return", required = true) @PathParam("videoId") @NotNull  Integer videoId,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getVideoStream(videoId, securityContext);
    }
    @jakarta.ws.rs.GET
    @Path("/rate")
    
    
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Void.class, tags={ "video", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Update successful", response = Void.class)
    })
    public Response rateVideo(@ApiParam(value = "Id of video to rate", required = true) @QueryParam("videoId") @NotNull  Integer videoId,@ApiParam(value = "The new rating", required = true) @QueryParam("rating") @NotNull  Integer rating,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.rateVideo(videoId, rating, securityContext);
    }
    @jakarta.ws.rs.POST
    @Path("/update")
    @Consumes({ "application/json" })
    
    @io.swagger.annotations.ApiOperation(value = "", notes = "", response = Void.class, tags={ "video", })
    @io.swagger.annotations.ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 200, message = "Update video successful", response = Void.class)
    })
    public Response videoUpdate(@ApiParam(value = "one parameter must be provided", required = true) @NotNull @Valid  ImageUpdate imageUpdate,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.videoUpdate(imageUpdate, securityContext);
    }
}
