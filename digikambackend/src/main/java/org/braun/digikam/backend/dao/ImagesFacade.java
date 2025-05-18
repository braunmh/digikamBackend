package org.braun.digikam.backend.dao;

import org.braun.digikam.backend.entity.ImageCopyright;
import org.braun.digikam.backend.entity.Tags;
import org.braun.digikam.backend.entity.ImageInformation;
import org.braun.digikam.backend.entity.Images;
import org.braun.digikam.backend.entity.ImageComments;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
        Set<Tags> sanitized = sanitizeTags(image.getTags());
        image.getTags().clear();
        image.getTags().addAll(sanitized);
        image.getTags().add(tag);
        merge(image);
    }

    public void addTag(Images image, List<Tags> tag) {
        Set<Tags> sanitized = sanitizeTags(image.getTags());
        image.getTags().clear();
        image.getTags().addAll(sanitized);
        image.getTags().addAll(tag);
        merge(image);
    }

    private Set<Tags> sanitizeTags(Set<Tags> tags) {
        return tags.stream().filter(t -> t.getId() > 0).collect(Collectors.toSet());
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

    public final void update(long id, String title, String description, Integer rating, List<Tags> tags, String creator, LocalDateTime creationDate) throws NotFoundException {
        Images images = find(id);
        if (images == null) {
            String msg = String.format("Image with id %s not found or exists anymore", id);
            throw new NotFoundException(404, msg);
        }
        images.setModificationDate(new Date());
        ImageInformation information = getImageInformationFacade().findByImageId(id);
        if (null != information) {
            information.setRating(rating);
            information.setCreationDate(getDate(creationDate));
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

    /**
     * 
     * @param ldt DateTime without TimeZone
     * @return absolute Date without any Timezone-Information
     */
    private Date getDate(LocalDateTime ldt) {
        Calendar cal = Calendar.getInstance();
        cal.set(ldt.getYear(), ldt.getMonthValue()-1, ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond());
        return cal.getTime();
    }
    
    public Images findByNameAndAlbum(String name, int albumId) {
        TypedQuery<Images> query = getEntityManager().createNamedQuery("Images.findByNameAndAlbumId", Images.class);
        query.setParameter("name", name);
        query.setParameter("albumId", albumId);
        List<Images> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
    
    public ImageInformationFacade getImageInformationFacade() {
        if (imageInformationFacade == null) {
            imageInformationFacade = Util.Cdi.lookup(ImageInformationFacade.class);
        }
        return imageInformationFacade;
    }

    public void setImageInformationFacade(ImageInformationFacade imageInformationFacade) {
        this.imageInformationFacade = imageInformationFacade;
    }

    public void setEnitityManager(EntityManager em) {
        this.em = em;
    }

}
