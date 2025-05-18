package org.braun.digikam.web.ui;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.api.NotFoundException;
import org.braun.digikam.backend.ejb.ImageFacade;
import org.braun.digikam.backend.ejb.VideoFacade;
import org.braun.digikam.backend.model.ImageInternal;
import org.braun.digikam.backend.model.Media;
import org.braun.digikam.backend.model.VideoInternal;
import org.braun.digikam.web.model.CatAperture;
import org.braun.digikam.web.model.CatExposure;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DialogFrameworkOptions;

/**
 *
 * @author mbraun
 */
@Named(value = "mediaDetailBean")
@ViewScoped
public class MediaDetailBean implements DialogBean, Serializable {

    private static final Logger LOG = LogManager.getLogger();

    private List<Detail> details;

    private transient DateTimeFormatter isoDate = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private Long mediaId;
    
    private boolean image;
    
    @Inject
    private ImageFacade imageFacade;

    @Inject
    private VideoFacade videoFacade;

    public List<Detail> getDetails() {
        onload();
        return details;
    }

    public static void openDialog(Media media, int innerWidth) {
        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
            .modal(true)
            .fitViewport(true)
            .responsive(true)
            .resizable(true)
            .draggable(false).contentWidth("490px")
            .closeOnEscape(true)
            .build();
        if (innerWidth < 640) {
            options.setContentWidth(String.valueOf(innerWidth) + "px");
        }
        PrimeFaces.current().dialog().openDynamic("/dialog/mediaDetailDialog", options, 
                DialogParameters.builder()
                .parameter(DialogParameters.Parameter.builder("mediaId").add(media.getId()))
                .parameter(DialogParameters.Parameter.builder("image").add(media.getImage()))
                .build());
    }
    
    @Override
    public void onload() {
        if (details == null) {
            details = new ArrayList<>();
            if (mediaId != null) {
                try {
                    if (isImage()) {
                        ImageInternal img = imageFacade.getMetadata(mediaId);
                        details.add(new Detail("image.date", (img.getCreationDate() == null) ? "" : isoDate.format(img.getCreationDate())));
                        details.add(new Detail("common.id", img.getId()));
                        details.add(new Detail("image.name", img.getName()));
                        details.add(new Detail("image.title", img.getTitle()));
                        details.add(new Detail("image.description", img.getDescription()));
                        details.add(new Detail("image.keywords", String.join(", ", img.getKeywords().stream().map(k -> k.getName()).toList())));
                        details.add(new Detail("image.rating", img.getRating()));
                        details.add(new Detail("image.creator", img.getCreator()));
                        details.add(new Detail("image.camera", img.getMake() + " " + img.getModel()));
                        details.add(new Detail("image.lens", img.getLens()));
                        details.add(new Detail("image.focalLength", img.getFocalLength35()));
                        details.add(new Detail("image.dimension", "" + img.getHeight() + " x " + img.getWidth()));
                        details.add(new Detail("image.exposure", CatExposure.findNearest(img.getExposureTime())));
                        details.add(new Detail("image.iso", img.getIso()));
                        details.add(new Detail("image.aperture", CatAperture.findNearest(img.getAperture())));
                        details.add(new Detail("image.latitude", String.valueOf(img.getLatitude()) + "°"));
                        details.add(new Detail("image.longitude", String.valueOf(img.getLongitude()) + "°"));
                    } else {
                        VideoInternal vid = videoFacade.getMetadata(mediaId);
                        details.add(new Detail("image.date", (vid.getCreationDate() == null) ? "" : isoDate.format(vid.getCreationDate())));
                        details.add(new Detail("common.id", vid.getId()));
                        details.add(new Detail("image.name", vid.getName()));
                        details.add(new Detail("image.title", vid.getTitle()));
                        details.add(new Detail("image.description", vid.getDescription()));
                        details.add(new Detail("image.keywords", String.join(", ", vid.getKeywords().stream().map(k -> k.getName()).toList())));
                        details.add(new Detail("image.rating", vid.getRating()));
                        details.add(new Detail("image.creator", vid.getCreator()));
                        details.add(new Detail("image.dimension", "" + vid.getHeight() + " x " + vid.getWidth()));
                        details.add(new Detail("video.duration", formatDuration(vid.getDuration())));
                        details.add(new Detail("image.latitude", String.valueOf(vid.getLatitude()) + "°"));
                        details.add(new Detail("image.longitude", String.valueOf(vid.getLongitude()) + "°"));
                    }

                } catch (NotFoundException e) {
                    LOG.error("Image with id={} not found", mediaId);
                }
            }
        }
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public void closeDialog() {
        PrimeFaces.current().dialog().closeDynamic(true);
    }

    private String formatDuration(Integer duration)  {
        if (duration == null) {
            return "";
        }
        int millisecondes = duration % 1000;
        duration = duration / 1000;
        int seconds = (duration == 0) ? 0 : duration % 60;
        int minutes = (duration == 0) ? 0 : duration / 60;
        return (String.format("%d:%02d.%03d Minuten", minutes, seconds, millisecondes));
    }

    public class Detail {

        private final String name;
        private final String value;

        public Detail(String name, String value) {
            this.name = name;
            this.value = (value == null) ? "" : value;
        }

        public Detail(String name, Double value) {
            this.name = name;
            this.value = (value == null) ? "" : String.valueOf(value);
        }

        public Detail(String name, Integer value) {
            this.name = name;
            this.value = (value == null) ? "" : String.valueOf(value);
        }

        public Detail(String name, Long value) {
            this.name = name;
            this.value = (value == null) ? "" : String.valueOf(value);
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

    }
}
