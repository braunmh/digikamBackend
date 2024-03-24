package org.braun.digikam.backend.ejb;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.braun.digikam.backend.api.NotFoundException;
import org.braun.digikam.backend.util.Util;

/**
 *
 * @author mbraun
 */
@Stateless
public class ImagesFacade extends AbstractFacade<Images> {

    @Inject
    private ImageInformationFacade imageInformationFacade;
    
    @PersistenceContext(unitName = "digikam")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ImagesFacade() {
        super(Images.class);
    }

    public void addTag(Images image, Tags tag) {
        image.getTags().add(tag);
        merge(image);
    }

    public void updateCreator(String value, Images image) {
        for (ImageCopyright c : image.getCopyrights()) {
            if (null != c.getProperty() && "creator".equals(c.getProperty())) {
                c.setValue(value);
                return;
            }
        }
        ImageCopyright ic = new ImageCopyright();
        ic.setImage(image);
        ic.setProperty("creator");
        ic.setValue(value);
        image.getCopyrights().add(ic);
    }

    public final void update(int id, String title, String description, int rating, List<Tags> tags, String creator) throws NotFoundException {
        Images images = find(id);
        if (images == null) {
            String msg = String.format("Image with id %s not found or exists anymore", id);
            throw new NotFoundException(404, msg);
        }

        ImageInformation information = getImageInformationFacade().findByImageId(id);
        if (null != information) {
            information.setRating(rating);
            getImageInformationFacade().merge(information);
        }

        updateComment(3, title, images);
        updateComment(1, description, images);

        updateCreator(creator, images);
        
        images.getTags().clear();
        images.getTags().addAll(tags);
        merge(images);
    }

    private void updateComment(int type, String value, Images images) {
        for (ImageComments c : images.getComments()) {
            if (null != c.getType() && type == c.getType()) {
                c.setComment(value);
                return;
            }
        }

        ImageComments imageComments = new ImageComments();
        imageComments.setType(type);
        imageComments.setImage(images);
        imageComments.setLanguage("x-default");
        imageComments.setComment(value);
        imageComments.setType(type);
        images.getComments().add(imageComments);
    }

    public ImageInformationFacade getImageInformationFacade() {
        if (imageInformationFacade == null) {
            imageInformationFacade = Util.Cdi.lookup(ImageInformationFacade.class);
        }
        return imageInformationFacade;
    }

}
