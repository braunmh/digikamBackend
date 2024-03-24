package org.braun.digikam.backend.api;

import org.braun.digikam.backend.model.ImageUpdate;

import java.util.List;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
public abstract class ImageApiService {
    public abstract Response findImagesByImageAttributes(List<Integer> keywords,String creator,String makeModel,String lens,String orientation,String dateFrom,String dateTo,Integer ratingFrom,Integer ratingTo,Integer isoFrom,Integer isoTo,Double exposureTimeFrom,Double exposureTimeTo,Double apertureFrom,Double apertureTo,Integer focalLengthFrom,Integer focalLengthTo,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getImage(Integer imageId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getInformationAboutImage(Integer imageId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response imageStatus(SecurityContext securityContext) throws NotFoundException;
    public abstract Response imageUpdate(ImageUpdate imageUpdate,SecurityContext securityContext) throws NotFoundException;
    public abstract Response rateImage( @NotNull Integer imageId, @NotNull Integer rating,SecurityContext securityContext) throws NotFoundException;
    public abstract Response scalesImage( @NotNull Integer imageId, @NotNull Integer width, @NotNull Integer height,SecurityContext securityContext) throws NotFoundException;
    public abstract Response statKeyword( @NotNull Integer keywordId, @NotNull Integer year,SecurityContext securityContext) throws NotFoundException;
    public abstract Response statMonth(Integer year,SecurityContext securityContext) throws NotFoundException;
}
