package org.braun.digikam.backend.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author mbraun
 */
@Stateless
public class AlbumRootsFacade extends AbstractFacade<AlbumRoots> {

    @PersistenceContext(unitName = "digikam")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AlbumRootsFacade() {
        super(AlbumRoots.class);
    }
    
}
