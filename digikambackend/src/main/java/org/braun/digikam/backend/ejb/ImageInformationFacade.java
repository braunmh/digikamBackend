package org.braun.digikam.backend.ejb;

import org.braun.digikam.backend.entity.ImageInformation;
import jakarta.ejb.Stateless;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.api.NotFoundException;

/**
 *
 * @author mbraun
 */
@Stateless
public class ImageInformationFacade extends AbstractFacade<ImageInformation> {

    private static final Logger LOG = LogManager.getLogger();
    
    private static final String SQL_FIND_BY_NAME
        = """
          select ii.imageid, ii.rating, ii.creationDate, ii.digitizationDate, ii.orientation,
          ii.width, ii.height, ii.format, ii.colorDepth, ii.colorModel
          from ImageInformation ii inner join Images i on ii.imageid = i.id
          where i.name = ?""";

    @PersistenceContext(unitName = "digikam")
    private EntityManager em;

    public ImageInformationFacade() {
        super(ImageInformation.class);
    }

    public ImageInformation findByName(String name) {
        Query query = getEntityManager().createNativeQuery(SQL_FIND_BY_NAME, ImageInformation.class);
        query.setParameter(1, name);
        @SuppressWarnings("unchecked")
        List<ImageInformation> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    public ImageInformation findByImageId(Long imageId) {
        TypedQuery<ImageInformation> query = getEntityManager().createNamedQuery("ImageInformation.findByImageId", ImageInformation.class);
        query.setParameter("imageId", imageId);
        List<ImageInformation> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    /**
     * 
     * @param id of Image to rate
     * @param rating new rating 
     * @throws NotFoundException 
     */
    public final void updateRating(Long id, int rating) throws NotFoundException {
        TypedQuery<ImageInformation> query = getEntityManager().createNamedQuery("ImageInformation.findByImageId", ImageInformation.class);
        query.setParameter("imageId", id);
        try {
            ImageInformation imageInformation = query.getSingleResult();
            imageInformation.setRating(rating);
            getEntityManager().merge(imageInformation);
        } catch (NoResultException e) {
            String msg = String.format("Image with id %s not found or exists anymore", id);
            LOG.info("msg");
            throw new NotFoundException(404, msg);
        }
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
