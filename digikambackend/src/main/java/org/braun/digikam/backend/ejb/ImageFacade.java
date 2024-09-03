package org.braun.digikam.backend.ejb;

import org.braun.digikam.backend.entity.ImageFull;
import org.braun.digikam.backend.entity.ImageComments;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.api.NotFoundException;
import org.braun.digikam.backend.graphics.ExifUtil;
import org.braun.digikam.backend.graphics.ImageUtil;
import org.braun.digikam.backend.model.ImageInternal;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.backend.model.Media;
import org.braun.digikam.backend.model.MediaSolr;
import org.braun.digikam.backend.model.StatisticKeyword;
import org.braun.digikam.backend.model.StatisticMonth;
import org.braun.digikam.backend.search.ConditionParseException;
import org.braun.digikam.backend.search.solr.SolrQueryBuilder;
import org.braun.digikam.backend.search.sql.ExistsCondition;
import org.braun.digikam.backend.search.sql.InCondition;
import org.braun.digikam.backend.search.sql.JoinCondition;
import org.braun.digikam.backend.search.sql.Operator;
import org.braun.digikam.backend.search.sql.RangeCondition;
import org.braun.digikam.backend.search.sql.SimpleCondition;
import org.braun.digikam.backend.search.sql.SortOrder;
import org.braun.digikam.backend.search.sql.Sql;
import org.braun.digikam.backend.util.Configuration;
import org.braun.digikam.backend.util.Util;

/**
 *
 * @author mbraun
 */
@Stateless
public class ImageFacade {

    private static final Logger LOG = LogManager.getLogger();
    private static final String FIND_BY_ATTRIBUTES
        = "SELECT i.id, i.name, i.root, i.relativePath, i.fileSize, i.rating, i.creationDate, i.orientation, i.width, i.height, i.make, i.model, "
        + "i.lens, i.aperture, i.focalLength, i.focalLength35, i.exposureTime, i.sensitivity, i.creator, i.latitudeNumber, i.longitudeNumber "
        + "FROM ImageFull i";

    @PersistenceContext(unitName = "digikam")
    private EntityManager em;

    public InputStream getImage(long id) throws NotFoundException {
        ImageFull image = getImageFull(id);
        FileInputStream imageStream = getImageFile(image.getRoot(), image.getRelativePath(), image.getName());
        return imageStream;
    }

    public byte[] getScaledImage(long id, int width, int height) throws NotFoundException {
        ImageInternal image = ImageFacade.this.getMetadata(id);
        FileInputStream fis = getImageFile(image.getRoot(), image.getRelativePath(), image.getName());
        ByteArrayOutputStream scaledImage = new ByteArrayOutputStream();
        try {
            ImageUtil.scaleImage(fis, scaledImage, width, height, image.getOrientationTechnical());
            ByteArrayOutputStream taggedImage = ExifUtil.writeExifData(image, scaledImage.toByteArray());
            return taggedImage.toByteArray();
        } catch (IOException e) {
            LOG.error("Error scaling image with id = " + id, e);
            throw new NotFoundException(404, e.getMessage());
        }
    }

    public ImageInternal getMetadata(long id) throws NotFoundException {
        ImageFull res = getImageFull(id);
        return getMetadata(res);
    }

    public ImageInternal getMetadata(ImageFull res) {
        ImageInternal image = new ImageInternal()
            .id(res.getId())
            .name(res.getName())
            .root(normAlbumPath(res.getRoot()))
            .relativePath(res.getRelativePath())
            .aperture(res.getAperture())
            .creationDate(Util.convert(res.getCreationDate()))
            .creator(res.getCreator())
            .exposureTime(res.getExposureTime())
            .focalLength35(res.getFocalLength35())
            .focalLength(res.getFocalLength())
            .height(res.getHeight())
            .width(res.getWidth())
            .iso(res.getSensitivity())
            .make(res.getMake())
            .model(res.getModel())
            .lens(res.getLens())
            .latitude(res.getLatitudeNumber())
            .longitude(res.getLongitudeNumber())
            .exposureTime(res.getExposureTime())
            .rating(res.getRating())
            .keywords(getKeywords(res.getId()))
            .orientationTechnical(res.getOrientation());

        TypedQuery<ImageComments> qi = getEntityManager().createNamedQuery("ImageComments.findByImageId", ImageComments.class);
        qi.setParameter("imageId", res.getId());
        for (ImageComments ic : qi.getResultList()) {
            if (null == ic.getType()) {
                continue;
            }
            switch (ic.getType()) {
                case 1:
                    image.description(ic.getComment());
                    break;
                case 3:
                    image.title(ic.getComment());
                    break;
            }
        }
        return image;
    }
    
    public List<Media> findImagesByImageAttributesSolr(
        List<Long> keywords, Boolean keywordsOr, String creator, String make, String model, String lens, String orientation,
        String dateFrom, String dateTo, Integer ratingFrom, Integer ratingTo, Integer isoFrom, Integer isoTo,
        Double exposureTimeFrom, Double exposureTimeTo, Double apertureFrom, Double apertureTo,
        Integer focalLengthFrom, Integer focalLengthTo) throws ConditionParseException {
        try (SolrClient client = getSolrClient()) {
           final String solrCollection = Configuration.getInstance().getSolrCollection(); 
            SolrQuery query = new SolrQueryBuilder()
                .addField("id")
                .addField("name")
                .addField("creationDate")
                .addField("type")
                .addField("score")
                .addQuery("creationDate", new DateWrapper("202407--"))
//                .addQuery("creator", "Michael Braun")
                .addQuery("keywordIds", keywords, keywordsOr)
                .addQuery("creator", creator)
                .addQuery("make", make)
                .addQuery("model", model)
                .addQuery("lens", lens)
                .addQuery("creator", creator)
                .addQuery("rating", ratingFrom, ratingTo)
                .addQuery("iso", isoFrom, isoTo)
                .addQuery("exposureTime", exposureTimeFrom, exposureTimeTo)
                .addQuery("focalLength", focalLengthFrom, focalLengthTo)
                .addQuery("aperture", apertureFrom, apertureTo)
                .addQuery("creationDate", new DateWrapper(dateFrom), new DateWrapper(dateTo))
                .build();
            QueryResponse response = client.query(solrCollection, query);
            LOG.info("Number of Documents found: " + response.getResults().getNumFound());
            List<MediaSolr> result = response.getBeans(MediaSolr.class);
            List<Media> res = new ArrayList<>(result.size());
            for (MediaSolr is : result) {
                Media media = is.toMedia();
                res.add(media);
            }
            return res;
        } catch (IOException | SolrServerException e) {
            LOG.debug(e.getMessage(), e);
            throw new ConditionParseException(e.getMessage());
        }
    }
    
    private SolrClient getSolrClient() {
        final String solrUrl = Configuration.getInstance().getSolrClientUrl();
        return new Http2SolrClient.Builder(solrUrl).build();
    }
    
    public List<Media> findImagesByImageAttributes(
        List<Long> keywords, Boolean keyordsOr, String creator, String make, String model, String lens, String orientation,
        String dateFrom, String dateTo, Integer ratingFrom, Integer ratingTo, Integer isoFrom, Integer isoTo,
        Double exposureTimeFrom, Double exposureTimeTo, Double apertureFrom, Double apertureTo,
        Integer focalLengthFrom, Integer focalLengthTo) throws ConditionParseException {
        Sql sql = new Sql(FIND_BY_ATTRIBUTES);
        addCondition(sql, "i.creator", creator);
        addCondition(sql, "i.make", make);
        addCondition(sql, "i.model", model);
        addCondition(sql, "i.lens", lens);
        addRangeCondition(sql, "i.rating", ratingFrom, ratingTo);
        addRangeCondition(sql, "i.sensitivity", isoFrom, isoTo);
        addRangeCondition(sql, "i.exposureTime", exposureTimeFrom, exposureTimeTo);
        addRangeCondition(sql, "i.aperture", apertureFrom, apertureTo);
        addRangeCondition(sql, "i.focalLength", focalLengthFrom, focalLengthTo);
        addRangeCondition(sql, "i.sensitivity", isoFrom, isoTo);
        if (StringUtils.isNotBlank(orientation)) {
            if ("portrait".equals(orientation)) {
                sql.addCondition(new JoinCondition(Operator.Less, "i.width", "i.height"));
            } else {
                sql.addCondition(new JoinCondition(Operator.GreaterEquals, "i.width", "i.height"));
            }
        }
        DateWrapper wFrom = new DateWrapper(dateFrom);
        DateWrapper wTo = new DateWrapper(dateTo);

        if (!wFrom.isEmpty() || !wFrom.isEmpty()) {
            Date from = null;
            Date to;
            if (wTo.isEmpty()) {
                from = wFrom.getLowerBound();
                to = wFrom.getUpperBound();
            } else if (wFrom.isEmpty()) {
                to = wTo.getUpperBound();
            } else {
                from = wFrom.getLowerBound();
                to = wTo.getUpperBound();
            }
            sql.addCondition(new RangeCondition("i.creationDate", from, to));
        }

        if (keywords != null && !keywords.isEmpty()) {
            for (Long k : keywords) {
                List<Long> ks = NodeFactory.getInstance().getChildrensRec(k);
                if (ks.isEmpty()) {
                    continue;
                }
                ExistsCondition kc = new ExistsCondition(false, "Select t.id from Tags t inner join ImageTags it on t.id = it.tagid");
                kc.addCondition(new JoinCondition(Operator.Equals, "i.id", "it.imageid"));
                kc.addCondition(new InCondition("t.id", ks));
                sql.addCondition(kc);
            }
        }

        Query query = sql.buildQuery(getEntityManager(), ImageFull.class);
        sql.addOrderClause("i.creationDate");
        LOG.debug(sql.toString());

        List<ImageFull> res = query.getResultList();
        List<Media> result = new ArrayList<>();
        for (ImageFull i : res) {
            result.add(new Media().id(i.getId())
                .creationDate(Util.convert(i.getCreationDate()))
                .image(true)
                .name(i.getName())
            );
        }
        return result;
    }

    public List<StatisticKeyword> statKeyword(Long keywordId, int year) throws ConditionParseException {
        Sql sql = new Sql("select count(*) cnt, t.id, t.name from Tags t inner join ImageTags it on t.id = it.tagid inner join ImageInformation ii on it.imageid = ii.imageid");
        sql.addGroupClause("t.name");
        sql.addGroupClause("t.id");
        sql.addOrderClause("t.name");
        List<Long> keywords = NodeFactory.getInstance().getChildrensRec(keywordId);
        sql.addCondition(new InCondition("t.id", keywords));
        Calendar now = Calendar.getInstance();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, 0);
        now.set(Calendar.DATE, 1);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        Date from = now.getTime();
        now.set(Calendar.MONTH, 11);
        now.set(Calendar.DATE, 31);
        Date to = now.getTime();
        sql.addCondition(new RangeCondition("ii.creationDate", from, to));
        Query q = sql.buildQuery(getEntityManager(), StatisticKeywordEntity.class);
        List<StatisticKeywordEntity> res = q.getResultList();
        List<StatisticKeyword> result = new ArrayList<>();
        for (StatisticKeywordEntity e : res) {
            result.add(new StatisticKeyword().count(e.getCount()).name(NodeFactory.getInstance().getKeywordQualById(e.getId())));
        }
        return result;
    }

    private ImageFull getImageFull(long id) throws NotFoundException {
        TypedQuery<ImageFull> query = getEntityManager().createNamedQuery("ImageFull.findById", ImageFull.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            String msg = String.format("Image with id %s not found or exists anymore", id);
            LOG.info("msg");
            throw new NotFoundException(404, msg);
        }
    }

    public List<StatisticMonth> statMonth(Integer year) {
        String select = "select year(creationDate) year, month(creationDate) month, count(*) 'count' from ImageInformation";
        Sql sql = new Sql(select);
        if (year != null && year > 0) {
            sql.addCondition(new SimpleCondition("year(creationDate)", year));
        }
        sql.addGroupClause("year(creationDate)");
        sql.addGroupClause("month(creationDate)");
        sql.addOrderClause("year(creationDate)", SortOrder.Descending);
        sql.addOrderClause("month(creationDate)", SortOrder.Descending);
        Query query = sql.buildQuery(getEntityManager(), StatistikMonthEntity.class);
        List<StatistikMonthEntity> temp = query.getResultList();
        List<StatisticMonth> result = new ArrayList<>();
        for (StatistikMonthEntity e : temp) {
            result.add(new StatisticMonth()
                .month(e.getKey().getMonth())
                .year(e.getKey().getYear())
                .cnt(e.getCount())
            );
        }
        return result;
    }

    private FileInputStream getImageFile(String root, String path, String name) throws NotFoundException {
        File file = new File(root + path + "/" + name);
        if (!file.exists() || !file.canRead()) {
            LOG.error("File {} does not exists or can not be read.", file.getPath());
            throw new NotFoundException(404, "File +" + path + "/" + name + " existiert nicht");
        }
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            LOG.error("Reading File {} failed. Msg: {}", file.getPath(), e.getMessage());
            throw new NotFoundException(404, "File +" + path + "/" + name + " existiert nicht");
        }
    }
    
    private void addCondition(Sql sql, String columnName, String value) {
        if (StringUtils.isNotEmpty(value)) {
            sql.addCondition(new SimpleCondition(columnName, value));
        }
    }

    private void addRangeCondition(Sql sql, String columnName, Integer from, Integer to) throws ConditionParseException {
        sql.addCondition(new RangeCondition(columnName, from, to));
    }

    private void addRangeCondition(Sql sql, String columnName, Double from, Double to) throws ConditionParseException {
        sql.addCondition(new RangeCondition(columnName, from, to));
    }

    private List<Keyword> getKeywords(long imageId) {
        List<Keyword> result = new ArrayList<>();
        for (Integer id : getKeyordIds(imageId)) {
            Keyword k = NodeFactory.getInstance().getKeywordById(id);
            if (k != null) {
                result.add(k);
            }
        }
        return result;
    }

    private List<Integer> getKeyordIds(long imageId) {
        Query query = getEntityManager().createNativeQuery("select tagId from ImageTags where imageid = ?");
        query.setParameter(1, imageId);
        return query.getResultList();
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    private String normAlbumPath(String value) {
        int amp = value.indexOf('&');
        return (amp > 0) ? value.substring(0, amp) : value;
    }
}
