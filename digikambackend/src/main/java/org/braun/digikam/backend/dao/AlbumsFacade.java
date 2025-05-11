package org.braun.digikam.backend.dao;

import org.braun.digikam.backend.entity.Albums;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author mbraun
 */
public class AlbumsFacade extends AbstractFacade<Albums> {

    @PersistenceContext(unitName = "digikam")
    private EntityManager em;
    
    public AlbumsFacade() {
        super(Albums.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public void setEnitityManager(EntityManager em) {
        this.em = em;
    }

    public Albums findByRelPath(String relPath) {
        TypedQuery<Albums> query = getEntityManager().createNamedQuery("Albums.findByRelPath", Albums.class);
        query.setParameter("relativePath", relPath);
        List<Albums> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
