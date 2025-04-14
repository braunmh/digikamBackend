package org.braun.digikam.web.ui;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.CreatorFactory;
import org.braun.digikam.backend.NodeFactory;
import org.braun.digikam.backend.api.NotFoundException;
import org.braun.digikam.backend.ejb.ImageFacade;
import org.braun.digikam.backend.ejb.ImagesFacade;
import org.braun.digikam.backend.entity.Tags;
import org.braun.digikam.backend.model.ImageInternal;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.web.model.CatRating;
import org.braun.digikam.web.model.ImageEditModel;
import org.primefaces.PrimeFaces;

/**
 *
 * @author mbraun
 */
@Named(value = "imageEditBean")
@ViewScoped
public class ImageEditBean implements Serializable {

    private static final Logger LOG = LogManager.getLogger();

    @Inject
    private ImageFacade imageFacade;

    @Inject
    private ImagesFacade imagesFacade;

    private long imageId;

    private ImageEditModel model;

    private ImageInternal getImageInternal() {
        try {
            return imageFacade.getMetadata(imageId);
        } catch (NotFoundException e) {
            LOG.error("Image width id {} not found.", imageId);
        }
        return null;
    }

    public void init() {
        model = new ImageEditModel();
        ImageInternal imageInternal = getImageInternal();
        if (imageInternal != null) {
            model.setRating(imageInternal.getRating());
            model.setTitle(imageInternal.getTitle());
            model.setDescription(imageInternal.getDescription());
            model.setCreator(imageInternal.getCreator());
            model.getKeywords().addAll(imageInternal.getKeywords());
        }

    }

    public ImageEditModel getModel() {
        return model;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public String save() {
        List<Tags> tags = new ArrayList<>();
        for (Keyword k : model.getKeywords()) {
            tags.add(new Tags()
                    .name(NodeFactory.getInstance().getKeywordById(k.getId()).getName())
                    .id(k.getId()));
        }
        try {
            imagesFacade.update(imageId, model.getTitle(), model.getDescription(), model.getRating(), tags, model.getCreator());
        } catch (NotFoundException e) {
            LOG.error("Image with id {} not exists any more.", imageId);
        }
        return null;
    }

    public void close() {
        PrimeFaces.current().dialog().closeDynamic("");
    }
    
    public List<String> completeCreator(String query) {
        return CreatorFactory.getInstance().findByName(query).stream().map(c -> c.getName()).toList();
    }

    public List<Keyword> completeKeyword(String query) {
        return NodeFactory.getInstance().getKeywordByFullName(query.toLowerCase());
    }

    public List<CatRating> getRatingValues() {
        return CatRating.values;
    }
    
}
