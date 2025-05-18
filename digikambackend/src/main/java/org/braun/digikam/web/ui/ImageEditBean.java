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
import org.braun.digikam.backend.ejb.VideoFacade;
import org.braun.digikam.backend.entity.Tags;
import org.braun.digikam.backend.model.ImageInternal;
import org.braun.digikam.backend.model.Keyword;
import org.braun.digikam.backend.model.Media;
import org.braun.digikam.backend.model.VideoInternal;
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

    @Inject
    private VideoFacade videoFacade;

    private long mediaId;
    
    private boolean image;

    private ImageEditModel model;

    private ImageInternal getImageInternal() {
        try {
            return imageFacade.getMetadata(mediaId);
        } catch (NotFoundException e) {
            LOG.error("Image width id {} not found.", mediaId);
        }
        return null;
    }

    private VideoInternal getVideoInternal() {
        try {
            return videoFacade.getMetadata(mediaId);
        } catch (NotFoundException e) {
            LOG.error("Image width id {} not found.", mediaId);
        }
        return null;
    }

    public static void openDialog(Media media, int innerWidth) {
        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
               .modal(true)
               .fitViewport(true)
               .responsive(true)
               .resizable(false)
               .draggable(false)
               .closeOnEscape(true)
               .build();
        if (innerWidth < 640) {
            options.setContentWidth(String.valueOf(innerWidth) + "px");
        }
        PrimeFaces.current().dialog().openDynamic("/dialog/imageEditDialog", options, 
                DialogParameters.builder()
                .parameter(DialogParameters.Parameter.builder("mediaId").add(media.getId()))
                .parameter(DialogParameters.Parameter.builder("image").add(media.getImage()))
                .build());
    }
    
    @Override
    public void onload() {
        model = new ImageEditModel();
        if (isImage()) {
            ImageInternal imageInternal = getImageInternal();
            if (imageInternal != null) {
                model.setRating(CatRating.findById(imageInternal.getRating()));
                model.setTitle(imageInternal.getTitle());
                model.setDescription(imageInternal.getDescription());
                model.setCreator(imageInternal.getCreator());
                model.getKeywords().addAll(imageInternal.getKeywords());
                model.setCreationDate(imageInternal.getCreationDate());
            }
        } else {
            VideoInternal videoInternal = getVideoInternal();
            if (videoInternal != null) {
                model.setRating(CatRating.findById(videoInternal.getRating()));
                model.setTitle(videoInternal.getTitle());
                model.setDescription(videoInternal.getDescription());
                model.setCreator(videoInternal.getCreator());
                model.getKeywords().addAll(videoInternal.getKeywords());
                model.setCreationDate(videoInternal.getCreationDate());
            }
        }
    }

    public ImageEditModel getModel() {
        return model;
    }

    public long getMediaId() {
        return mediaId;
    }

    public void setMediaId(long mediaId) {
        this.mediaId = mediaId;
        onload();
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public void save() {
        List<Tags> tags = new ArrayList<>();
        for (Keyword k : model.getKeywords()) {
            tags.add(new Tags()
                    .name(NodeFactory.getInstance().getKeywordById(k.getId()).getName())
                    .id(k.getId()));
        }
        
        try {
            if (isImage()) {
                imageFacade.update(mediaId, model.getTitle(), model.getDescription(), 
                    model.getRating().getValue(), tags, 
                    model.getCreator(), model.getCreationDate());
            } else {
                videoFacade.update(mediaId, model.getTitle(), model.getDescription(), 
                    model.getRating().getValue(), tags, 
                    model.getCreator(), model.getCreationDate());
            }
            close();
        } catch (NotFoundException e) {
            LOG.error("Image with id {} not exists any more.", mediaId);
        }
    }

    @Override
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
