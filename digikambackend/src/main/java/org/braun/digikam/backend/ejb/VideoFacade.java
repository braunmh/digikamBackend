package org.braun.digikam.backend.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.api.NotFoundException;
import org.braun.digikam.backend.api.Thumbnail;
import org.braun.digikam.backend.graphics.ExifUtil;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.backend.model.Media;
import org.braun.digikam.backend.model.VideoInternal;
import org.braun.digikam.backend.search.ConditionParseException;
import org.braun.digikam.backend.search.ExistsCondition;
import org.braun.digikam.backend.search.InCondition;
import org.braun.digikam.backend.search.JoinCondition;
import org.braun.digikam.backend.search.Operator;
import org.braun.digikam.backend.search.RangeCondition;
import org.braun.digikam.backend.search.SimpleCondition;
import org.braun.digikam.backend.search.Sql;
import org.braun.digikam.backend.util.Util;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

/**
 *
 * @author mbraun
 */
@Stateless
public class VideoFacade {

    private static final Logger LOG = LogManager.getLogger();
    private static final String FIND_BY_ATTRIBUTES
        = "SELECT i.id, i.name, i.root, i.relativePath, i.fileSize, i.rating, i.creationDate, i.orientation, i.width, i.height, "
        + "i.creator, i.latitudeNumber, i.longitudeNumber, i.duration "
        + "FROM VideoFull i";

    @PersistenceContext(unitName = "digikam")
    private EntityManager em;

    public InputStream getVideo(int id) throws NotFoundException {
        VideoFull image = getVideoFull(id);
        FileInputStream imageStream = getImageFile(image.getRoot(), image.getRelativePath(), image.getName());
        return imageStream;
    }

    public byte[] getThumbnail(int id) throws NotFoundException {
        Thumbnail thumbnail = getEntityManager().find(Thumbnail.class, id);
        if (thumbnail == null) {
            VideoFull videoFull = getVideoFull(id);
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(getImageFile(videoFull.getRoot(), videoFull.getRelativePath(), videoFull.getName()));
            try {
                grabber.start();
                Java2DFrameConverter java2DConverter = new Java2DFrameConverter();
                BufferedImage image = null;
                while (image == null) {
                    image = java2DConverter.getBufferedImage(grabber.grabImage());
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", baos);
                grabber.stop();
                VideoInternal videoInternal = getMetadata(videoFull);
                baos = ExifUtil.writeExifData(videoInternal, baos.toByteArray());
                thumbnail = new Thumbnail(id);
                thumbnail.setModificationDate(videoFull.getModificationDate());
                thumbnail.setOrientation(videoFull.getOrientation());
                thumbnail.setData(baos.toByteArray());
                getEntityManager().persist(thumbnail);
                return baos.toByteArray();
            } catch(IOException e) {
                throw new NotFoundException(404, "No Thumbnail for Video with id=" + id);
            }
        } else {
            return thumbnail.getData();
        }
    }
    
    public List<Media> findVideosByAttributes(
        List<Integer> keywords, String creator, String orientation,
        String dateFrom, String dateTo, Integer ratingFrom, Integer ratingTo) throws ConditionParseException {
        Sql sql = new Sql(FIND_BY_ATTRIBUTES);
        addCondition(sql, "i.creator", creator);
        addRangeCondition(sql, "i.rating", ratingFrom, ratingTo);
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

        sql.addOrderClause("i.creationDate");
        Query query = sql.buildQuery(getEntityManager(), VideoFull.class);
        LOG.debug(sql.toString());

        List<VideoFull> res = query.getResultList();
        List<Media> result = new ArrayList<>();
        for (VideoFull i : res) {
            result.add(new Media().id(i.getId())
                .creationDate(Util.convert(i.getCreationDate()))
                .image(false)
                .name(i.getName()));
        }
        return result;
    }

    public VideoInternal getMetadata(int id) throws NotFoundException {
        VideoFull videoFull = getVideoFull(id);
        return getMetadata(videoFull);
    }

    private VideoInternal getMetadata(VideoFull videoFull) {
        VideoInternal video = new VideoInternal()
            .id(videoFull.getId())
            .name(videoFull.getName())
            .root(normAlbumPath(videoFull.getRoot()))
            .relativePath(videoFull.getRelativePath())
            //            .duration(res.getDuration()) String to Integer number of seconds
            .creationDate(Util.convert(videoFull.getCreationDate()))
            .creator(videoFull.getCreator())
            .height(videoFull.getHeight())
            .width(videoFull.getWidth())
            .latitude(videoFull.getLatitudeNumber())
            .longitude(videoFull.getLongitudeNumber())
            .rating(videoFull.getRating())
            .keywords(getKeywords(videoFull.getId()))
            .orientationExif(videoFull.getOrientation())
            .orientationTechnical(videoFull.getOrientation());

        TypedQuery<ImageComments> qi = getEntityManager().createNamedQuery("ImageComments.findByImageId", ImageComments.class);
        qi.setParameter("imageId", video.getId());
        for (ImageComments ic : qi.getResultList()) {
            if (null == ic.getType()) {
                continue;
            }
            switch (ic.getType()) {
                case 1:
                    video.description(ic.getComment());
                    break;
                case 3:
                    video.title(ic.getComment());
                    break;
            }
        }
        return video;
    }
    
    private VideoFull getVideoFull(int id) throws NotFoundException {
        TypedQuery<VideoFull> query = getEntityManager().createNamedQuery("VideoFull.findById", VideoFull.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            String msg = String.format("Image with id %s not found or exists anymore", id);
            LOG.info("msg");
            throw new NotFoundException(404, msg);
        }
    }

    private FileInputStream getImageFile(String root, String path, String name) throws NotFoundException {
        File file = new File(normAlbumPath(root) + path + "/" + name);
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
