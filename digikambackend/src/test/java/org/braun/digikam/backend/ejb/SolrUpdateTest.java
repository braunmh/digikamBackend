package org.braun.digikam.backend.ejb;

import org.braun.digikam.backend.dao.TagsFacade;
import org.braun.digikam.backend.dao.ThumbnailFacade;
import org.braun.digikam.common.DateWrapper;
import org.braun.digikam.backend.entity.ImageFull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.dao.ImageCopyrightFacade;
import org.braun.digikam.backend.dao.ImageInformationFacade;
import org.braun.digikam.backend.dao.ImageMetadataFacade;
import org.braun.digikam.backend.dao.ImagesFacade;
import org.braun.digikam.backend.entity.ThumbnailToGenerate;
import org.braun.digikam.backend.entity.VideoFull;
import org.braun.digikam.backend.model.ImageSolr;
import org.braun.digikam.backend.model.Media;
import org.braun.digikam.backend.model.MediaSolr;
import org.braun.digikam.backend.search.solr.SolrQueryBuilder;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.junit.jupiter.api.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.io.WKTReader;
import org.locationtech.spatial4j.shape.Shape;

/**
 *
 * @author mbraun
 */
public class SolrUpdateTest {

    Logger LOG = LogManager.getLogger();
    private EntityManager em;
    private TagsFacade tagsFacade;
    private String solrCollection = "digikam1";

    //@Test
    public void testLocation() {
        String location = "POINT(176.29444444444442 68.56194444444445)";
        SpatialContextFactory factory = new SpatialContextFactory();
        WKTReader reader = new WKTReader(SpatialContext.GEO, factory);
        try {
            Shape shape = reader.read(location);
            System.out.println(shape);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @Test
    public void updateImages() {
        EntityTransaction userTransaction = null;
        try {
            em = getEntityManager();
            userTransaction = em.getTransaction();
            String sqlStatement = """
            SELECT i.id, substr(ar.identifier, 16) root, a.relativePath, i.name,
                i.modificationDate, ii.orientation, null as data, ii.width, ii.height, t.imageId
            FROM AlbumRoots ar inner join Albums a on (ar.id = a.albumRoot) inner join Images i on a.id = i.album
                inner join ImageInformation ii on i.id = ii.imageId inner join ImageMetadata im on ii.imageid = im.imageid
                left join Thumbnail t on i.id = t.imageId
            where (ii.creationDate between ?1 and ?2) 
            order by 3, 4"""; // createNativeQuery
            Query query = em.createNativeQuery(sqlStatement, ThumbnailToGenerate.class)
                    .setParameter(1, newDate(2025, 5, 30, 0, 0, 0))
                    .setParameter(2, newDate(2025, 5, 30, 23, 59, 59));
            List<ThumbnailToGenerate> result = query.getResultList();

            HouseKeepingFacade houseKeepingFacade = new HouseKeepingFacade();
            houseKeepingFacade.setEntityManager(em);
            houseKeepingFacade.setThumbnailFacade(getThumbnailFacade(em));
            houseKeepingFacade.setTagsFacade(getTagsFacade(em));
            houseKeepingFacade.setImageCopyrightFacade(getImageCopyrightFacade(em));
            houseKeepingFacade.setImageFacade(getImageFacade(em));
            houseKeepingFacade.setImageMetadataFacade(getImageMetadataFacade(em));
            houseKeepingFacade.setImagesFacade(getImagesFacade(em));
            
            int cnt = 0;
            try (SolrClient client = getSolrClient()) {
                cnt = houseKeepingFacade.handleImages(result, client, new CommonTransaction(userTransaction), solrCollection);
            }
            System.out.println("Number of images handled: " + cnt);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } 
    }

    Date newDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        return calendar.getTime();
    }
    
    ImagesFacade imagesFacade;
    ImagesFacade getImagesFacade(EntityManager em) {
        if (imagesFacade == null) {
            imagesFacade = new ImagesFacade();
            imagesFacade.setEnitityManager(em);
            imagesFacade.setImageInformationFacade(getImageInformationFacade(em));
        }
        return imagesFacade;
    }
    ImageInformationFacade imageInformationFacade; 
    ImageInformationFacade getImageInformationFacade(EntityManager em) {
        if (imageInformationFacade == null) {
            imageInformationFacade = new ImageInformationFacade();
            imageCopyrightFacade.setEntityManager(em);
        }
        return imageInformationFacade;
    } 
    ImageMetadataFacade imageMetadataFacade;
    ImageMetadataFacade getImageMetadataFacade(EntityManager em) {
        if (imageMetadataFacade == null) {
            imageMetadataFacade = new ImageMetadataFacade();
            imageMetadataFacade.setEntityManager(em);
        }
        return imageMetadataFacade;
    }
    
    
    TagsFacade getTagsFacade(EntityManager em) {
        if (tagsFacade == null) {
            tagsFacade = new TagsFacade();
            tagsFacade.setEntityManager(em);
            NodeFactory.getInstance().refresh(tagsFacade.findAll());
        }
        return tagsFacade;
    }

    ThumbnailFacade thumbnailFacade;

    ThumbnailFacade getThumbnailFacade(EntityManager em) {
        if (thumbnailFacade == null) {
            thumbnailFacade = new ThumbnailFacade();
            thumbnailFacade.setEntityManger(em);
        }
        return thumbnailFacade;
    }
    ImageCopyrightFacade imageCopyrightFacade;

    ImageCopyrightFacade getImageCopyrightFacade(EntityManager em) {
        if (imageCopyrightFacade == null) {
            imageCopyrightFacade = new ImageCopyrightFacade();
            imageCopyrightFacade.setEntityManager(em);
        }
        return imageCopyrightFacade;
    }

    ImageFacade imageFacade;

    ImageFacade getImageFacade(EntityManager em) {
        if (imageFacade == null) {
            imageFacade = new ImageFacade();
            imageFacade.setEntityManager(em);
            imageFacade.setThumbnailFacade(getThumbnailFacade(em));
            imageFacade.setImagesFacade(getImagesFacade(em));
        }
        return imageFacade;
    }

    //@Test
    public void deleteAvis() {
        int deleted = 0;
        try (SolrClient client = getSolrClient()) {

            SolrQueryBuilder builder = new SolrQueryBuilder()
                    .addField("id")
                    .addField("name")
                    .addQuery("type", 2)
                    .addQuery("id", Arrays.asList("154458", "154459", "154460", "154461", "154462", "154463", "154464", "154465", "154466", "154467",
                            "154480", "154482", "154483", "154484", "154485", "154486", "154487", "154488",
                            "154490", "154491", "154492", "154493"))
                    //                .addQuery("name", "*.AVI")
                    .setRows(100);

            SolrQuery query = builder.build();
            query.addSort("id", SolrQuery.ORDER.asc);
            QueryResponse response = client.query(solrCollection, query);
            List<MediaSolr> result = response.getBeans(MediaSolr.class);
            deleted = result.size();
            for (MediaSolr m : result) {
                client.deleteById(solrCollection, m.getId());
            }
            client.commit(solrCollection);
        } catch (IOException | SolrServerException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            System.out.println(String.format("Number of Images deleted=%s", deleted));
        }
    }

    //@Test
    public void updateImageTest() {
        em = getEntityManager();

        NodeFactory.getInstance().refresh(getTagsFacade(em).findAll());

        String sql
                = """
            select * from ImageFull where creationDate between {d '2025-05-30'} and {d '2025-06-01'}
            """;
        // skipped 89476 'location'='176.29444444444442,68.56194444444445'
        List<ImageFull> source = em.createNativeQuery(sql, ImageFull.class).getResultList();

        String relativePath = null;
        String name = null;
        try (SolrClient client = getSolrClient();) {
            int cnt = 0;
            for (ImageFull imageFull : source) {
                relativePath = imageFull.getRelativePath();
                name = imageFull.getName();
                ImageSolr image = new ImageSolr(getImageFacade(em).getMetadata(imageFull));
                final UpdateResponse response = client.addBean(solrCollection, image);
                cnt++;
                if (cnt % 200 == 0) {
                    client.commit(solrCollection);
                    System.out.println("Last image processed: " + image.getId() + " Number of Documents indexed: " + cnt);
                }
            }
            client.commit(solrCollection);
        } catch (IOException | SolrServerException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            System.out.println(String.format("Number of Images updated=%s, %s/%s", source.size(), relativePath, name));
        }
    }

    //@Test
    public void updateVideoTest() {
        em = getEntityManager();

        tagsFacade = new TagsFacade();
        tagsFacade.setEntityManager(em);
        NodeFactory.getInstance().refresh(tagsFacade.findAll());

        VideoFacade videoFacade = new VideoFacade();
        videoFacade.setEntityManager(em);

        String sql
                = """
            select f.* from VideoFull f
            """;
        // skipped 89476 'location'='176.29444444444442,68.56194444444445'
        List<VideoFull> source = em.createNativeQuery(sql, VideoFull.class).getResultList();

        String relativePath = null;
        String name = null;
        String solrCollection = "digikam1";
        try (SolrClient client = getSolrClient();) {
            int cnt = 0;
            for (VideoFull videoFull : source) {
                relativePath = videoFull.getRelativePath();
                name = videoFull.getName();
                ImageSolr image = new ImageSolr(videoFacade.getMetadata(videoFull));
                final UpdateResponse response = client.addBean(solrCollection, image);
                cnt++;
                if (cnt % 200 == 0) {
                    client.commit(solrCollection);
                    System.out.println("Last video processed: " + image.getId() + " Number of Documents indexed: " + cnt);
                }
            }
            client.commit(solrCollection);
        } catch (IOException | SolrServerException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            System.out.println(String.format("Number of Videos updated=%s, %s/%s", source.size(), relativePath, name));
        }
    }

    // @Test
    public void testFind() {
        Date now = new Date();
        em = getEntityManager();
        tagsFacade = new TagsFacade();
        tagsFacade.setEntityManager(em);
        NodeFactory.getInstance().refresh(tagsFacade.findAll());
        try (SolrClient client = getSolrClient();) {
            SolrQuery query = new SolrQueryBuilder()
                    .addField("id")
                    .addField("name")
                    .addField("creationDate")
                    .addField("type")
                    .addField("score")
                    .addQuery("creationDate", new DateWrapper("202407--"))
                    //                .addQuery("creator", "Michael Braun")
                    .addQueryLong("keywordIds", Arrays.asList(8724L))
                    .build();
            QueryResponse response = client.query("digikam", query);
            System.out.println("Number of Documents found: " + response.getResults().getNumFound());
            List<MediaSolr> result = response.getBeans(MediaSolr.class);
            for (MediaSolr is : result) {
                Media media = is.toMedia();
                System.out.println(media.getId() + ", type:" + is.getType()
                        + ", creationDate:" + is.getCreationDate()
                        + ", name:" + is.getName() + ", score:" + media.getScore());
            }
        } catch (IOException | SolrServerException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private SolrClient getSolrClient() {
        final String solrUrl = "http://localhost:8983/solr";
        return new Http2SolrClient.Builder(solrUrl).build();
    }

    protected EntityManager getEntityManager() {
        try {
            final Map<String, Object> props = new HashMap<>();
            props.put(PersistenceUnitProperties.TRANSACTION_TYPE, PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
            props.put(PersistenceUnitProperties.JDBC_DRIVER, "com.mysql.cj.jdbc.Driver");
            props.put(PersistenceUnitProperties.JDBC_URL, "jdbc:mysql://192.168.0.219:3306/digikam4?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
            props.put(PersistenceUnitProperties.JDBC_USER, "mbraun");
            props.put(PersistenceUnitProperties.JDBC_PASSWORD, "gesa0403");
            props.put("eclipselink.id-validation", "NULL");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("digikam", props);
            return emf.createEntityManager();

        } catch (Exception e) {
            LOG.error(e.getMessage());
            return null;
        }
    }
}
