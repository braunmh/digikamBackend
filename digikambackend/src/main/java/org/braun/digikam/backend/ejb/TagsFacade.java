package org.braun.digikam.backend.ejb;

import org.braun.digikam.backend.entity.Tags;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

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
    
    /**
     * Search for Tag. If not exist creates an new Tag with the hierarchical structure given in tags
     * 
     * @param tags list of tags
     * @return id of last tag
     */
    public Tags findAndInsertTagByTree(String... tags) {
        int current = 0;
        Tags parent = null;
        long parentId = 0;
        for (String tagName : tags) {
            TypedQuery<Tags> query = em.createQuery("Select t from Tags t where t.name = :name and t.pid = :pid", Tags.class);
            query.setParameter("name", tagName);
            query.setParameter("pid", parentId);
            List<Tags> result = query.getResultList();
            if (result.isEmpty()) {
                for (int i = current; i < tags.length; i++) {
                    Tags tag = new Tags();
                    tag.setName(tags[i]);
                    tag.setPid(parentId);
                    create(tag);
                    TypedQuery<Tags> aq = em.createQuery("Select t from Tags t where t.name = :name and t.pid = :pid", Tags.class);
                    aq.setParameter("name", tags[i]);
                    aq.setParameter("pid", parentId);
                    parent = aq.getSingleResult();
                    parentId = parent.getId();
                }
                break;
            } else {
                parent = result.get(0);
                parentId = parent.getId();
                current++;
            }
        }
        return parent;
    }
    
}
