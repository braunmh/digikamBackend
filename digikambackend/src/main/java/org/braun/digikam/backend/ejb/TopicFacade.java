package org.braun.digikam.backend.ejb;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.dao.TagsDao;
import org.braun.digikam.backend.dao.TopicDao;
import org.braun.digikam.backend.entity.Tags;
import org.braun.digikam.backend.entity.Topic;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.backend.model.Media;
import org.braun.digikam.backend.search.ConditionParseException;
import org.braun.digikam.backend.search.sql.SimpleCondition;
import org.braun.digikam.backend.search.sql.Sql;
import org.braun.digikam.backend.search.sql.TextCondition;
import org.braun.digikam.web.model.SearchParameter;
import org.braun.digikam.web.model.TopicView;
import org.braun.digikam.web.ui.TopicDisplayBean;
import org.braun.digikam.web.model.TopicDisplayView;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 *
 * @author mbraun
 */
@Stateless
public class TopicFacade {

    private static final Logger LOG = LogManager.getLogger();
    
    @PersistenceContext(unitName = "digikam")
    private EntityManager entityManager;
    
    @Inject
    TopicDao topicDao;

    @Inject
    private TagsDao tagsFacade;
    
    @Inject
    private ImageFacade imageFacade;

    public TopicView findById(Long id) {
        return findById(id, false);
    }

    public TopicView findById(Long id, boolean forDisplay) {
        TypedQuery<Topic> query = getEntityManager()
                .createQuery("Select t from Topic t where t.id = :id", Topic.class)
                .setParameter("id", id);
        List<Topic> t = query.getResultList();
        if (t.isEmpty()) {
            return new TopicView();
        }
        return map(t.get(0), forDisplay);
    }

    public TopicDisplayView findViewById(Long id) {
        TopicDisplayView view = new TopicDisplayView();
        if (id == null ) {
            return view;
        }
        TopicView topicView = findById(id, true);
        view.setDescription(topicView.getContent());
        view.setTitle(topicView.getTitle());
        List<Long> keywords = topicView.getSearchParameter().getKeywords().stream().map(k -> k.getId()).collect(Collectors.toList());
        try {
            List<Media> temp = imageFacade.findImagesByImageAttributesSolr(
                keywords, 
                topicView.getSearchParameter().isKeywordsOr(), 
                topicView.getSearchParameter().getCreator(), 
                topicView.getSearchParameter().getMake(), 
                topicView.getSearchParameter().getModel(), 
                topicView.getSearchParameter().getLens(), 
                topicView.getSearchParameter().getOrientation().getValue(), 
                topicView.getSearchParameter().getDate().getFrom().getUncompleteDateTime().toString(), 
                topicView.getSearchParameter().getDate().getTo().getUncompleteDateTime().toString(), 
                topicView.getSearchParameter().getRating().getFrom(), 
                topicView.getSearchParameter().getRating().getTo(), 
                topicView.getSearchParameter().getIso().getFrom(), 
                topicView.getSearchParameter().getIso().getTo(), 
                topicView.getSearchParameter().getExposureTime().getFrom(), 
                topicView.getSearchParameter().getExposureTime().getTo(), 
                topicView.getSearchParameter().getAperture().getFrom(), 
                topicView.getSearchParameter().getAperture().getTo(), 
                topicView.getSearchParameter().getFocalLength().getFrom(), 
                topicView.getSearchParameter().getFocalLength().getTo(),
                topicView.getSearchParameter().getDescTitle());
            if (!temp.isEmpty()) {
                temp.sort((o1, o2) -> o1.getCreationDate().compareTo(o2.getCreationDate()));
                view.setFrom(toDate(temp.get(0).getCreationDate()));
                view.setTo(toDate(temp.get(0).getCreationDate()));
                view.newEntry(view.getFrom());
                LocalDateTime current = temp.get(0).getCreationDate();
                int dayOfYear = current.getDayOfYear();
                for (Media m : temp) {
                    if (m.getCreationDate().getDayOfYear() > dayOfYear) {
                       current = m.getCreationDate();
                       dayOfYear = m.getCreationDate().getDayOfYear();
                       view.newEntry(toDate(m.getCreationDate()));
                    }
                    view.getLatest().getEntries().add(m);
                }
                view.setTo(toDate(current));
            }
            
        } catch (ConditionParseException e) {
                
        }
        
        return view;
    }
    
    private Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.of("Europe/Paris")).toInstant());
    }
    
    public boolean isUnique(TopicView in) {
        TypedQuery<Topic> query = getEntityManager()
                .createQuery("Select t from Topic t where t.year = :year and t.title = :title", Topic.class)
                .setParameter("year", in.getYear())
                .setParameter("title", in.getTitle());
        List<Topic> res = query.getResultList();
        if (res.isEmpty()) {
            return true;
        }
        return res.size() == 1 && Objects.equals(res.get(0).getId(), in.getId());
    }
    
    public void delete(TopicView v) {
        topicDao.remove(map(v));
    }
    
    public void update(TopicView in) {
        topicDao.merge(map(in));
    }
    
    public TopicView insert(TopicView in) {
        topicDao.create(map(in));
        TypedQuery<Topic> query = getEntityManager()
                .createQuery("Select t from Topic t where t.title = :title and t.year = :year", Topic.class)
                .setParameter("title", in.getTitle())
                .setParameter("year", in.getYear());
        List<Topic> t = query.getResultList();
        if (!t.isEmpty()) {
            in.setId(t.get(0).getId());
        }
        return in;
    }
    
    public List<TopicView> findAll() {
        TypedQuery<Topic> query = getEntityManager()
                .createQuery("Select t from Topic t order by t.year, t.tag.name, t.begin", Topic.class);
        List<Topic> res = query.getResultList();
        List<TopicView> result = new ArrayList<>(res.size());
        for (Topic in : res) {
            result.add(map(in, false));
        }
        return result;

    }
    
    public List<TopicView> findByAttributes(String title, Keyword keyword, Integer year) {
        try {
            Sql sql = new Sql("Select id, tagId, title, year, content, search, begin from Topic");
            if (keyword != null && keyword.getId() > 0) {
                sql.addCondition(new SimpleCondition("tagId", keyword.getId()));
            }
            if (year != null && year > 0) {
                sql.addCondition(new SimpleCondition("year", year));
            }
            if (StringUtils.isNotBlank(title)) {
                sql.addCondition(new TextCondition("title", title));
            }
            Query query = sql.buildQuery(getEntityManager(), Topic.class);
            List<Topic> res = query.getResultList();
            List<TopicView> result = new ArrayList<>(res.size());
            for (Topic in : res) {
                result.add(map(in, false));
            }
            return result;
        } catch (ConditionParseException e) {
            LOG.error(e.getMessage());
            return Collections.emptyList();
        }
        
    }
    private TopicView map(Topic in, boolean forDisplay) {
        TopicView out = new TopicView();
        out.setId(in.getId());
        if (forDisplay) {
            try {
                String text = "<root>\n" + in.getContent() + "\n</root>";
                XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                StringWriter writer = new StringWriter();
                HtmlDisplayFilter filter = new HtmlDisplayFilter(writer);
                filter.setParent(reader);
                filter.parse(new InputSource(new StringReader(text)));
                out.setContent(filter.writer.toString());
            } catch (IOException | SAXException | ParserConfigurationException e) {
                LOG.error(e.getMessage());
            }
        } else {
            out.setContent(in.getContent());
        }
        out.setBegin(in.getBegin());
        out.setKeyword(NodeFactory.getInstance().getKeywordById(in.getTag().getId()));
        out.setTitle(in.getTitle());
        out.setYear(in.getYear());
        if (StringUtils.isNotBlank(in.getSearch())) {
            try {
                JAXBContext context = JAXBContext.newInstance(SearchParameter.class);
                out.setSearchParameter((SearchParameter) context.createUnmarshaller().unmarshal(new StringReader(in.getSearch())));
            } catch (JAXBException e) {
                LOG.error("Error unmarshal " + in.getSearch(), e);
            }
        }
        return out;
    }
    
    private Topic map(TopicView in) {
        Topic out = new Topic();
        out.setId(in.getId());
        if (in.getBegin() == null && !in.getSearchParameter().getDate().getFrom().isEmpty()) {
            out.setBegin(in.getSearchParameter().getDate().getFrom().getLowerBound());
        } else {
            out.setBegin(in.getBegin());
        }
        if (StringUtils.isNotBlank(in.getContent())) {
            try {
                XMLReader reader = new Parser();
                StringWriter sw = new StringWriter();
                HtmlFilter filter = new HtmlFilter(sw);
                filter.setParent(reader);
                InputSource inputSource = new InputSource(new StringReader(in.getContent()));
                filter.parse(inputSource);
                out.setContent(sw.toString());
            } catch (IOException | SAXException e) {
                LOG.error("Can not transform description", e);
                out.setContent("");
            }
        } else {
            out.setContent("");
        }
        Tags tag = tagsFacade.find(in.getKeyword().getId());
        out.setTag(tag);
        out.setYear(in.getYear());
        out.setTitle(in.getTitle());
            try {
                JAXBContext context = JAXBContext.newInstance(SearchParameter.class);
                StringWriter writer = new StringWriter();
                context.createMarshaller().marshal(in.getSearchParameter(), writer);
                out.setSearch(writer.toString());
            } catch (JAXBException e) {
                LOG.error("Error unmarshal " + in.getSearchParameter(), e);
            }
        return out;
    }

    class HtmlFilter extends XMLFilterImpl {
        boolean ignore = true;

        private final Writer writer;
        
        public HtmlFilter(Writer writer) {
            this.writer = writer;
        }
        
        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            if (ignore) {
                if ("body".equals(qName)) {
                    ignore = false;
                }
            } else {
                write("<" + qName);
                for (int i = 0; i < atts.getLength(); i++) {
                    write(" " + atts.getQName(i));
                    write("=\"");
                    write(" " + atts.getValue(i));
                    write("\"");
                }
                write(">");
            }

        }
 
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (!ignore) {
                if ("body".equals(qName)) {
                    ignore = true;
                } else {
                    write("</" + qName + ">");
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (!ignore) {
                write(ch, start, length);
            }
        }
        
        private void write(String value) {
            try {
                writer.write(value);
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }

        private void write(char[] ch, int start, int length) {
            try {
                writer.write(ch, start, length);
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
    }
    
    class HtmlDisplayFilter extends XMLFilterImpl {

        private final Writer writer;

        public HtmlDisplayFilter(Writer writer) {
            this.writer = writer;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            if ("root".equals(qName)) {
                return;
            }
            if ("a".equals(qName)) {
                write("<a class=\"desc-link\"");
                String href = atts.getValue("href");
                if (href != null) {
                    write(" href=\"" + href.trim() + "\"");
                }
            } else {
                write("<" + qName);
                for (int i = 0; i < atts.getLength(); i++) {
                    write(" " + atts.getQName(i));
                    write("=\"");
                    write(" " + atts.getValue(i));
                    write("=\"");
                }
            }
            write(">");
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("root".equals(qName)) {
                return;
            }
            write("</" + qName + ">");
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            write(ch, start, length);
        }

        private void write(String value) {
            try {
                writer.write(value);
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }

        private void write(char[] ch, int start, int length) {
            try {
                writer.write(ch, start, length);
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setTopicDao(TopicDao topicDao) {
        this.topicDao = topicDao;
    }

    public void setTagsFacade(TagsDao tagsFacade) {
        this.tagsFacade = tagsFacade;
    }

    public void setImageFacade(ImageFacade imageFacade) {
        this.imageFacade = imageFacade;
    }
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
