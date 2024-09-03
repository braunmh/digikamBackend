package org.braun.digikam.backend.ejb;

import org.braun.digikam.backend.entity.Albums;
import org.braun.digikam.backend.entity.Tags;
import org.braun.digikam.backend.entity.Images;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Logger;
import org.braun.digikam.backend.Node;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.api.NotFoundException;
import org.braun.digikam.backend.model.Image;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.backend.model.Media;
import org.braun.digikam.backend.search.ConditionParseException;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.junit.jupiter.api.Test;

/**
 *
 * @author mbraun
 */
public class TagsFacadeTest {

    Logger LOG = Logger.getLogger(TagsFacadeTest.class.getName());

    public TagsFacadeTest() {
    }

    //@Test
    public void insertTagsTree() {
        try {
            JsonReader reader = Json.createReader(new FileReader("/home/mbraun/.local/share/photils/override_labels.json"));
            JsonObject jo = reader.readObject();
            TagsFacade tf = new TagsFacade();
            EntityManager em = getEntityManager();
            tf.setEntityManager(em);
            em.getTransaction().begin();
            int k=0;
            for (String key : jo.keySet()) {
                String tagTree = ("auto|" + jo.getString(key));
                long id = tf.findAndInsertTagByTree(tagTree.split("\\|")).getId();
                System.out.println(id + " -> " +tagTree);
            }
            em.getTransaction().commit();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
    
    private ImagesFacade imagesFacade;
    private AlbumsFacade albumsFacade;
    private TagsFacade tagsFacade;
    private Map<String, Tags> tagsCached = new HashMap<>();
    private EntityManager em;
    
    @Test
    public void validateAutoTags() {
        em = getEntityManager();
        tagsFacade = new TagsFacade();
        tagsFacade.setEntityManager(em);
        List<PhotilsTag> result = new ArrayList<>();
        NodeFactory.getInstance().refresh(tagsFacade.findAll());
        List<Node> nodes = NodeFactory.getInstance().list();
        try (InputStream is = new FileInputStream("/home/mbraun/.local/share/photils/override_labels.json");
            JsonReader reader = Json.createReader(is);) {
            JsonObject jObject = reader.readObject();
            for (String key : jObject.keySet()) {
                String value = "/auto/" + jObject.getJsonString(key).getString().replaceAll("\\|", "/");
                if (!nodes.stream().anyMatch(p -> p.getFullName().equals(value))) {
                    result.add(new PhotilsTag().name(key).translated(value));
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        if (!result.isEmpty()) {
            Collections.sort(result);
            for (PhotilsTag pt : result) {
                System.out.println(pt);
            }
        }
    }
    
    class PhotilsTag implements Comparable<PhotilsTag> {
        String name;
        String translated;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        public PhotilsTag name(String value) {
            name = value;
            return this;
        }

        public String getTranslated() {
            return translated;
        }

        public void setTranslated(String translated) {
            this.translated = translated;
        }
        
        public PhotilsTag translated(String value) {
            translated = value;
            return this;
        }

        @Override
        public int compareTo(PhotilsTag o) {
            if (o == null) {
                return 1;
            }
            return translated.compareTo(o.getTranslated());
        }

        @Override
        public String toString() {
            return "PhotilsTag{" + "name=" + name + ", translated=" + translated + '}';
        }
    }
    
    /**
     * Associate Images to Tags 
     */
    //@Test
    public void provideTags() {
        em = getEntityManager();
        String root = "/data/pictures/";
        String[] relPaths = new String[] {"2024/05/", "2024/06/"};
        imagesFacade = new ImagesFacade();
        imagesFacade.setEnitityManager(em);
        albumsFacade = new AlbumsFacade();
        albumsFacade.setEnitityManager(em);
        tagsFacade = new TagsFacade();
        tagsFacade.setEntityManager(em);
        int cnt = 0;
        for (String relPath : relPaths) {
            long start = System.currentTimeMillis();
            cnt = traversTree(root, new File(root + relPath));
            long duration = System.currentTimeMillis() - start;
            System.out.println("Tagging of " + relPath + " tooks " + Duration.ofMillis(duration).toString());
            System.out.println("Number of images tagged " + cnt + ". Seconds/Image: " + ((float)duration / cnt / 1000));
        }
    }
    
    private String formatTimestamp(long milliseconds) {
        Instant instant = Instant.ofEpochMilli(milliseconds);
        ZoneId defaultZone = ZoneId.systemDefault();
        LocalDateTime ofInstant = LocalDateTime.ofInstant(instant, defaultZone);
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(ofInstant);
    }
    
    private int traversTree(String root, File directory) {
        String relPath = directory.getPath().substring(root.length() - 1);
        Albums album = albumsFacade.findByRelPath(relPath);
        int cnt = 0;
        if (album == null) {
            return cnt;
        }
        File[] sorted = directory.listFiles();
        Arrays.sort(sorted, new FileNameSorter());
        for (File entry : sorted) {
            if (entry.isDirectory()) {
                cnt = cnt + traversTree(root, entry);
            } else {
                if (!entry.getName().toLowerCase().endsWith(".jpg") && !entry.getName().toLowerCase().endsWith(".jpeg")) {
                    continue;
                }
                try {
                    List<String> tags = getTags(entry);
                    if (tags.isEmpty()) {
                        continue;
                    }
                    Images image = imagesFacade.findByNameAndAlbum(entry.getName(), album.getId());
                    if (image == null) {
                        continue;
                    }
                    cnt++;
                    em.getTransaction().begin();
                    List<Tags> newTags = new ArrayList<>();
                    for (String tag : tags) {
                        Tags t = lookupTag(tag);
                        if (t != null && t.getId() > 0) {
                            newTags.add(t);
                        }
                    }
                    System.out.println(image.getId() + ":" + relPath + ", " + entry.getName());
                    System.out.println("=> " + String.join(",", tags));
                    imagesFacade.addTag(image, newTags);
                    em.getTransaction().commit();
                } catch (IOException e) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    e.printStackTrace(System.out);
                }
            }
        }
        return cnt;
    }
    
    private Tags lookupTag(String tagTree) {
        if (tagsCached.containsKey(tagTree)) {
            return tagsCached.get(tagTree);
        }
        Tags t = tagsFacade.findAndInsertTagByTree(tagTree.split("\\|"));
        tagsCached.put(tagTree, t);
        return t;
    }
    
    private List<String> getTags(File image) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
            "/opt/photils-cli-0.4.1-linux-x86_64.AppImage",
            "--image",
            image.getPath(),
            "--with_confidence"
        );
        Process process = pb.start();
        return watch(process);
    }

    private synchronized List<String> watch(Process process) {
        List<String> tags = new ArrayList<>();
        new Thread() {
            public void run() {
                try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = input.readLine()) != null) {
                        if (line.contains("Örtlichkeit")) {
                            continue;
                        }
                        TagWeighted tw = TagWeighted.parse(line);
                        if (tw.weight >= 0.99 && !"Örtlichkeit".equals(tw.name)) {
                            tags.add("auto|" + tw.name);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
            }
        }.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return tags;
    }
    
    class FileNameSorter implements Comparator<File> {

        @Override
        public int compare(File f1, File f2) {
            return f1.getName().compareTo(f2.getName());
        }
        
    }
        

    public static class TagWeighted {

        String name;
        double weight;

        public static TagWeighted parse(String line) {
            String[] parts = line.split(":");
            TagWeighted tw = new TagWeighted();
            tw.name = parts[0];
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
    
    //@Test
    public void findAll() {
        TagsFacade tf = new TagsFacade();
        EntityManager em = getEntityManager();
        tf.setEntityManager(em);
        List<Tags> list = tf.findAll();
        NodeFactory.getInstance().refresh(list);
        List<Keyword> result = NodeFactory.getInstance().getKeywordByName("Mich");
        for (Keyword k : result) {
            LOG.fine(k.getName());
        }
        ImageMetadataFacade im = new ImageMetadataFacade();
        im.setEntityManager(em);
        for (String lens : im.getLens("SONY", "SLT-A99V")) {
            LOG.fine(lens);
        }

        ImageFacade imageFacade = new ImageFacade();
        imageFacade.setEntityManager(em);
        try {
            Image image = imageFacade.getMetadata(112280);
            LOG.info(image.toString());
        } catch (NotFoundException e) {
            LOG.severe(e.getMessage());
        }

        List<Keyword> pilz = NodeFactory.getInstance().getKeywordByName("Natur");
        try {
            List<Long> keywords = Arrays.asList(pilz.get(0).getId());
            String creator = "Michael Braun";
            String make = null;
            String model = null;
            String lens = null;
            String orientation = null;

            String dateFrom = null;
            String dateTo = null;
            Integer ratingFrom = 2;
            Integer ratingTo = null;
            Integer isoFrom = null;
            Integer isoTo = null;

            Double exposureTimeFrom = null;
            Double exposureTimeTo = null;
            Double apertureFrom = null;
            Double apertureTo = null;

            Integer focalLengthFrom = null;
            Integer focalLengthTo = null;
            List<Media> res = imageFacade.findImagesByImageAttributes(keywords, false, creator, make, model, lens, orientation, dateFrom, dateTo, ratingFrom, ratingTo, isoFrom, isoTo, exposureTimeFrom, exposureTimeTo, apertureFrom, apertureTo, focalLengthFrom, focalLengthTo);
            for (Media ii : res) {
                LOG.fine(String.format("id=%s, date=%s", ii.getId(), ii.getCreationDate()));
            }
        } catch (ConditionParseException e)  {
            LOG.severe(e.getMessage());
        }
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
            LOG.severe(e.getMessage());
            return null;
        }
    }
}
