package org.braun.digikam.backend;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.ejb.ImageCopyrightFacade;
import org.braun.digikam.backend.ejb.ImageMetadataFacade;
import org.braun.digikam.backend.ejb.TagsFacade;
import org.braun.digikam.backend.util.Configuration;
import org.braun.digikam.backend.util.Util;

/**
 *
 * @author mbraun
 */
@WebListener
public class InitListener implements ServletContextListener {

    private static final Logger LOG = LogManager.getLogger();
    
    @EJB
    private TagsFacade tagsFacade;
    
    @EJB
    private ImageMetadataFacade imageMetadataFacade;
    
    @EJB
    private ImageCopyrightFacade imageCopyrightFacade;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("Initialize application");
        String configPath = sce.getServletContext().getInitParameter("org.braun.digikam.configPath");
        
        try (InputStream is = getConfiguartion(configPath)) {
            Configuration.init(is);
            LOG.info("Configuartion read. solr.clientUrl = {}", Configuration.getInstance().getSolrClientUrl());
        } catch (IOException e) {
            LOG.error("Configuration config.xml {} not found.", configPath);
        }
        
        if (tagsFacade == null) {
            tagsFacade = Util.EJB.lookup(TagsFacade.class);
        }
        NodeFactory.getInstance().refresh(tagsFacade.findAll());
        if (imageCopyrightFacade == null) {
            imageCopyrightFacade = Util.EJB.lookup(ImageCopyrightFacade.class);
        }
        CreatorFactory.getInstance().refresh(imageCopyrightFacade.findAllCreators());
        if (imageMetadataFacade == null) {
            imageMetadataFacade = Util.EJB.lookup(ImageMetadataFacade.class);
        }
        CameraFactory.getInstance().refresh(imageMetadataFacade.findAllCameras());
        CameraLensFactory.getInstance().refresh(imageMetadataFacade.getAllCombinations());
        LOG.info("Initialize application done");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
    
    private InputStream getConfiguartion(String configPath) throws IOException {
        if (configPath == null) {
            return this.getClass().getClassLoader().getResourceAsStream("/config.xml");
        } else {
            return new FileInputStream(configPath);
        }
    }
}
