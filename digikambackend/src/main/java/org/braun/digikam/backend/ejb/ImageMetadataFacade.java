package org.braun.digikam.backend.ejb;

import java.util.ArrayList;
import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.braun.digikam.backend.CameraFactory;
import org.braun.digikam.backend.entity.CameraLensView;
import org.braun.digikam.backend.entity.ImageMetadata;
import org.braun.digikam.backend.model.CameraLens;
import org.braun.digikam.backend.search.sql.EmptyCondition;
import org.braun.digikam.backend.search.sql.Operator;
import org.braun.digikam.backend.search.sql.SimpleCondition;
import org.braun.digikam.backend.search.sql.Sql;

/**
 *
 * @author mbraun
 */
@Stateless
public class ImageMetadataFacade extends AbstractFacade<ImageMetadata>  {
    
    private static final String FIND_CAMERAS = "select distinct make, model from ImageMetadata order by make, model";
    
    @PersistenceContext(unitName = "digikam")
    private EntityManager em;

    public ImageMetadataFacade() {
        super(ImageMetadata.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<CameraFactory.CameraEntry> findAllCameras() {
        Query query = getEntityManager().createNativeQuery(FIND_CAMERAS);
        @SuppressWarnings("unchecked")
        List<Object[]> records = query.getResultList();
        List<CameraFactory.CameraEntry> result = new ArrayList<>();
        for (Object[] o : records) {
            result.add(new CameraFactory.CameraEntry((String)o[0], (String)o[1]));
        }
        return result;
    }
    
    public List<String> getLens(String make, String model) {
        Sql sql = new Sql("select distinct lens from ImageMetadata");
        if (StringUtils.isNotEmpty(make)) {
            sql.addCondition(new SimpleCondition("make", make));
        }
        if (StringUtils.isNotEmpty(model)) {
            sql.addCondition(new SimpleCondition("model", model));
        }
        sql.addCondition(new EmptyCondition("lens", Operator.IsNotNull));
        sql.addCondition(new SimpleCondition("lens", "----", Operator.NotEquals));
        sql.addOrderClause("lens");
        Query query = sql.buildQuery(em, null);
        return query.getResultList();
    }
    
    private static final String ALL_COMBINATIONS = 
            """
            select distinct concat(make, ', ', model) camera, ifnull(lens, '') lens 
            from ImageMetadata
            order by 1, 2""";
    
    public List<CameraLens> getAllCombinations() {
        Query query = getEntityManager().createNativeQuery(ALL_COMBINATIONS, CameraLensView.class);
        List<CameraLensView> tmp = query.getResultList();
        List<CameraLens> result = new ArrayList<>();
        for (CameraLensView entry : tmp) {
            result.add(new CameraLens()
                .camera(entry.getId().getCamera())
                .lens(entry.getId().getLens()));
        }
        return result;
    }
    
    void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
