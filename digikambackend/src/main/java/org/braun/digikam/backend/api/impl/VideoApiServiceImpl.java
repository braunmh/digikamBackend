package org.braun.digikam.backend.api.impl;

import jakarta.validation.constraints.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.api.*;
import org.braun.digikam.backend.ejb.ImageInformationFacade;
import org.braun.digikam.backend.ejb.ImagesFacade;
import org.braun.digikam.backend.ejb.Tags;
import org.braun.digikam.backend.ejb.VideoFacade;
import org.braun.digikam.backend.model.ImageUpdate;
import org.braun.digikam.backend.model.ImagesInner;
import org.braun.digikam.backend.model.Video;
import org.braun.digikam.backend.search.ConditionParseException;
import org.braun.digikam.backend.util.Util;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-03-01T16:23:06.936844939+01:00[Europe/Berlin]")
public class VideoApiServiceImpl extends VideoApiService {

    @Override
    public Response findVideosByAttributes(List<Integer> keywords, String creator, String orientation,
        String dateFrom, String dateTo, Integer ratingFrom, Integer ratingTo, SecurityContext securityContext) throws NotFoundException {

        VideoFacade facade = Util.EJB.lookup(VideoFacade.class);
        try {
            List<ImagesInner> result = facade.findVideosByAttributes(
                keywords, creator, orientation,
                dateFrom, dateTo, ratingFrom, ratingTo);
            return Response.ok().entity(result).build();
        } catch (ConditionParseException e) {
            throw new NotFoundException(404, e.getMessage());
        }
    }

    @Override
    public Response getInformationAboutVideo(Integer videoId, SecurityContext securityContext) throws NotFoundException {
        VideoFacade facade = Util.EJB.lookup(VideoFacade.class);
        Video image = facade.getMetadata(videoId);
        return Response.ok().entity(image).build();
    }

    @Override
    public Response getVideo(Integer videoId, SecurityContext securityContext) throws NotFoundException {
        VideoFacade facade = Util.EJB.lookup(VideoFacade.class);
        return Response.ok().entity(facade.getVideo(videoId)).build();
    }

    @Override
    public Response rateVideo(@NotNull Integer videoId, @NotNull Integer rating, SecurityContext securityContext) throws NotFoundException {
        ImageInformationFacade facade = Util.EJB.lookup(ImageInformationFacade.class);
        facade.updateRating(videoId, rating);
        return Response.ok().entity("Okay").build();
    }

    @Override
    public Response videoUpdate(ImageUpdate imageUpdate, SecurityContext securityContext) throws NotFoundException {
        ImagesFacade facade = Util.EJB.lookup(ImagesFacade.class);
        List<Tags> tags = new ArrayList<>();
        for (Integer tagId : imageUpdate.getKeywords()) {
            tags.add(new Tags().name(NodeFactory.getInstance().getKeywordById(tagId).getName()).id(tagId));
        }
        facade.update(imageUpdate.getImageId(), imageUpdate.getTitle(), imageUpdate.getDescription(), imageUpdate.getRating(),
             tags, imageUpdate.getCreator());
        return Response.ok().build();
    }

    @Override
    public Response getThumbnail(Integer videoId, SecurityContext securityContext) throws NotFoundException {
        VideoFacade facade = Util.EJB.lookup(VideoFacade.class);
         return Response.ok().entity(facade.getThumbnail(videoId)).build();
   }
}
