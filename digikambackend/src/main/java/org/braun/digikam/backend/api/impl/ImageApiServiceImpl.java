package org.braun.digikam.backend.api.impl;

import java.io.ByteArrayInputStream;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import org.braun.digikam.backend.CameraFactory;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.api.*;
import org.braun.digikam.backend.ejb.ImageFacade;
import org.braun.digikam.backend.ejb.ImageInformationFacade;
import org.braun.digikam.backend.ejb.ImagesFacade;
import org.braun.digikam.backend.ejb.Tags;
import org.braun.digikam.backend.model.Image;
import org.braun.digikam.backend.model.ImageUpdate;
import org.braun.digikam.backend.model.ImagesInner;
import org.braun.digikam.backend.model.StatisticKeyword;
import org.braun.digikam.backend.model.StatisticMonth;
import org.braun.digikam.backend.search.ConditionParseException;
import org.braun.digikam.backend.util.Util;

public class ImageApiServiceImpl extends ImageApiService {

    /**
     *
     * @param keywords
     * @param creator
     * @param makeModel
     * @param lens
     * @param orientation
     * @param dateFrom
     * @param dateTo
     * @param ratingFrom
     * @param ratingTo
     * @param isoFrom
     * @param isoTo
     * @param exposureTimeFrom
     * @param exposureTimeTo
     * @param apertureFrom
     * @param apertureTo
     * @param focalLengthFrom
     * @param focalLengthTo
     * @param securityContext
     * @return List of ImagesInner
     * @throws NotFoundException
     */
    @Override
    public Response findImagesByImageAttributes(List<Integer> keywords, String creator, String makeModel, String lens, 
        String orientation, String dateFrom, String dateTo, Integer ratingFrom, Integer ratingTo, Integer isoFrom, Integer isoTo, 
        Double exposureTimeFrom, Double exposureTimeTo, Double apertureFrom, Double apertureTo, Integer focalLengthFrom, Integer focalLengthTo, 
        SecurityContext securityContext) throws NotFoundException {
        CameraFactory.CameraEntry camera = CameraFactory.getInstance().getByName(makeModel);
        
        ImageFacade facade = Util.EJB.lookup(ImageFacade.class);
        try {
            List<ImagesInner> result = facade.findImagesByImageAttributes(
                keywords, creator, camera.getMake(), camera.getModel(), lens, orientation, 
                dateFrom, dateTo, ratingFrom, ratingTo, isoFrom, isoTo, exposureTimeFrom, exposureTimeTo, 
                apertureFrom, apertureTo, focalLengthFrom, focalLengthTo);
            return Response.ok().entity(result).build();
        } catch (ConditionParseException e) {
            throw new NotFoundException(404, e.getMessage());
        }
    }

    /**
     *
     * @param imageId
     * @param securityContext
     * @return Image as byte[]
     * @throws NotFoundException
     */
    @Override
    public Response getImage(Integer imageId, SecurityContext securityContext) throws NotFoundException {
        ImageFacade facade = Util.EJB.lookup(ImageFacade.class);
        return Response.ok().entity(facade.getImage(imageId)).build();
    }

    /**
     *
     * @param imageId of Image
     * @param securityContext
     * @return Image as response
     * @throws NotFoundException
     */
    @Override
    public Response getInformationAboutImage(Integer imageId, SecurityContext securityContext) throws NotFoundException {
        ImageFacade facade = Util.EJB.lookup(ImageFacade.class);
        Image image = facade.getMetadata(imageId);
        return Response.ok().entity(image).build();
    }

    /**
     *
     * @param imageId
     * @param width
     * @param height
     * @param securityContext
     * @return Image as byte[]
     * @throws NotFoundException
     */
    @Override
    public Response scalesImage(@NotNull Integer imageId, @NotNull Integer width, @NotNull Integer height, SecurityContext securityContext) throws NotFoundException {
        ImageFacade facade = Util.EJB.lookup(ImageFacade.class);
        byte [] result = facade.getScaledImage(imageId,width, height);
        return Response.ok().entity(new ByteArrayInputStream(result)).build();
    }

    @Override
    public Response rateImage(Integer imageId, Integer rating, SecurityContext securityContext) throws NotFoundException {
        ImageInformationFacade facade = Util.EJB.lookup(ImageInformationFacade.class);
        facade.updateRating(imageId, rating);
        return Response.ok().entity("Okay").build();
    }

    @Override
    public Response statKeyword(Integer keywordId, Integer year, SecurityContext securityContext) throws NotFoundException {
        ImageFacade facade = Util.EJB.lookup(ImageFacade.class);
        try {
            List<StatisticKeyword> result = facade.statKeyword(keywordId, year);
            return Response.ok().entity(result).build();
        } catch (ConditionParseException e) {
            throw new NotFoundException(404, e.getMessage());
        }
    }
    @Override

    public Response statMonth(Integer year, SecurityContext securityContext) throws NotFoundException {
        ImageFacade facade = Util.EJB.lookup(ImageFacade.class);
        List<StatisticMonth> result = facade.statMonth(year);
        return Response.ok().entity(result).build();
    }

    @Override
    public Response imageStatus(SecurityContext securityContext) throws NotFoundException {
        return Response.ok().build();
    }

    /**
     * 
     * @param imageUpdate Information of Image to update
     * @param securityContext 
     * @return Response.ok()
     * @throws NotFoundException if image with supplied imageId was not found
     */
    @Override
    public Response imageUpdate(ImageUpdate imageUpdate, SecurityContext securityContext) throws NotFoundException {
        ImagesFacade facade = Util.EJB.lookup(ImagesFacade.class);
        List<Tags> tags = new ArrayList<>();
        for (Integer tagId : imageUpdate.getKeywords()) {
            tags.add(new Tags().name(NodeFactory.getInstance().getKeywordById(tagId).getName()).id(tagId));
        }
        facade.update(imageUpdate.getImageId(), imageUpdate.getTitle(), imageUpdate.getDescription(), imageUpdate.getRating()
            , tags, imageUpdate.getCreator());
        return Response.ok().build();
    }
}
