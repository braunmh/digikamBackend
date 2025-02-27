package org.braun.digikam.backend.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author mbraun
 */
public class Configuration {
 
    private static Configuration INSTANCE;
    
    private Properties props;
    
    private Configuration() {
        
    }
    
    public static void init(InputStream is) throws IOException {
        INSTANCE = new Configuration();
        INSTANCE.props = new Properties();
        INSTANCE.props.loadFromXML(is);
    }
    
    public static Configuration getInstance() {
        return INSTANCE;
    }
    
    public String getSolrClientUrl() {
        return props.getProperty("app.solr.clienturl");
    }
    
    public String getSolrCollection() {
        return props.getProperty("app.solr.collection");
    }
    
    public String getExifCameras() {
        return props.getProperty("app.exif.cameras");
    }
    
    public String getExifLenses() {
        return props.getProperty("app.exif.lenses");
    }
    
    public String getProperty(String key ) {
        return props.getProperty(key);
    }
}
