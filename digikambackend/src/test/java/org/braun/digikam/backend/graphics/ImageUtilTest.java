package org.braun.digikam.backend.graphics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.braun.digikam.backend.util.Configuration;
import org.braun.digikam.backend.util.exif.Camera;
import org.braun.digikam.backend.util.exif.Cameras;
import org.braun.digikam.backend.util.exif.ExifData;
import org.braun.digikam.backend.util.exif.Lens;
import org.braun.digikam.backend.util.exif.Lenses;
import org.junit.jupiter.api.Test;

/**
 *
 * @author mbraun
 */
public class ImageUtilTest {

    //@Test
    public void genThumnbnail() {
        File file = new File("/data/pictures/2024/10/12/20241012141836sonyilce-7rm50021.jpg");
        if (!file.exists()) {
            System.out.println("Wrong file");
            return;
        }
        try (OutputStream os = new FileOutputStream("/data/temp/" + file.getName()); InputStream is = new FileInputStream(file)) {
            ImageUtil.scaleImage(is, os, 1024, 1024, Orientation.angle0);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    //@Test
    public void testLensFromExifApache() {
        try {
            File imageFile = new File("/data/pictures/2025/02/16/20250216152636sonyilce-64000321.jpg");
            final ImageMetadata metadata = Imaging.getMetadata(imageFile);
            if (!(metadata instanceof JpegImageMetadata)) {
                return;
            }
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            if (null != jpegMetadata) {
                final TiffImageMetadata exif = jpegMetadata.getExif();
                if (null != exif) {
                    for (TiffField field : exif.getAllFields()) {
                        System.out.println(String.format("Type: %s, Name: %s, Value: %s", field.getFieldTypeName(), field.getTagName(), field.getValue()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExifTool() {
        try {
            Configuration.init(getConfiguartion(null));

            File[] images = new File[]{
                new File("/data/pictures/2025/02/16/20250216151713sonyilce-7rm50520.jpg"),
                new File("/data/pictures/2025/02/16/20250216152636sonyilce-64000321.jpg")
            };

            try (ExifTool exifTool = new ExifToolBuilder().enableStayOpen().build()) {
                for (File imageFile : images) {
                    System.out.println(imageFile.getName());
                    ExifData exifData = new ExifData(exifTool, imageFile);
                    double fl = exifData.getFocalLength(exifData.getLens());
                    String makeAndModel = exifData.getMakeAndModelFromExif();
                    System.out.println(String.format("Kamera: %s, Objektiv: %s, Brennweite: %s, Brennweite 35: %s", 
                            makeAndModel, exifData.getLens(), fl, exifData.getFocalLength35(fl, makeAndModel)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException  | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ttt() throws JsonProcessingException {
        Lenses lenses = new Lenses();
        lenses.addLensesItem(new Lens().lens("LAOWA 90mm f/2,8 2x Ultra Macro APO").focalLength(90));
        lenses.addLensesItem(new Lens().lens("Samyang T-S 24mm f/3.5 ED AS UMC").focalLength(24));
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(lenses);
        System.out.println(json);
        
        Lenses l2 = mapper.readValue(json, Lenses.class);
        System.out.println(l2);
        
        Cameras c1 = new Cameras();
        c1.addCamerasItem(new Camera().crop(1).make("SONY").model("ILCA-99M2"));
        c1.addCamerasItem(new Camera().crop(1).make("SONY").model("ILCE-7RM5"));
        c1.addCamerasItem(new Camera().crop(1.5f).make("SONY").model("ILCE-6400"));
        
        json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(c1);
        System.out.println(json);
        
        Cameras c2 = mapper.readValue(json, Cameras.class);
        System.out.println(c2);
    }
    
    private InputStream getConfiguartion(String configPath) throws IOException {
        if (configPath == null) {
            return this.getClass().getClassLoader().getResourceAsStream("config.xml");
        } else {
            return new FileInputStream(configPath);
        }
    }
}
