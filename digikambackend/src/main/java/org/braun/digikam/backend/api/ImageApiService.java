package org.braun.digikam.backend.api;


import java.util.List;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.validation.constraints.*;
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-11-02T16:29:05.722779185+01:00[Europe/Berlin]")
public abstract class ImageApiService {
    public abstract Response findImagesByImageAttributes(List<Integer> keywords,String creator,String makeModel,String lens,String orientation,String dateFrom,String dateTo,Integer ratingFrom,Integer ratingTo,Integer isoFrom,Integer isoTo,Double exposureTimeFrom,Double exposureTimeTo,Double apertureFrom,Double apertureTo,Integer focalLengthFrom,Integer focalLengthTo,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getImage(Integer imageId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getInformationAboutImage(Integer imageId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response rateImage( @NotNull Integer imageId, @NotNull Integer rating,SecurityContext securityContext) throws NotFoundException;
    public abstract Response scalesImage( @NotNull Integer imageId, @NotNull Integer width, @NotNull Integer height,SecurityContext securityContext) throws NotFoundException;
    public abstract Response statKeyword( @NotNull Integer keywordId, @NotNull Integer year,SecurityContext securityContext) throws NotFoundException;
    public abstract Response statMonth(Integer year,SecurityContext securityContext) throws NotFoundException;
    public abstract Response imageStatus(SecurityContext securityContext) throws NotFoundException;
}
