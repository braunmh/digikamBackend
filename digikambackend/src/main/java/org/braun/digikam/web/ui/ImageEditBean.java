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
import org.braun.digikam.backend.entity.Tags;
import org.braun.digikam.backend.model.ImageInternal;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.web.model.CatRating;
import org.braun.digikam.web.model.ImageEditModel;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DialogFrameworkOptions;

/**
 *
 * @author mbraun
 */
@Named(value = "imageEditBean")
@ViewScoped
public class ImageEditBean implements DialogBean, Serializable {

    private static final Logger LOG = LogManager.getLogger();

    @Inject
    private ImageFacade imageFacade;

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

    public static void openDialog(long imageId) {
        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
               .contentHeight("650px")
               .modal(true).fitViewport(true)
               .responsive(true)
               .resizable(false)
               .draggable(false)
               .styleClass("")
               .iframeStyleClass("")
               .closeOnEscape(true)
               .build();
        PrimeFaces.current().dialog().openDynamic("/admin/imageEditDialog", options, 
                DialogParameters.builder()
                .parameter(DialogParameters.Parameter.builder("imageId").add(imageId)).build());
    }
    
    @Override
    public void onload() {
        model = new ImageEditModel();
        ImageInternal imageInternal = getImageInternal();
        if (imageInternal != null) {
            model.setRating(CatRating.findById(imageInternal.getRating()));
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
        onload();
    }

    public void save() {
        List<Tags> tags = new ArrayList<>();
        for (Keyword k : model.getKeywords()) {
            tags.add(new Tags()
                    .name(NodeFactory.getInstance().getKeywordById(k.getId()).getName())
                    .id(k.getId()));
        }
        try {
            imageFacade.update(imageId, model.getTitle(), model.getDescription(), model.getRating().getValue(), tags, model.getCreator());
        } catch (NotFoundException e) {
            LOG.error("Image with id {} not exists any more.", imageId);
        }
    }

    public void close() {
        PrimeFaces.current().dialog().closeDynamic(true);
    }
    
    public List<String> completeCreator(String query) {
        return CreatorFactory.getInstance().findByName(query).stream().map(c -> c.getName()).toList();
    }

    public List<Keyword> completeKeyword(String query) {
        return NodeFactory.getInstance().getKeywordByFullName(query.toLowerCase());
    }

    public List<CatRating> completeRating(String query) {
        return CatRating.values.stream().filter(r -> r.getName().contains(query)).toList();
    }
    
}
