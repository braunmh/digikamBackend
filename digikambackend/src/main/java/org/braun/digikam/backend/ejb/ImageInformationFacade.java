package org.braun.digikam.backend.ejb;

import jakarta.ejb.Stateless;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

/**
 *
 * @author mbraun
 */
@Stateless
public class ImageInformationFacade extends AbstractFacade<ImageInformation> {

    private static final String SQL_FIND_BY_NAME
        = "select ii.imageid, ii.rating, ii.creationDate, ii.digitizationDate, ii.orientation,\n"
        + "ii.width, ii.height, ii.format, ii.colorDepth, ii.colorModel\n"
        + "from ImageInformation ii inner join Images i on ii.imageid = i.id\n"
        + "where i.name = ?";

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

    public ImageInformation findByImageId(int imageId) {
        TypedQuery<ImageInformation> query = getEntityManager().createNamedQuery("ImageInformation.findByImageId", ImageInformation.class);
        query.setParameter("imageId", imageId);
        List<ImageInformation> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
