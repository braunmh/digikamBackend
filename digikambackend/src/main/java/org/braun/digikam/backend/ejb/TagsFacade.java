package org.braun.digikam.backend.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author mbraun
 */
@Stateless
public class TagsFacade extends AbstractFacade<Tags> {

    @PersistenceContext(unitName = "digikam")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TagsFacade() {
        super(Tags.class);
    }
    
    void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
