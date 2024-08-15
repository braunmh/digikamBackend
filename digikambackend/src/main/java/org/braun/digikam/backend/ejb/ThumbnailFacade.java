package org.braun.digikam.backend.ejb;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.braun.digikam.backend.entity.Thumbnail;

/**
 *
 * @author mbraun
 */
public class ThumbnailFacade extends AbstractFacade<Thumbnail> {
    
    public ThumbnailFacade() {
        super(Thumbnail.class);
    }

    @PersistenceContext(unitName = "digikam")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
