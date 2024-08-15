package org.braun.digikam.backend.api.impl;

import jakarta.inject.Inject;
import jakarta.validation.constraints.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.api.*;
import org.braun.digikam.backend.ejb.ImageInformationFacade;
import org.braun.digikam.backend.ejb.ImagesFacade;
import org.braun.digikam.backend.entity.Tags;
import org.braun.digikam.backend.ejb.VideoFacade;
import org.braun.digikam.backend.model.ImageUpdate;
import org.braun.digikam.backend.model.Media;
import org.braun.digikam.backend.model.Video;
import org.braun.digikam.backend.search.ConditionParseException;
import org.braun.digikam.backend.util.Util;

public class VideoApiServiceImpl extends VideoApiService {

    @Inject VideoFacade videoFacade;
    @Inject private ImagesFacade imagesFacade;
    @Inject private ImageInformationFacade informationFacade;
    
    @Override
    public Response findVideosByAttributes(List<Long> keywords, String creator, String orientation,
        String dateFrom, String dateTo, Integer ratingFrom, Integer ratingTo, SecurityContext securityContext) throws NotFoundException {

        VideoFacade facade = getVideoFacade();
        try {
            List<Media> result = facade.findVideosByAttributes(
                keywords, creator, orientation,
                dateFrom, dateTo, ratingFrom, ratingTo);
            return Response.ok().entity(result).build();
        } catch (ConditionParseException e) {
            throw new NotFoundException(404, e.getMessage());
        }
    }

    @Override
    public Response getInformationAboutVideo(Long videoId, SecurityContext securityContext) throws NotFoundException {
        VideoFacade facade = getVideoFacade();
        Video image = facade.getMetadata(videoId);
        return Response.ok().entity(image).build();
    }

    @Override
    public Response getVideoStream(Long videoId, SecurityContext securityContext) throws NotFoundException {
        VideoFacade facade = getVideoFacade();
        return Response.ok().entity(facade.getVideo(videoId)).build();
    }

    @Override
    public Response rateVideo(@NotNull Long videoId, @NotNull Integer rating, SecurityContext securityContext) throws NotFoundException {
        ImageInformationFacade facade = getInformationFacade();
        facade.updateRating(videoId, rating);
        return Response.ok().entity("Okay").build();
    }

    @Override
    public Response videoUpdate(ImageUpdate imageUpdate, SecurityContext securityContext) throws NotFoundException {
        ImagesFacade facade = getImagesFacade();
        List<Tags> tags = new ArrayList<>();
        for (Long tagId : imageUpdate.getKeywords()) {
            tags.add(new Tags().name(NodeFactory.getInstance().getKeywordById(tagId).getName()).id(tagId));
        }
        facade.update(imageUpdate.getImageId(), imageUpdate.getTitle(), imageUpdate.getDescription(), imageUpdate.getRating(),
             tags, imageUpdate.getCreator());
        return Response.ok().build();
    }

    @Override
    public Response getThumbnail(Long videoId, SecurityContext securityContext) throws NotFoundException {
        VideoFacade facade = getVideoFacade();
         return Response.ok().entity(facade.getThumbnail(videoId)).build();
   }

    public VideoFacade getVideoFacade() {
        if (videoFacade == null) {
            videoFacade = Util.Cdi.lookup(VideoFacade.class);
        }
        return videoFacade;
    }
    
    public ImagesFacade getImagesFacade() {
        if (imagesFacade == null) {
            imagesFacade = Util.Cdi.lookup(ImagesFacade.class);
        }
        return imagesFacade;
    }

    public ImageInformationFacade getInformationFacade() {
        if (informationFacade == null) {
            informationFacade = Util.Cdi.lookup(ImageInformationFacade.class);
        }
        return informationFacade;
    }
}
