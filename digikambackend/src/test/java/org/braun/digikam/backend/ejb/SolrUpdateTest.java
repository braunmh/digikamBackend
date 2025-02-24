package org.braun.digikam.backend.ejb;

import org.braun.digikam.common.DateWrapper;
import org.braun.digikam.backend.entity.ImageFull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
import org.braun.digikam.backend.entity.Thumbnail;
import org.braun.digikam.backend.entity.ThumbnailToGenerate;
import org.braun.digikam.backend.graphics.ImageUtil;
import org.braun.digikam.backend.graphics.Orientation;
import org.braun.digikam.backend.model.ImageInternal;
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

    //@Test
    public void regenerateThumbnails() {
        EntityTransaction userTransaction = null;
        try {
            em = getEntityManager();
            ThumbnailFacade thumbnailFacade = new ThumbnailFacade();
            thumbnailFacade.setEntityManger(em);
            userTransaction = em.getTransaction();
            String sqlStatement = """
            SELECT i.id, substr(ar.identifier, 16) root, a.relativePath, i.name,
                i.modificationDate, ii.orientation, null as data, ii.width, ii.height, t.imageId
            FROM AlbumRoots ar inner join Albums a on (ar.id = a.albumRoot) inner join Images i on a.id = i.album
                inner join ImageInformation ii on i.id = ii.imageId inner join ImageMetadata im on ii.imageid = im.imageid
                left join Thumbnail t on i.id = t.imageId
            where (ii.height > ii.width and i.modificationDate between '2024-01-01 00:00:00' and '2024-12-31 23:59:59') 
            order by 3, 4""";
            Query query = em.createNativeQuery(sqlStatement, ThumbnailToGenerate.class);
            List<ThumbnailToGenerate> result = query.getResultList();
            userTransaction.begin();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int cnt = 0;
            for (ThumbnailToGenerate thumbToGenerate : result) {
                LOG.info(thumbToGenerate);
                Thumbnail thumbnail = thumbToGenerate.getThumbnail();
                File imageFile = new File(thumbToGenerate.getPath());
                if (!imageFile.exists()) {
                    continue;
                }
                try {
                    Orientation orientation = Orientation.parse(thumbToGenerate.getOrientation());
                    baos.reset();
                    ImageUtil.scaleImage(imageFile, baos, 1024, 1024, orientation);
                    if (thumbnail.getData() == null) {
                        thumbnail.setData(baos.toByteArray());
                        thumbnailFacade.create(thumbnail);
                    } else {
                        thumbnail.setData(baos.toByteArray());
                        thumbnailFacade.merge(thumbnail);
                    }
                } catch (IOException e) {
                    LOG.info("Thumbnail-Generation for image {} skipped. Msg: {}", thumbToGenerate.getId(), e.getMessage());
                }
                if (cnt++ % 100 == 0) {
                    userTransaction.commit();
                    userTransaction.begin();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            if (userTransaction != null) {
                userTransaction.commit();
            }
        }
    }

    @Test
    public void updateTest() {
        em = getEntityManager();

        tagsFacade = new TagsFacade();
        tagsFacade.setEntityManager(em);
        NodeFactory.getInstance().refresh(tagsFacade.findAll());

        ImageFacade imageFacade = new ImageFacade();
        imageFacade.setEntityManager(em);

        String sql = 
            """
            select * from ImageFull f where f.lens = 'LAOWA 90mm f/2,8 2x Ultra Macro APO'
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
                ImageSolr image = new ImageSolr(imageFacade.getMetadata(imageFull));
                final UpdateResponse response = client.addBean("digikam", image);
                cnt++;
                if (cnt % 200 == 0) {
                    client.commit("digikam");
                    System.out.println("Last image processed: " + image.getId() + " Number of Documents indexed: " + cnt);
                }
                LOG.debug(response.jsonStr());
            }
            client.commit("digikam");
        } catch (IOException | SolrServerException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            System.out.println(String.format("Number of Images updated=%s, %s/%s", source.size(), relativePath, name));
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
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("digikam", props);
            return emf.createEntityManager();

        } catch (Exception e) {
            LOG.error(e.getMessage());
            return null;
        }
    }
}
