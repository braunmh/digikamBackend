package org.braun.digikam.backend.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import org.braun.digikam.backend.entity.Thumbnail;

/**
 *
 * @author mbraun
 */
@Stateless
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

    public void setEntityManger(EntityManager em) {
        this.em = em;
    }
    
    public void updateModificationDate(long id) {
        Thumbnail thumbnail = find(id);
        thumbnail.setModificationDate(new Date());
        merge(thumbnail);
    }
}
