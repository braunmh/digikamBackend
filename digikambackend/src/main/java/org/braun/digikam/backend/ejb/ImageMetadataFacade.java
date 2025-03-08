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
import org.braun.digikam.backend.entity.DiagramView;
import org.braun.digikam.backend.entity.ImageMetadata;
import org.braun.digikam.backend.model.CameraLens;
import org.braun.digikam.backend.search.sql.EmptyCondition;
import org.braun.digikam.backend.search.sql.Operator;
import org.braun.digikam.backend.search.sql.SimpleCondition;
import org.braun.digikam.backend.search.sql.Sql;
import org.braun.digikam.web.component.diagram.Point;

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
    
    private static final String STAT_EXPOSURE_TIME =
            """
            select count(*) count, exposureTime value, make, model, lens 
            from ImageMetadata
            where make = ? and model = ? and lens = ?
            group by exposureTime, make, model, lens
            order by exposureTime""";
    
    private static final String STAT_FOCAL_LENGTH =
            """
            select count(*) count, focalLength35 value, make, model, lens 
            from ImageMetadata
            where make = ? and model = ? and lens = ?
            group by focalLength35, make, model, lens
            order by focalLength35""";
    
    private static final String STAT_ISO =
            """
            select count(*) count, sensitivity value, make, model, lens 
            from ImageMetadata
            where make = ? and model = ? and lens = ?
            group by sensitivity, make, model, lens
            order by sensitivity""";
    
    private static final String STAT_APERTURE =
            """
            select count(*) count, aperture value, make, model, lens 
            from ImageMetadata
            where make = ? and model = ? and lens = ?
            group by aperture, make, model, lens
            order by aperture""";
    
    public List<Point.Count<Double>> getExposureTime(String make, String model, String lens) {
        List<DiagramView> list = getStat(make, model, lens, STAT_EXPOSURE_TIME);
        List<Point.Count<Double>> res = new ArrayList<>();
        for (DiagramView d : list) {
            res.add(new Point.CountExposureTime(d.getId().getValue(), d.getId().getCount()));
        }
        return res;
    }
    
    public List<Point.Count<Double>> getIso(String make, String model, String lens) {
        List<DiagramView> list = getStat(make, model, lens, STAT_ISO);
        List<Point.Count<Double>> res = new ArrayList<>();
        for (DiagramView d : list) {
            res.add(new Point.CountIso(d.getId().getValue(), d.getId().getCount()));
        }
        return res;
    }
    
    public List<Point.Count<Double>> getAperture(String make, String model, String lens) {
        List<DiagramView> list = getStat(make, model, lens, STAT_APERTURE);
        List<Point.Count<Double>> res = new ArrayList<>();
        for (DiagramView d : list) {
            res.add(new Point.CountIso(d.getId().getValue(), d.getId().getCount()));
        }
        return res;
    }
    
    public List<Point.Count<Double>> getFocalLenth(String make, String model, String lens) {
        List<DiagramView> list = getStat(make, model, lens, STAT_FOCAL_LENGTH);
        List<Point.Count<Double>> res = new ArrayList<>();
        for (DiagramView d : list) {
            res.add(new Point.Count<>(d.getId().getValue(), d.getId().getCount()));
        }
        return res;
    }
    
    private List<DiagramView> getStat(String make, String model, String lens, String statement) {
        Query query = getEntityManager().createNativeQuery(statement, DiagramView.class);
        query.setParameter(1, make);
        query.setParameter(2, model);
        query.setParameter(3, lens);
        return query.getResultList();
    }
 
    
    void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
