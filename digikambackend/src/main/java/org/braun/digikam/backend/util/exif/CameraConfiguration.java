package org.braun.digikam.backend.util.exif;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.util.Configuration;

/**
 *
 * @author mbraun
 */
public class CameraConfiguration {

    private static final Logger LOG = LogManager.getLogger();
    
    private static final CameraConfiguration INSTANCE = new CameraConfiguration();
    
    private final Map<String, Float> cameras;
    
    private CameraConfiguration() {
        cameras = new HashMap<>();
        String configPath = Configuration.getInstance().getExifCameras();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Cameras temp = mapper.readValue(init(configPath), Cameras.class);
            for (Camera c : temp.getCameras()) {
                cameras.put(c.getMake() + ", " + c.getModel(), c.getCrop());
            }
        } catch (IOException e) {
            LOG.error("Camera Configurtion can not be read from configPath '{}'.", configPath);
        }
    }
    
    public static CameraConfiguration getInstance() {
        return INSTANCE;
    }
    
    public float getCrop(String makeAndModel) {
        return cameras.getOrDefault(makeAndModel, 1.0f);
    }
    
    public float getCrop(String make, String model) {
        return cameras.getOrDefault(make + ", " + model, 1.0f);
    }
    
    private InputStream init(String configPath) throws IOException  {
        if (configPath == null) {
            return this.getClass().getClassLoader().getResourceAsStream("cameras.json");
        } else {
            return new FileInputStream(configPath);
        }
    }
}
