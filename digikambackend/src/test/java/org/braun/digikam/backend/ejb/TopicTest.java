package org.braun.digikam.backend.ejb;

import jakarta.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.backend.util.Configuration;
import org.braun.digikam.common.DateWrapper;
import org.braun.digikam.web.model.SearchParameter;
import org.braun.digikam.web.model.TopicView;
import org.junit.jupiter.api.Test;
import org.braun.digikam.web.model.TopicDisplayView;

/**
 *
 * @author mbraun
 */
public class TopicTest extends BaseTest {

    //@Test
    public void findView() {
        try {
            EntityManager em = getEntityManager();
            NodeFactory.getInstance().refresh(getTagsFacade(em).findAll());
            Configuration.init(this.getClass().getClassLoader().getResourceAsStream("config.xml"));
            TopicFacade topicFacade = getTopicFacade(em);
            TopicDisplayView v = topicFacade.findViewById(2L);
            System.out.println(v.getEntries().size());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    //@Test
    public void selectAll() {
        try {
            TopicFacade tf = getTopicFacade(getEntityManager());
            List<TopicView> res = tf.findAll();
            for (TopicView tv : res) {
                System.out.println(tv.getId() + ", " + tv.getYear() + ", " + tv.getKeyword().getName() + ", " + tv.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @Test
    public void dateTest() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.YEAR, 2020);
        now.set(Calendar.MONTH, 6);
        now.set(Calendar.DATE, 5);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 27);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        Date date = now.getTime();
        
        System.out.println(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        System.out.println(simpleDateFormat.format(date));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime ldt = date.toInstant().atZone(ZoneId.of("Europe/Paris")).toLocalDateTime();
        System.out.println(formatter.format(ldt));
    }
    //@Test
    public void updateBegin() {
        EntityManager em = getEntityManager();
        NodeFactory.getInstance().refresh(getTagsFacade(em).findAll());
        TopicFacade tf = getTopicFacade(em);
        List<TopicView> topics = tf.findAll();
        em.getTransaction().begin();
        for (TopicView tv : topics) {
            if (tv.getSearchParameter().getDate().getFrom().isEmpty()) {
                continue;
            }
            tv.setBegin(tv.getSearchParameter().getDate().getFrom().getLowerBound());
            tf.update(tv);
        }
        em.getTransaction().commit();
    }

    public void insert() {
        try {
            EntityManager em = getEntityManager();
            NodeFactory.getInstance().refresh(getTagsFacade(em).findAll());
            List<Keyword> keywords = NodeFactory.getInstance().getKeywordByQualifiedName("_manuell/Freizeit/Urlaub");
            if (keywords.size() == 1) {
                em.getTransaction().begin();
                TopicFacade tf = getTopicFacade(em);
                TopicView v = new TopicView()
                        .keyword(keywords.get(0))
                        .title("Fahrradtour Provence mit Karin")
                        .year(2006)
                        .content("""
                                 <h1>Organisierte Fahrrad-Tout in der Provence mit Karin</h1>
                                 <p>Die Fahrrad-Tour wurde durch Schimmelreisen betreut.</p>
                                 """);
                SearchParameter sp = v.getSearchParameter();
                sp.getDate().setFrom(new DateWrapper("20070601"));
                sp.getDate().setTo(new DateWrapper("20070604"));
                sp.getKeywords().add(keywords.get(0));
                v.setBegin(sp.getDate().getFrom().getLowerBound());
                v = tf.insert(v);
                em.getTransaction().rollback();
                System.out.println(v.getId() + ", " + v.getTitle() + ", " + v.getContent());
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
