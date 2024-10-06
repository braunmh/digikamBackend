package org.braun.digikam.backend.graphics;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import static org.braun.digikam.backend.graphics.Orientation.angle180;
import static org.braun.digikam.backend.graphics.Orientation.angle270;
import static org.braun.digikam.backend.graphics.Orientation.angle90;

/**
 *
 * @author mbraun
 */
public class ImageUtil {

    public static String genThumbnail(String baseDirectory, String imageName, String outputFolder, int widthNew, int heightNew, Orientation orientation) throws IOException {
        File thumbFolder = new File(baseDirectory, outputFolder);
        if (!thumbFolder.exists()) {
            thumbFolder.mkdir();
        }
        File thumbFile = new File(thumbFolder, imageName);
        if (thumbFile.exists()) {
            return "thumb/" + imageName;
        }

        FileOutputStream outputStream = new FileOutputStream(thumbFile);

        File imageFile = new File(baseDirectory, imageName);

        getImage(imageFile, outputStream, widthNew, heightNew, orientation);

        return outputFolder + "/" + imageName;
    }

    public static void getImage(File imageFile, OutputStream outputStream, int widthNew, int heightNew, Orientation orientation) throws IOException {
        BufferedImage image = readImage(imageFile, orientation);
        if (image == null) {
            return;
        }

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage tmpImage;
        if (height > width) {

            int cropHeight = width / widthNew * heightNew;
            int crop = (height - cropHeight) / 2;

            BufferedImage croppedImage = image.getSubimage(0, crop, width, cropHeight);

            tmpImage = new BufferedImage(widthNew, heightNew, image.getType());
            Graphics2D graphics2D = tmpImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            graphics2D.drawImage(croppedImage, 0, 0, widthNew, heightNew, null);
            graphics2D.dispose();

        } else {
            tmpImage = new BufferedImage(widthNew, heightNew, image.getType());
            Graphics2D graphics2D = tmpImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.drawImage(image, 0, 0, widthNew, heightNew, null);
            graphics2D.dispose();
        }

        writeImage(tmpImage, outputStream);
    }

    /**
     * <p>
     * Returns a thumbnail of a source image.</p>
     * <p>
     * This method offers a good trade-off between speed and quality. The result looks better than
     * {@link #createThumbnailFast(java.awt.image.BufferedImage, int)} when the new size is less than half the longest dimension of
     * the source image, yet the rendering speed is almost similar.</p>
     *
     * @see #createThumbnailFast(java.awt.image.BufferedImage, int)
     * @see #createThumbnailFast(java.awt.image.BufferedImage, int, int)
     * @see #createThumbnail(java.awt.image.BufferedImage, int)
     * @param image the source image
     * @param newWidth the width of the thumbnail
     * @param newHeight the height of the thumbnail
     * @return a new compatible <code>BufferedImage</code> containing a thumbnail of <code>image</code>
     * @throws IllegalArgumentException if <code>newWidth</code> is larger than the width of <code>image</code> or if
     * <code>newHeight</code> is larger than the height of <code>image or if one the dimensions is not &gt; 0</code>
     */
    private static BufferedImage createThumbnail(BufferedImage image,
        int newWidth, int newHeight, int width, int height) {

        if (newWidth >= width || newHeight >= height) {
            throw new IllegalArgumentException("newWidth and newHeight cannot"
                + " be greater than the image"
                + " dimensions");
        } else if (newWidth <= 0 || newHeight <= 0) {
            throw new IllegalArgumentException("newWidth and newHeight must"
                + " be greater than 0");
        }

        BufferedImage thumb = image;
        BufferedImage temp = null;

        Graphics2D g2 = null;

        int previousWidth = width;
        int previousHeight = height;

        do {
            if (width > newWidth) {
                width /= 2;
                if (width < newWidth) {
                    width = newWidth;
                }
            }

            if (height > newHeight) {
                height /= 2;
                if (height < newHeight) {
                    height = newHeight;
                }
            }

            if (temp == null) {
                temp = createCompatibleImage(image, width, height);
                g2 = temp.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            }
            g2.drawImage(thumb, 0, 0, width, height,
                0, 0, previousWidth, previousHeight, null);

            previousWidth = width;
            previousHeight = height;

            thumb = temp;
        } while (width != newWidth || height != newHeight);

        g2.dispose();

        if (width != thumb.getWidth() || height != thumb.getHeight()) {
            temp = createCompatibleImage(image, width, height);
            g2 = temp.createGraphics();
            g2.drawImage(thumb, 0, 0, null);
            g2.dispose();
            thumb = temp;
        }

        return thumb;
    }

    /**
     * <p>
     * Returns a new compatible image of the specified width and height, and the same transparency setting as the image specified as
     * a parameter.</p>
     *
     * @see java.awt.Transparency
     * @see #createCompatibleImage(java.awt.image.BufferedImage)
     * @see #createCompatibleImage(int, int)
     * @see #createCompatibleTranslucentImage(int, int)
     * @see #loadCompatibleImage(java.net.URL)
     * @see #toCompatibleImage(java.awt.image.BufferedImage)
     * @param width the width of the new image
     * @param height the height of the new image
     * @param image the reference image from which the transparency of the new image is obtained
     * @return a new compatible <code>BufferedImage</code> with the same transparency as <code>image</code> and the specified
     * dimension
     */
    public static BufferedImage createCompatibleImage(BufferedImage image,
        int width, int height) {
        int type = (image.getTransparency() == Transparency.OPAQUE)
            ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        return new BufferedImage(width, height, type);
    }

    public static BufferedImage scaleImage(InputStream inputStream, int targetWidth, int targetHeight, Orientation orientation) throws IOException {
        BufferedImage img = readImage(inputStream, orientation);
        if (img == null) {
            return null;
        }

        int w = img.getWidth();
        int h = img.getHeight();
        if (targetWidth == 0 && targetHeight == 0) {
            return img;
        }
        
        if (targetWidth == 0) {
            targetWidth = w * targetHeight / h;
        } else if (targetHeight == 0) {
            targetHeight = targetWidth * h / w;
        } else {
            double qw = w / (double) targetWidth;
            double qh = h / (double) targetHeight;
            if (qh > qw) {
               targetWidth = w * targetHeight / h; 
            } else if (qw > qh) {
                targetHeight = targetWidth * h / w;
            }
        }
        if (h > w && targetHeight < targetWidth) {
            int temp = targetWidth;
            targetWidth = targetHeight;
            targetHeight = temp;
        }
        if (targetWidth >= w || targetHeight > h) {
            return img;
        }
        return createThumbnail(img, targetWidth, targetHeight, w, h);
    }

    public static void scaleImage(InputStream inputStream, OutputStream outputStream, int targetWidth, int targetHeight, Orientation orientation) throws IOException {
        BufferedImage ret = scaleImage(inputStream, targetWidth, targetHeight, orientation);
        if (null == ret) {
            return;
        }
        UnsharpMaskFilter filter = new UnsharpMaskFilter();
        ret = filter.filter(ret, null);
        writeImage(ret, outputStream);
    }

    public static void cropImage(File imageFile, OutputStream outputStream, int widthNew, int heightNew, Orientation orientation) throws IOException {
        BufferedImage image = readImage(imageFile, orientation);

        int width = image.getWidth();
        int height = image.getHeight();

        int cropHeight = widthNew * height / width;
        int crop = (height - cropHeight) / 2;

        BufferedImage croppedImage = image.getSubimage(0, crop, width, cropHeight);

        BufferedImage tmpImage = new BufferedImage(widthNew, heightNew, image.getType());
        Graphics2D graphics2D = tmpImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        graphics2D.drawImage(croppedImage, 0, 0, widthNew, heightNew, null);
        graphics2D.dispose();

        writeImage(tmpImage, outputStream);
    }

    public static void writeImage(BufferedImage image, OutputStream outputStream) throws IOException {
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByMIMEType("image/jpeg");
        ImageWriter writer = iter.next();

        try {
            ImageWriteParam imageWriteParam = writer.getDefaultWriteParam();
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionQuality(0.63f);

            writer.setOutput(ImageIO.createImageOutputStream(outputStream));
            writer.write(null, new IIOImage(image, null, null), imageWriteParam);
        } finally {
            if (writer != null) {
                writer.dispose();
            }
        }
    }

    public static BufferedImage readImage(File imageFile, Orientation orientation) throws IOException {
        switch (orientation) {
            case angle90:
                return rotate(90, ImageIO.read(imageFile));
            case angle180:
                return rotate(180, ImageIO.read(imageFile));
            case angle270:
                return rotate(270, ImageIO.read(imageFile));
            default:
                return ImageIO.read(imageFile);
        }
    }

    public static BufferedImage readImage(InputStream inputStream, Orientation orientation) throws IOException {
        switch (orientation) {
            case angle90:
                return rotate(90, ImageIO.read(inputStream));
            case angle180:
                return rotate(180, ImageIO.read(inputStream));
            case angle270:
                return rotate(270, ImageIO.read(inputStream));
            default:
                return ImageIO.read(inputStream);
        }
    }

    public static BufferedImage rotate(double angle, BufferedImage image) {
        double radians = Math.toRadians(angle);
        int w = image.getWidth(), h = image.getHeight();

        int neww, newh, x, y;
        if (angle == 90.0) {
            neww = h;
            newh = w;
            y = (newh - neww) / 2;
            x = 0;
        } else if (angle == 270.0) {
            neww = h;
            newh = w;
            x = (newh - neww) / 2;
            y = 0;
        } else {
            neww = w;
            newh = h;
            x = 0;
            y = 0;
        }

        BufferedImage result = new BufferedImage(neww, newh, image.getType());
        Graphics2D g = result.createGraphics();
        g.rotate(radians, neww / 2 + x, newh / 2 - y);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }

    public static ByteArrayOutputStream crop(InputStream bais, int width, int height) throws IOException {
        BufferedImage src = ImageIO.read(bais);
        BufferedImage clipping = crop(src, width, height);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(clipping, "JPG", baos);
        return baos;
    }

    public static BufferedImage crop(BufferedImage src, int width, int height) throws IOException {
        int x = src.getWidth() / 2 - width / 2;
        int y = src.getHeight() / 2 - height / 2;

//        System.out.println("---" + src.getWidth() + " - " + src.getHeight() + " - " + x + " - " + y);
        BufferedImage clipping = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//src.getType());  
        Graphics2D area = (Graphics2D) clipping.getGraphics().create();
        area.drawImage(src, 0, 0, clipping.getWidth(), clipping.getHeight(), x, y, x + clipping.getWidth(),
            y + clipping.getHeight(), null);
        area.dispose();

        return clipping;
    }

    public static void smartCrop(File imageFile, int width, int height, OutputStream outputStream, Orientation orientation) throws IOException {
        BufferedImage src = readImage(imageFile, orientation);

        float scale;
        if (src.getWidth() > src.getHeight()) {
            scale = (float) height / (float) src.getHeight();
            if (src.getWidth() * scale < width) {
                scale = (float) width / (float) src.getWidth();
            }
        } else {
            scale = (float) width / (float) src.getWidth();
            if (src.getHeight() * scale < height) {
                scale = (float) height / (float) src.getHeight();
            }
        }

        BufferedImage temp = scale(src, Float.valueOf(src.getWidth() * scale).intValue(),
            Float.valueOf(src.getHeight() * scale).intValue());

        temp = crop(temp, width, height);

        ImageIO.write(temp, "JPG", outputStream);
    }

    public static BufferedImage scale(BufferedImage src, int width, int height) throws IOException {
        BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dest.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(
            (double) width / src.getWidth(),
            (double) height / src.getHeight());
        g.drawRenderedImage(src, at);
        return dest;
    }

}
