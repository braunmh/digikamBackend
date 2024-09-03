package org.braun.digikam.backend.ejb;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.bytedeco.javacv.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author mbraun
 */
public class Schlampe {
    
    //@Test
    public void idToken() {
        String ie = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFlOWdkazcifQ.ewogImlzcyI6ICJodHRwOi8vc2VydmVyLmV4YW1wbGUuY29tIiwKICJzdWIiOiAiMjQ4Mjg5NzYxMDAxIiwKICJhdWQiOiAiczZCaGRSa3F0MyIsCiAibm9uY2UiOiAibi0wUzZfV3pBMk1qIiwKICJleHAiOiAxMzExMjgxOTcwLAogImlhdCI6IDEzMTEyODA5NzAKfQ.ggW8hZ1EuVLuxNuuIJKX_V8a_OMXzR0EHR9R6jgdqrOOF4daGU96Sr_P6qJp6IcmD3HP99Obi1PRs-cwh3LO-p146waJ8IhehcwL7F09JdijmBqkvPeB2T9CJNqeGpe-gccMg4vfKjkM8FcGvnzZUN4_KSP0aAp1tOJ1zZwgjxqGByKHiOtX7TpdQyHE5lcMiKPXfEIQILVq0pc_E2DzL7emopWoaoZTF_m0_N0YzFC6g6EJbOEoRoSK5hoDalrcvRYLSrQAZZKflyuVCyixEoV9GfNQC3_osjzw2PAithfubEEBLuVVk4XUVrWOLrLl0nx7RkKU8NXNHq-rvKMzqg";
        String[] parts = ie.split("\\.");
        String header = new String (Base64.getDecoder().decode(parts[0]));
        String payload = new String (Base64.getDecoder().decode(parts[1]));
        String signature = parts[2]; //new String (Base64.getDecoder().decode(parts[2]));
        System.out.println(header);
        System.out.println(payload);
        System.out.println(signature);
    }

    @Test
    public void getPath() {
        List<Long> ids = Arrays.asList(10112l, 10204l, 10261l, 10652l);
        List<String> values = ids.stream().map(i -> String.valueOf(i)).toList();
        System.out.println(String.join(" ", values));
        String root = "/data/pictures&fileuuid=73f31a67-17ee-48da-85c5-0fef54129c50";
        String name = "20240623111239sonyilce-7rm50037.jpg";
        String relativePath = "/2024/06/23";
        int endRoot = root.indexOf("&");
        StringBuilder path = new StringBuilder();
        path.append((endRoot > 0) ? root.subSequence(0, endRoot) : root);
        path.append(relativePath).append('/').append(name);
        System.out.println(path.toString());
        
        String[] parts = path.toString().split("/");
        
        root = "/" + parts[1] + "/" + parts[2];
        name = parts[parts.length - 1];
        relativePath = "/" + String.join("/", Arrays.asList(Arrays.copyOfRange(parts, 3, parts.length - 1)));
        
        System.out.println("root: " + root + ", relativePath: " + relativePath + ", name: " + name);
    }
    
    //@Test
    public void testDuration() {
        Duration duration = Duration.ofMillis(4882000l);
        System.out.println(duration);
    }
//    @Test
    public void readKeywords() {
        try {
            JsonReader reader = Json.createReader(new FileReader("/home/mbraun/.local/share/photils/override_labels.json"));
            JsonObject jo = reader.readObject();
            for (Map.Entry<String, JsonValue> entry : jo.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue().toString());
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
    
   // @Test
    public void test() throws IOException {
        FileInputStream inputStream = new FileInputStream("/data/videos/2018/20181013/00007.MTS");
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputStream);
        grabber.start();
//        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter java2DConverter = new Java2DFrameConverter();
        BufferedImage image = null;
        while (image == null) {
            image = java2DConverter.getBufferedImage(grabber.grabImage());
        }

        ImageIO.write(image, "jpg", new File("/data/videos/2018/20181013/00007_thumb.jpg"));
        grabber.stop();
    } 
}
