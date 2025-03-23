package org.braun.digikam.backend.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.braun.digikam.backend.entity.Label;

/**
 *
 * @author mbraun
 */
@Stateless
public class LabelFacade extends AbstractFacade<Label> {

    public LabelFacade() {
        super(Label.class);
    }

    @PersistenceContext(unitName = "digikam")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void setEnityManager(EntityManager em) {
        this.em = em;
    }
    
    public Label createNew(Label entity) {
        entity.setLastModified(new Date());
        super.create(entity);
        TypedQuery<Label> query = getEntityManager()
                .createQuery("Select l from Label l where l.key = :key and l.language = :language and l.country = :country", Label.class)
                .setParameter("key", entity.getKey())
                .setParameter("language", entity.getLanguage())
                .setParameter("country", entity.getCountry());
        return query.getSingleResult();
    }

    public Label findByKeyLanguageCountry(String key, String language, String country) {
        TypedQuery<Label> query = getEntityManager()
                .createQuery("Select l from Label l where l.key = :key and l.language = :language and l.country = :country", Label.class)
                .setParameter("key", key)
                .setParameter("language", getNormedValue(language))
                .setParameter("country", getNormedValue(country));
        List<Label> res = query.getResultList();
        if (res.isEmpty()) {
            return null; 
        }
        return res.get(0);
    }
    
    @Override
    public void merge(Label entity) {
        entity.setLastModified(new Date());
        super.merge(entity);
    }

    public List<Label> findAll(Locale locale) {
        TypedQuery<Label> query = getEntityManager()
                .createQuery("Select l from Label l where l.country = :country and l.language = :language order by l.key", Label.class)
                .setParameter("country", getCountry(locale))
                .setParameter("language", getLanguage(locale));
        return query.getResultList();
    }

    public Date getLastModified(Locale locale) {
        Query query = getEntityManager()
                .createNativeQuery("select max(last_modified) from Label where country = ? and language = ?")
                .setParameter(1, getCountry(locale))
                .setParameter(2, getLanguage(locale));
        Object res = query.getSingleResult();
        if (res == null) {
            return new Date(0);
        } else {
            return (Date) res;
        }
    }
    
    private String getCountry(Locale locale) {
        return getNormedValue(locale.getCountry());
    } 
    
    private String getLanguage(Locale locale) {
        return getNormedValue(locale.getLanguage());
    } 
    private String getNormedValue(String value) {
        return (StringUtils.isBlank(value))? "--" : value;
    } 
    
}
