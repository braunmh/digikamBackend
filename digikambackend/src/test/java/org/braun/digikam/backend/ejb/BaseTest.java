package org.braun.digikam.backend.ejb;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.dao.ImageCopyrightDao;
import org.braun.digikam.backend.dao.ImageInformationDao;
import org.braun.digikam.backend.dao.ImageMetadataDao;
import org.braun.digikam.backend.dao.ImagesDao;
import org.braun.digikam.backend.dao.TagsDao;
import org.braun.digikam.backend.dao.ThumbnailDao;
import org.braun.digikam.backend.dao.TopicDao;
import org.eclipse.persistence.config.PersistenceUnitProperties;

/**
 *
 * @author mbraun
 */
public abstract class BaseTest {

    Logger LOG = LogManager.getLogger();

    protected String solrCollection = "digikam1";

    private EntityManager em;

    protected SolrClient getSolrClient() {
        final String solrUrl = "http://localhost:8983/solr";
        return new Http2SolrClient.Builder(solrUrl).build();
    }

    protected EntityManager getEntityManager() {
        if (em == null) {
            try {
                final Map<String, Object> props = new HashMap<>();
                props.put(PersistenceUnitProperties.TRANSACTION_TYPE, PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
                props.put(PersistenceUnitProperties.JDBC_DRIVER, "com.mysql.cj.jdbc.Driver");
                props.put(PersistenceUnitProperties.JDBC_URL, "jdbc:mysql://192.168.0.219:3306/digikam4?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
                props.put(PersistenceUnitProperties.JDBC_USER, "mbraun");
                props.put(PersistenceUnitProperties.JDBC_PASSWORD, "gesa0403");
                props.put("eclipselink.id-validation", "NULL");
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("digikam", props);
                em = emf.createEntityManager();
                return em;

            } catch (Exception e) {
                LOG.error(e.getMessage());
                return null;
            }
        }
        return em;
    }

    private TopicFacade topicFacade;

    protected TopicFacade getTopicFacade(EntityManager em) {
        if (topicFacade == null) {
            topicFacade = new TopicFacade();
            topicFacade.setEntityManager(em);
            TopicDao topicDao = new TopicDao();
            topicDao.setEntityManager(em);
            topicFacade.setTopicDao(topicDao);
            topicFacade.setImageFacade(getImageFacade(em));
            topicFacade.setTagsFacade(getTagsFacade(em));
        }
        return topicFacade;
    }

    private ImagesDao imagesDao;

    protected ImagesDao getImagesFacade(EntityManager em) {
        if (imagesDao == null) {
            imagesDao = new ImagesDao();
            imagesDao.setEnitityManager(em);
            imagesDao.setImageInformationFacade(getImageInformationFacade(em));
        }
        return imagesDao;
    }
    private ImageInformationDao imageInformationDao;

    protected ImageInformationDao getImageInformationFacade(EntityManager em) {
        if (imageInformationDao == null) {
            imageInformationDao = new ImageInformationDao();
            imageInformationDao.setEntityManager(em);
        }
        return imageInformationDao;
    }
    private ImageMetadataDao imageMetadataDao;

    protected ImageMetadataDao getImageMetadataFacade(EntityManager em) {
        if (imageMetadataDao == null) {
            imageMetadataDao = new ImageMetadataDao();
            imageMetadataDao.setEntityManager(em);
        }
        return imageMetadataDao;
    }

    private TagsDao tagsDao;

    protected TagsDao getTagsFacade(EntityManager em) {
        if (tagsDao == null) {
            tagsDao = new TagsDao();
            tagsDao.setEntityManager(em);
            NodeFactory.getInstance().refresh(tagsDao.findAll());
        }
        return tagsDao;
    }

    private ThumbnailDao thumbnailDao;

    protected ThumbnailDao getThumbnailFacade(EntityManager em) {
        if (thumbnailDao == null) {
            thumbnailDao = new ThumbnailDao();
            thumbnailDao.setEntityManger(em);
        }
        return thumbnailDao;
    }
    private ImageCopyrightDao imageCopyrightDao;

    protected ImageCopyrightDao getImageCopyrightFacade(EntityManager em) {
        if (imageCopyrightDao == null) {
            imageCopyrightDao = new ImageCopyrightDao();
            imageCopyrightDao.setEntityManager(em);
        }
        return imageCopyrightDao;
    }

    private ImageFacade imageFacade;

    protected ImageFacade getImageFacade(EntityManager em) {
        if (imageFacade == null) {
            imageFacade = new ImageFacade();
            imageFacade.setEntityManager(em);
            imageFacade.setThumbnailFacade(getThumbnailFacade(em));
            imageFacade.setImagesFacade(getImagesFacade(em));
        }
        return imageFacade;
    }

    private HouseKeepingFacade houseKeepingFacade;

    protected HouseKeepingFacade getHouseKeepingFacade(EntityManager em) {
        if (houseKeepingFacade == null) {
            houseKeepingFacade = new HouseKeepingFacade();
            houseKeepingFacade.setEntityManager(em);
            houseKeepingFacade.setThumbnailFacade(getThumbnailFacade(em));
            houseKeepingFacade.setTagsFacade(getTagsFacade(em));
            houseKeepingFacade.setImageCopyrightFacade(getImageCopyrightFacade(em));
            houseKeepingFacade.setImageFacade(getImageFacade(em));
            houseKeepingFacade.setImageMetadataFacade(getImageMetadataFacade(em));
            houseKeepingFacade.setImagesFacade(getImagesFacade(em));
        }
        return houseKeepingFacade;
    }

}
