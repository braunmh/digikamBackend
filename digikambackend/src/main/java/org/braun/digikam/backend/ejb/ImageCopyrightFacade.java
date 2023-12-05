package org.braun.digikam.backend.ejb;

import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
@Stateless
public class ImageCopyrightFacade extends AbstractFacade<ImageCopyright> {

    private static final String FIND_CREATORS = "SELECT distinct i.value FROM ImageCopyright i where i.property = 'creator' order by i.value";
    
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
    
}
