package org.braun.digikam.backend.ejb;

import org.braun.digikam.backend.entity.ImageCopyright;
import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.braun.digikam.backend.entity.StatAuthorView;

/**
 *
 * @author mbraun
 */
@Stateless
public class ImageCopyrightFacade extends AbstractFacade<ImageCopyright> {

    private static final String FIND_CREATORS = "SELECT distinct i.value FROM ImageCopyright i where i.property = 'creator' order by i.value";
    private static final String FIND_AUTHOR_CAMERA_STATISTIC = 
        """
        select count(*) count, im.make, im.model, COALESCE(ic.value, 'unbekannt') creator
        from ImageMetadata im left join ImageCopyright ic on im.imageid = ic.imageid and ic.property = 'creator'
        group by ic.value, im.make, im.model
        order by 4, 2, 3""";
    
    @PersistenceContext(unitName = "digikam")
    private EntityManager em;

    public ImageCopyrightFacade() {
        super(ImageCopyright.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<String> findAllCreators() {
        Query query = getEntityManager().createNativeQuery(FIND_CREATORS);
        return query.getResultList();
    }
    
    public List<StatAuthorView> findAuthorCameraStatistic() {
        Query query = getEntityManager().createNativeQuery(FIND_AUTHOR_CAMERA_STATISTIC, StatAuthorView.class);
        return query.getResultList();
    }
}
