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
import org.braun.digikam.backend.util.Util;

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
        LOG.info("Initialize application done");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
    
}
