package org.braun.digikam.backend.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.entity.Topic;
/**
 *
 * @author mbraun
 */
@Stateless
public class TopicDao extends AbstractDao<Topic> {

    private static final Logger LOG = LogManager.getLogger();

    public TopicDao() {
        super(Topic.class);
    }

    @PersistenceContext(unitName = "digikam")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
    
}
