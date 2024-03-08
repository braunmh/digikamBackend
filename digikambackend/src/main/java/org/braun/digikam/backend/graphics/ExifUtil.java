package org.braun.digikam.backend.graphics;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.MicrosoftTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoRationals;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShorts;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoXpString;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.model.Image;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.backend.model.Video;
import org.braun.digikam.backend.model.VideoInternal;

/**
 *
 * @author mbraun
 */
public class ExifUtil {
    private static final Logger LOG = LogManager.getLogger();
    
    public static ByteArrayOutputStream writeExifData(Image image, byte[] imageByte) throws IOException {
        try {
            TiffOutputSet outputSet = null;
            final ImageMetadata metadata = Imaging.getMetadata(imageByte);
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            if (null != jpegMetadata) {
                final TiffImageMetadata exif = jpegMetadata.getExif();
                if (null != exif) {
                    outputSet = exif.getOutputSet();
                }
            }
            if (null == outputSet) {
                outputSet = new TiffOutputSet();
            }
            boolean execute = true;
            {
                final TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
                if (execute) {
                    addTag(exifDirectory, ExifTagConstants.EXIF_TAG_APERTURE_VALUE, image.getAperture());
                    addTag(exifDirectory, ExifTagConstants.EXIF_TAG_EXPOSURE_TIME, image.getExposureTime());
                    addTag(exifDirectory, ExifTagConstants.EXIF_TAG_FOCAL_LENGTH, image.getFocalLength());
                    addTag(exifDirectory, ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, convert(image.getCreationDate()));
                    addTag(exifDirectory, ExifTagConstants.EXIF_TAG_LENS_MODEL, image.getLens());
                    addTag(exifDirectory, ExifTagConstants.EXIF_TAG_ISO, image.getIso());
                    addTag(exifDirectory, ExifTagConstants.EXIF_TAG_FOCAL_LENGTH_IN_35MM_FORMAT, image.getFocalLength35());
                    addTag(exifDirectory, TiffTagConstants.TIFF_TAG_MAKE, image.getMake());
                    addTag(exifDirectory, TiffTagConstants.TIFF_TAG_MODEL, image.getModel());
                    addTag(exifDirectory, TiffTagConstants.TIFF_TAG_ARTIST, image.getCreator());
                    addTag(exifDirectory, TiffTagConstants.TIFF_TAG_COPYRIGHT, "Alle Rechte vorbehalten");
                    addTag(exifDirectory, MicrosoftTagConstants.EXIF_TAG_RATING, image.getRating());
                    addTag(exifDirectory, MicrosoftTagConstants.EXIF_TAG_XPTITLE, image.getTitle());
                    addTag(exifDirectory, MicrosoftTagConstants.EXIF_TAG_XPCOMMENT, image.getDescription());
                    addTag(exifDirectory, MicrosoftTagConstants.EXIF_TAG_XPKEYWORDS, convertTags(image.getKeywords()));
                    addTag(exifDirectory, MicrosoftTagConstants.EXIF_TAG_XPSUBJECT, convertTags(image.getKeywords()));

                    if (null != image.getLongitude()&& null != image.getLatitude()) {
                        outputSet.setGPSInDegrees(image.getLongitude(), image.getLatitude());
                    }
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new ExifRewriter().updateExifMetadataLossless(imageByte, baos, outputSet);
            return baos;
        } catch (ImageReadException | ImageWriteException e) {
            LOG.error(e);
            throw new IOException(e.getMessage());
        }
    }

    public static ByteArrayOutputStream writeExifData(VideoInternal video, byte[] imageByte) throws IOException {
        try {
            TiffOutputSet outputSet = null;
            final ImageMetadata metadata = Imaging.getMetadata(imageByte);
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            if (null != jpegMetadata) {
                final TiffImageMetadata exif = jpegMetadata.getExif();
                if (null != exif) {
                    outputSet = exif.getOutputSet();
                }
            }
            if (null == outputSet) {
                outputSet = new TiffOutputSet();
            }
            boolean execute = true;
            {
                final TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
                if (execute) {
                    addTag(exifDirectory, ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, convert(video.getCreationDate()));
                    addTag(exifDirectory, TiffTagConstants.TIFF_TAG_ARTIST, video.getCreator());
                    addTag(exifDirectory, TiffTagConstants.TIFF_TAG_COPYRIGHT, "Alle Rechte vorbehalten");
                    addTag(exifDirectory, TiffTagConstants.TIFF_TAG_ORIENTATION, video.getOrientationExif());
                    addTag(exifDirectory, MicrosoftTagConstants.EXIF_TAG_RATING, video.getRating());
                    addTag(exifDirectory, MicrosoftTagConstants.EXIF_TAG_XPTITLE, video.getTitle());
                    addTag(exifDirectory, MicrosoftTagConstants.EXIF_TAG_XPCOMMENT, video.getDescription());
                    addTag(exifDirectory, MicrosoftTagConstants.EXIF_TAG_XPKEYWORDS, convertTags(video.getKeywords()));
                    addTag(exifDirectory, MicrosoftTagConstants.EXIF_TAG_XPSUBJECT, convertTags(video.getKeywords()));

                    if (null != video.getLongitude()&& null != video.getLatitude()) {
                        outputSet.setGPSInDegrees(video.getLongitude(), video.getLatitude());
                    }
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new ExifRewriter().updateExifMetadataLossless(imageByte, baos, outputSet);
            return baos;
        } catch (ImageReadException | ImageWriteException e) {
            LOG.error(e);
            throw new IOException(e.getMessage());
        }
    }

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

    private static String convert(LocalDateTime value) {
        if (null == value) {
            return null;
        }
        return fmt.format(value);
    }
    private static String convert(Collection<String> values) {
        if (null == values || values.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (String v : values) {
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append(", ");
            }
            builder.append(v);
        }
        return builder.toString();
    }

    private static String convertTags(List<Keyword> tags) {
        if (null == tags || tags.isEmpty()) {
            return null;
        }
        Set<String> values = new HashSet<>();
        for (Keyword t : tags) {
            for (String v : t.getFullName().split("/")) {
                if (v.trim().length() == 0) {
                    continue;
                }
                values.add(v.trim());
            }
        }
        return convert(values);
    }

    private static void addTag(TiffOutputDirectory exifDirectory, TagInfoRational tagInfo, Double value) throws ImageWriteException {
        if (null == value) {
            return;
        }
        exifDirectory.removeField(tagInfo);
        exifDirectory.add(tagInfo, new RationalNumber((int) (value * 100), 100));
    }

    private static void addTag(TiffOutputDirectory exifDirectory, TagInfoRationals tagInfo, Double value) throws ImageWriteException {
        if (null == value) {
            return;
        }
        exifDirectory.removeField(tagInfo);
        exifDirectory.add(tagInfo, new RationalNumber((int) (value * 1000), 1000));
    }

    private static void addTag(TiffOutputDirectory exifDirectory, TagInfoShorts tagInfo, Integer... value) throws ImageWriteException {
        if (null == value || value.length == 0) {
            return;
        }
        short[] shorts = new short[value.length];
        int i = 0;
        for (Integer v : value) {
            if (null == v) continue;
            shorts[i++] = v.shortValue();
        }
        exifDirectory.removeField(tagInfo);
        exifDirectory.add(tagInfo, shorts);
    }

    private static void addTag(TiffOutputDirectory exifDirectory, TagInfoShort tagInfo, Double value) throws ImageWriteException {
        if (null == value) {
            return;
        }
        exifDirectory.removeField(tagInfo);
        exifDirectory.add(tagInfo, value.shortValue());
    }

    private static void addTag(TiffOutputDirectory exifDirectory, TagInfoShort tagInfo, Integer value) throws ImageWriteException {
        if (null == value) {
            return;
        }
        exifDirectory.removeField(tagInfo);
        exifDirectory.add(tagInfo, value.shortValue());
    }

    private static void addTag(TiffOutputDirectory exifDirectory, TagInfoAscii tagInfo, String value) throws ImageWriteException {
        if (null == value) {
            return;
        }
        exifDirectory.removeField(tagInfo);
        exifDirectory.add(tagInfo, value);
    }

    private static void addTag(TiffOutputDirectory exifDirectory, TagInfoXpString tagInfo, String value) throws ImageWriteException {
        if (null == value) {
            return;
        }
        exifDirectory.removeField(tagInfo);
        exifDirectory.add(tagInfo, value);
    }

}
