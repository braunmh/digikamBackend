package org.braun.digikam.backend.util.exif;

import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.StandardTag;
import com.thebuzzmedia.exiftool.core.UnspecifiedTag;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.braun.digikam.common.ListBuilder;

/**
 *
 * @author mbraun
 */
public class ExifData {

    private static final UnspecifiedTag LENS_TAG = new UnspecifiedTag("Lens");

    private static List<Tag> TAGS = new ListBuilder<Tag>()
            .addArray(StandardTag.LENS_MAKE, StandardTag.LENS_MODEL)
            .add(LENS_TAG)
            .addArray(StandardTag.FOCAL_LENGTH, StandardTag.FOCAL_LENGTH_35MM)
            .addArray(StandardTag.MAKE, StandardTag.MODEL)
            .build();

    private Map<Tag, String> result;

    public ExifData(ExifTool exifTool, File imageFile) {
        try {
            result = exifTool.getImageMeta(imageFile, TAGS);
        } catch (IOException e) {
            result = new HashMap<>();
        }
    }

    public String getLens() {
        if (isEmpty(result.get(StandardTag.LENS_MODEL))) {
            return result.get(LENS_TAG);
        } else {
            return result.get(StandardTag.LENS_MODEL);
        }
    }

    public double getFocalLength(String lens) {
        if (getFocalLengthFromExif() == 0) {
            return LensConfiguration.getInstance().getFocalLength(lens);
        }
        return getFocalLengthFromExif();
    }
    
    public double getFocalLength35(double focalLength, String makeAndModel) {
        return CameraConfiguration.getInstance().getCrop(makeAndModel) * focalLength;
    }
    
    public double getFocalLength35(double focalLength, String make, String model) {
        return CameraConfiguration.getInstance().getCrop(make, model) * focalLength;
    }
    
    public String getMakeAndModelFromExif() {
        return result.get(StandardTag.MAKE) + ", " + result.get(StandardTag.MODEL);
    }
    
    private double getFocalLengthFromExif() {
        try {
            return Double.parseDouble(result.get(StandardTag.FOCAL_LENGTH));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean isEmpty(String value) {
        return (StringUtils.isBlank(value) || "----".equals(value));
    }
}
