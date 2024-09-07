package org.braun.digikam.backend.ejb;

import org.braun.digikam.backend.entity.Tags;
import org.braun.digikam.backend.entity.Images;
import jakarta.annotation.Resource;
import jakarta.ejb.AsyncResult;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.EJBContext;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.braun.digikam.backend.Node;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.StatusFactory;
import org.braun.digikam.backend.entity.Thumbnail;
import org.braun.digikam.backend.entity.ThumbnailToGenerate;
import org.braun.digikam.backend.graphics.ImageUtil;
import org.braun.digikam.backend.graphics.Orientation;
import org.braun.digikam.backend.model.ImageSolr;
import org.braun.digikam.backend.util.Configuration;

/**
 *
 * @author mbraun
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class HouseKeepingFacade {

    private static final Logger LOG = LogManager.getLogger();

    @PersistenceContext(unitName = "digikam")
    private EntityManager em;

    @Resource
    private EJBContext context;

    @Inject
    private ImageFacade imageFacade;

    @Inject
    private ImagesFacade imagesFacade;

    @Inject
    private ThumbnailFacade thumbnailFacade;

    @Inject
    private TagsFacade tagsFacade;

    @Asynchronous
    public Future<Integer> generateTumbnailsTagImages() {
        long startTst = System.currentTimeMillis();
        List<ThumbnailToGenerate> result = findImages();
        int generated = 0;
        List<Node> nodes = NodeFactory.getInstance().list();
        UserTransaction userTransaction = context.getUserTransaction();
        final String solrCollection = Configuration.getInstance().getSolrCollection();
        try (SolrClient client = getSolrClient();
            JsonReader reader = Json.createReader(this.getClass().getClassLoader().getResourceAsStream("org/braun/digikam/backend/override_labels.json"));) {
            Map<String, String> autoKeywords = new HashMap<>();
            JsonObject jo = reader.readObject();
            for (Map.Entry<String, JsonValue> entry : jo.entrySet()) {
                autoKeywords.put(entry.getKey(), entry.getValue().toString());
            }
            userTransaction.begin();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
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
                    ImageUtil.getImage(imageFile, baos, 1024, 1024, orientation);
                    if (thumbnail.getData() == null) {
                        thumbnail.setData(baos.toByteArray());
                        thumbnailFacade.create(thumbnail);
                    } else {
                        thumbnail.setData(baos.toByteArray());
                        thumbnailFacade.merge(thumbnail);
                    }
                } catch (IOException e) {
                    LOG.info("Thumbnail-Generation for image {} skipped", thumbToGenerate.getId());
                }
                List<String> autoTags = getTags(imageFile, autoKeywords);
                if (!autoTags.isEmpty()) {
                    Images image = imagesFacade.find(thumbToGenerate.getId());
                    List<Tags> tags = new ArrayList<>();
                    for (String autoTag : autoTags) {
                        Tags t = lookupTag(nodes, autoTag);
                        if (t == null) {
                            continue;
                        }
                        tags.add(t);
                    }
                    imagesFacade.addTag(image, tags);
                }
                ImageSolr image = new ImageSolr(imageFacade.getMetadata(thumbToGenerate.getId()));
                final UpdateResponse response = client.addBean(solrCollection, image);
                generated++;
                if (generated % 5 == 0) {
                    userTransaction.commit();
                    client.commit(solrCollection);
                    userTransaction.begin();
                }
            }
            userTransaction.commit();
            client.commit(solrCollection);
        } catch (NotSupportedException | SystemException | RollbackException
            | HeuristicMixedException | HeuristicRollbackException | IOException e) {
            LOG.error("Thumbnail-Generation and tagging failed", e);
        } catch (Exception e) {
            LOG.error("Thumbnail-Generation and tagging failed", e);
        }
        LOG.info("Thumbnail-Generation and tagging {} Images took {} seconds",
            generated, (System.currentTimeMillis() - startTst) / 1000);
        StatusFactory.getInstance().aquireTumbnailGenerationStatusDone();
        return new AsyncResult<>(generated);
    }

    private SolrClient getSolrClient() {
        final String solrUrl = Configuration.getInstance().getSolrClientUrl();
        return new Http2SolrClient.Builder(solrUrl).build();
    }
    
    private List<ThumbnailToGenerate> findImages() {
        String sqlStatement = "SELECT i.id, substr(ar.identifier, 16) root, a.relativePath, i.name,\n"
            + "i.modificationDate, ii.orientation, null as data, ii.width, ii.height, t.imageId\n"
            + "FROM AlbumRoots ar inner join Albums a on (ar.id = a.albumRoot) inner join Images i on a.id = i.album\n"
            + "inner join ImageInformation ii on i.id = ii.imageId inner join ImageMetadata im on ii.imageid = im.imageid\n"
            + "left join Thumbnail t on i.id = t.imageId\n"
            + "where (t.imageId is null or t.modificationDate < i.modificationDate)\n"
            + "and ii.width > 1024";
        Query query = getEntityManager().createNativeQuery(sqlStatement, ThumbnailToGenerate.class);
        return query.getResultList();
    }

    private EntityManager getEntityManager() {
        return em;
    }

    private Tags lookupTag(List<Node> nodes, String tagTree) {
        Optional<Node> tag = nodes.stream().filter(n -> tagTree.equals(n.getFullName())).findFirst();
        if (tag.isPresent()) {
            Tags tags = new Tags();
            tags.setId(tag.get().getId());
            return tags;
        }
        Tags t = tagsFacade.findAndInsertTagByTree(tagTree.split("\\|"));
        Node n = new Node(t.getId(), t.getName());
        n.setFullName(tagTree);
        nodes.add(n);
        return t;
    }

    private List<String> getTags(File image, Map<String, String> translatedKeywords) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
            "/opt/photils-cli-0.4.1-linux-x86_64.AppImage",
            "--image",
            image.getPath(),
            "--with_confidence"
        );
        Process process = pb.start();
        return watch(process, translatedKeywords);
    }

    private synchronized List<String> watch(Process process, Map<String, String> translatedKeywords) {
        List<String> tags = new ArrayList<>();
        new Thread() {
            @Override
            public void run() {
                try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = input.readLine()) != null) {
                        if (line.contains("Örtlichkeit")) {
                            continue;
                        }
                        TagWeighted tw = TagWeighted.parse(line, translatedKeywords);
                        if (tw.weight >= 0.99 && !"Örtlichkeit".equals(tw.name)) {
                            tags.add("auto|" + tw.name);
                        }
                    }
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            LOG.error("Thread for getting Tags interrupted.", e);
        }
        return tags;
    }

    public static class TagWeighted {

        String name;
        double weight;

        public static TagWeighted parse(String line, Map<String, String> translatedKeywords) {
            String[] parts = line.split(":");
            TagWeighted tw = new TagWeighted();
            tw.name = translatedKeywords.getOrDefault(parts[0], parts[0]);
            if (parts.length >= 1) {
                try {
                    tw.weight = Double.parseDouble(parts[1]);
                } catch (NumberFormatException e) {
                    tw.weight = 0d;
                }
            } else {
                tw.weight = 0d;
            }
            return tw;
        }

        @Override
        public String toString() {
            return name + " (" + weight + ")";
        }

    }

}
