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
public class LensConfiguration {

    private static final Logger LOG = LogManager.getLogger();
    
    private static final LensConfiguration INSTANCE = new LensConfiguration();
    
    private final Map<String, Double> lenses;
    
    private LensConfiguration() {
        lenses = new HashMap<>();
        String configPath = Configuration.getInstance().getExifLenses();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Lenses temp = mapper.readValue(init(configPath), Lenses.class);
            for (Lens c : temp.getLenses()) {
                lenses.put(c.getLens(), c.getFocalLength());
            }
        } catch (IOException e) {
            LOG.error("Lens Configurtion can not be read from configPath '{}'.", configPath);
        }
    }
    
    public static LensConfiguration getInstance() {
        return INSTANCE;
    }
    
    public double getFocalLength(String lens) {
        return lenses.getOrDefault(lens, 0.0);
    }
    
    private InputStream init(String configPath) throws IOException  {
        if (configPath == null) {
            return this.getClass().getClassLoader().getResourceAsStream("lenses.json");
        } else {
            return new FileInputStream(configPath);
        }
    }
}
