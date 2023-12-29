package org.braun.digikam.backend.ejb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.BadRequestException;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.api.NotFoundException;
import org.braun.digikam.backend.graphics.ExifUtil;
import org.braun.digikam.backend.graphics.ImageUtil;
import org.braun.digikam.backend.model.Image;
import org.braun.digikam.backend.model.ImageInternal;
import org.braun.digikam.backend.model.ImagesInner;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.backend.model.StatisticKeyword;
import org.braun.digikam.backend.model.StatisticMonth;
import org.braun.digikam.backend.search.ConditionParseException;
import org.braun.digikam.backend.search.ExistsCondition;
import org.braun.digikam.backend.search.InCondition;
import org.braun.digikam.backend.search.JoinCondition;
import org.braun.digikam.backend.search.Operator;
import org.braun.digikam.backend.search.RangeCondition;
import org.braun.digikam.backend.search.SimpleCondition;
import org.braun.digikam.backend.search.SortOrder;
import org.braun.digikam.backend.search.Sql;
import org.braun.digikam.backend.util.UncompleteDateTime;
import org.braun.digikam.backend.util.Util;

/**
 *
 * @author mbraun
 */
@Stateless
public class ImageFacade {

    private static final Logger LOG = LogManager.getLogger();
    private static final String FIND_BY_ATTRIBUTES
        = "SELECT i.id, i.name, i.relativePath, i.fileSize, i.rating, i.creationDate, i.orientation, i.width, i.height, i.make, i.model, "
        + "i.lens, i.aperture, i.focalLength, i.focalLength35, i.exposureTime, i.sensitivity, i.creator, i.latitudeNumber, i.longitudeNumber "
        + "FROM ImageFull i";

    @PersistenceContext(unitName = "digikam")
    private EntityManager em;

    @Inject
    private ImagesFacade imagesFacade;
    
    @Inject
    private ImageInformationFacade imageInformationFacade;
    
    public InputStream getImage(int id) throws NotFoundException {
        ImageFull image = getImageFull(id);
        FileInputStream imageStream = getImageFile(image.getRelativePath(), image.getName());
        return imageStream;
    }
    
    public byte[] getScaledImage(int id, int width, int height) throws NotFoundException {
        ImageInternal image = getMetadata(id);
        FileInputStream fis = getImageFile(image.getRelativePath(), image.getName());
        ByteArrayOutputStream scaledImage = new ByteArrayOutputStream();
        try {
            ImageUtil.scaleImage(fis, scaledImage, width, height, image.getOrientationTechnical());
            ByteArrayOutputStream taggedImage = ExifUtil.writeExifData(image, scaledImage.toByteArray());
            return taggedImage.toByteArray();
        } catch (IOException e) {
            LOG.error("Error scaling image with id = " + id,  e);
            throw new NotFoundException(404, e.getMessage());
        }
    }
    
    private FileInputStream getImageFile(String path, String name) throws NotFoundException {
        File file = new File("/data/pictures" + path + "/" + name);
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
    
    public ImageInternal getMetadata(int id) throws NotFoundException {
        ImageFull res = getImageFull(id);
        ImageInternal image = new ImageInternal()
            .id(res.getId())
            .name(res.getName())
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
            .keywords(getKeywords(id))
            .orientationTechnical(res.getOrientation());

        TypedQuery<ImageComments> qi = getEntityManager().createNamedQuery("ImageComments.findByImageId", ImageComments.class);
        qi.setParameter("imageId", id);
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

    public List<ImagesInner> findImagesByImageAttributes(
        List<Integer> keywords, String creator, String make, String model, String lens, String orientation,
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
            for (Integer k : keywords) {
                List<Integer> ks = NodeFactory.getInstance().getChildrensRec(k);
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
        List<ImagesInner> result = new ArrayList<>();
        for (ImageFull i : res) {
            result.add(new ImagesInner().imageId(i.getId()).creationDate(Util.convert(i.getCreationDate())));
        }
        return result;
    }

    /**
     * 
     * @param id of Image to rate
     * @param rating new rating 
     * @throws NotFoundException 
     */
    public void updateRating(int id, int rating) throws NotFoundException {
        TypedQuery<ImageInformation> query = getEntityManager().createNamedQuery("ImageInformation.findByImageId", ImageInformation.class);
        query.setParameter("imageId", id);
        try {
            ImageInformation imageInformation = query.getSingleResult();
            imageInformation.setRating(rating);
            getEntityManager().merge(imageInformation);
        } catch (NoResultException e) {
            String msg = String.format("Image with id %s not found or exists anymore", id);
            LOG.info("msg");
            throw new NotFoundException(404, msg);
        }
    }
    
    public List<StatisticKeyword> statKeyword(int keywordId, int year) throws ConditionParseException {
        Sql sql = new Sql("select count(*) cnt, t.id, t.name from Tags t inner join ImageTags it on t.id = it.tagid inner join ImageInformation ii on it.imageid = ii.imageid");
        sql.addGroupClause("t.name");
        sql.addGroupClause("t.id");
        sql.addOrderClause("t.name");
        List<Integer> keywords = NodeFactory.getInstance().getChildrensRec(keywordId);
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
        sql.addCondition(new RangeCondition("ii.creationDate",from, to));
        Query q = sql.buildQuery(getEntityManager(), StatisticKeywordEntity.class);
        List<StatisticKeywordEntity> res = q.getResultList();
        List<StatisticKeyword> result = new ArrayList<>();
        for (StatisticKeywordEntity e : res) {
            result.add(new StatisticKeyword().count(e.getCount()).name(NodeFactory.getInstance().getKeywordQualById(e.getId())));
        }
        return result;
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

    private List<Keyword> getKeywords(int imageId) {
        List<Keyword> result = new ArrayList<>();
        for (Integer id : getKeyordIds(imageId)) {
            Keyword k = NodeFactory.getInstance().getKeywordById(id);
            if (k != null) {
                result.add(k);
            }
        }
        return result;
    }

    private List<Integer> getKeyordIds(int imageId) {
        Query query = getEntityManager().createNativeQuery("select tagId from ImageTags where imageid = ?");
        query.setParameter(1, imageId);
        return query.getResultList();
    }

    private ImageFull getImageFull(int id) throws NotFoundException {
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
    
    protected EntityManager getEntityManager() {
        return em;
    }

    void setEntityManager(EntityManager em) {
        this.em = em;
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

    public void update(int id, String title, String description, int rating, List<Tags> tags, String creator) throws NotFoundException {
        Images images = imagesFacade.find(id);
        if (images == null) {
            String msg = String.format("Image with id %s not found or exists anymore", id);
            throw new NotFoundException(404, msg);
        }

        ImageInformation information = imageInformationFacade.findByImageId(id);
        if (null != information) {
            information.setRating(rating);
            getEntityManager().merge(information);
        }

        updateComment(3, title, images);
        updateComment(1, description, images);

        updateCreator(creator, images);
        
        images.getTags().clear();
        images.getTags().addAll(tags);
        getEntityManager().merge(images);
        getEntityManager().flush();
    }

    private void updateComment(int type, String value, Images images) {
        for (ImageComments c : images.getComments()) {
            if (null != c.getType() && type == c.getType()) {
                c.setComment(value);
                return;
            }
        }

        ImageComments imageComments = new ImageComments();
        imageComments.setType(type);
        imageComments.setImage(images);
        imageComments.setLanguage("x-default");
        imageComments.setComment(value);
        imageComments.setType(type);
        images.getComments().add(imageComments);
    }

    private void updateCreator(String value, Images image) {
        for (ImageCopyright c : image.getCopyrights()) {
            if (null != c.getProperty() && "creator".equals(c.getProperty())) {
                c.setValue(value);
                return;
            }
        }
        ImageCopyright ic = new ImageCopyright();
        ic.setImage(image);
        ic.setProperty("creator");
        ic.setValue(value);
        image.getCopyrights().add(ic);
    }
    
    class DateWrapper {

        private UncompleteDateTime udt;

        public DateWrapper(String udt) {
            try {
                this.udt = new UncompleteDateTime( udt);
            } catch (BadRequestException e) {
                this.udt = null;
            }
        }

        public boolean isEmpty() {
            return udt == null || isEmpty(udt.getYear());
        }

        public Date getLowerBound() {
            Calendar temp = Calendar.getInstance();
            temp.set(Calendar.YEAR, udt.getYear());
            if (isEmpty(udt.getMonth())) {
                temp.set(Calendar.MONTH, 0);
            } else {
                temp.set(Calendar.MONTH, udt.getMonth() - 1);
            }

            if (isEmpty(udt.getDay())) {
                temp.set(Calendar.DATE, 1);
            } else {
                temp.set(Calendar.DATE, udt.getDay());
            }

            if (isEmpty(udt.getHour())) {
                temp.set(Calendar.HOUR_OF_DAY, 0);
            } else {
                temp.set(Calendar.HOUR_OF_DAY, udt.getHour());
            }

            if (isEmpty(udt.getMinute())) {
                temp.set(Calendar.MINUTE, 0);
            } else {
                temp.set(Calendar.MINUTE, udt.getMinute());
            }

            if (isEmpty(udt.getSecond())) {
                temp.set(Calendar.SECOND, 0);
            } else {
                temp.set(Calendar.SECOND, udt.getSecond());
            }

            return temp.getTime();
        }

        public Date getUpperBound() {
            Calendar temp = Calendar.getInstance();
            temp.set(Calendar.YEAR, udt.getYear());
            if (isEmpty(udt.getMonth())) {
                temp.set(Calendar.MONTH, 11);
            } else {
                temp.set(Calendar.MONTH, udt.getMonth() - 1);
            }

            if (isEmpty(udt.getDay())) {
                temp.add(Calendar.MONTH, 1);
                temp.add(Calendar.DATE, -1);
            } else {
                temp.set(Calendar.DATE, udt.getDay());
            }

            if (isEmpty(udt.getHour())) {
                temp.set(Calendar.HOUR_OF_DAY, 23);
            } else {
                temp.set(Calendar.HOUR_OF_DAY, udt.getHour());
            }

            if (isEmpty(udt.getMinute())) {
                temp.set(Calendar.MINUTE, 59);
            } else {
                temp.set(Calendar.MINUTE, udt.getMinute());
            }

            if (isEmpty(udt.getSecond())) {
                temp.set(Calendar.SECOND, 59);
            } else {
                temp.set(Calendar.SECOND, udt.getSecond());
            }

            return temp.getTime();
        }

        private boolean isEmpty(Integer i) {
            return i == null || i == 0;
        }
    }
}
