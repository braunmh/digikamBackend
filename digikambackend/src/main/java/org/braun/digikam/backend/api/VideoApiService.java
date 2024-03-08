package org.braun.digikam.backend.api;

import jakarta.validation.constraints.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;
import org.braun.digikam.backend.model.ImageUpdate;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-03-01T16:23:06.936844939+01:00[Europe/Berlin]")
public abstract class VideoApiService {
    public abstract Response findVideosByAttributes(List<Integer> keywords,String creator,String orientation,String dateFrom,String dateTo,Integer ratingFrom,Integer ratingTo,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getInformationAboutVideo(Integer videoId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getThumbnail(Integer videoId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getVideo(Integer videoId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response rateVideo( @NotNull Integer videoId, @NotNull Integer rating,SecurityContext securityContext) throws NotFoundException;
    public abstract Response videoUpdate(ImageUpdate imageUpdate,SecurityContext securityContext) throws NotFoundException;
}
