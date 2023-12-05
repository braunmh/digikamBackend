package org.braun.digikam.backend;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.ejb.ImageCopyrightFacade;
import org.braun.digikam.backend.ejb.ImageMetadataFacade;
import org.braun.digikam.backend.ejb.TagsFacade;

/**
 *
 * @author mbraun
 */
@WebListener
public class InitListener implements ServletContextListener {

    private static final Logger LOG = LogManager.getLogger();
    
    @Inject
    private TagsFacade tagsFacade;
    
    @Inject
    private ImageMetadataFacade imageMetadataFacade;
    
    @Inject
    private ImageCopyrightFacade imageCopyrightFacade;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("Initialize application");
        NodeFactory.getInstance().refresh(tagsFacade.findAll());
        CreatorFactory.getInstance().refresh(imageCopyrightFacade.findAllCreators());
        CameraFactory.getInstance().refresh(imageMetadataFacade.findAllCameras());
        LOG.info("Initialize application done");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
    
}
