package org.braun.digikam.backend.api;

import org.braun.digikam.backend.model.ImageUpdate;

import java.util.List;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
public abstract class VideoApiService {
    public abstract Response findVideosByAttributes(List<Long> keywords,String creator,String orientation,String dateFrom,String dateTo,Integer ratingFrom,Integer ratingTo,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getInformationAboutVideo(Long videoId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getThumbnail(Long videoId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getVideoStream(Long videoId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response rateVideo( @NotNull Long videoId, @NotNull Integer rating,SecurityContext securityContext) throws NotFoundException;
    public abstract Response videoUpdate(ImageUpdate imageUpdate,SecurityContext securityContext) throws NotFoundException;
}
