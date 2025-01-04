package org.braun.digikam.web.ui;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.api.NotFoundException;
import org.braun.digikam.backend.ejb.ImageFacade;
import org.braun.digikam.backend.model.ImageInternal;
import org.braun.digikam.backend.model.Media;
import org.braun.digikam.web.model.CatAperture;
import org.braun.digikam.web.model.CatExposure;
import org.primefaces.PrimeFaces;

/**
 *
 * @author mbraun
 */
@Named(value = "mediaDetailBean")
@ViewScoped
public class MediaDetailBean implements Serializable {

    private static final Logger LOG = LogManager.getLogger();

    private List<Detail> details;

    private transient DateTimeFormatter isoDate = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private Media media;

    @Inject
    private ImageFacade facade;

    public List<Detail> getDetails() {
        onLoad();
        return details;
    }

//    public void init() {
//        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
//                .modal(true)
//                .closable(true)
//                .build();
//
//        String target = "/search/mediaDetail";
//        PrimeFaces.current().dialog().openDynamic(target, options,
//                new ParameterBuilder()
//                        .add("mediaId", mediaId)
//                        .add("image", image)
//                        .build());
//    }
//

    
    public void onLoad() {
        if (details == null) {
            details = new ArrayList<>();
            if (media != null && media.getImage()) {
                try {
                    ImageInternal image = facade.getMetadata(media.getId());
                    details.add(new Detail("Datum", isoDate.format(image.getCreationDate())));
                    details.add(new Detail("id", image.getId()));
                    details.add(new Detail("Name", image.getName()));
                    details.add(new Detail("Titel", image.getTitle()));
                    details.add(new Detail("Beschreibung", image.getDescription()));
                    details.add(new Detail("Stichworte", String.join(", ", image.getKeywords().stream().map(k -> k.getName()).toList())));
                    details.add(new Detail("Bewertung", image.getRating()));
                    details.add(new Detail("Urheber", image.getCreator()));
                    details.add(new Detail("Kamera", image.getMake() + " " + image.getModel()));
                    details.add(new Detail("Objektiv", image.getLens()));
                    details.add(new Detail("Brennweite", image.getFocalLength35()));
                    details.add(new Detail("Höhe x Breite", "" + image.getHeight() + " x " + image.getWidth()));
                    details.add(new Detail("Belichtungszeit", CatExposure.findNearest(image.getExposureTime())));
                    details.add(new Detail("ISO", image.getIso()));
                    details.add(new Detail("Blende", CatAperture.findNearest(image.getAperture())));
                    details.add(new Detail("Breitengrad", String.valueOf(image.getLatitude()) + "°"));
                    details.add(new Detail("Längengrad", String.valueOf(image.getLongitude()) + "°"));

                } catch (NotFoundException e) {
                    LOG.error("Image with id={} not found", media.getId());
                }
            }
        }
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
        details = null;
    }

    public void closeDialog() {
        PrimeFaces.current().dialog().closeDynamic("");
    }

    public class ParameterBuilder {

        private final Map<String, List<String>> params;

        public ParameterBuilder() {
            params = new HashMap<>();
        }

        public ParameterBuilder add(String name, boolean value) {
            params.put(name, Arrays.asList(String.valueOf(value)));
            return this;
        }

        public ParameterBuilder add(String name, long value) {
            params.put(name, Arrays.asList(String.valueOf(value)));
            return this;
        }

        public ParameterBuilder add(String name, String value) {
            params.put(name, Arrays.asList(value));
            return this;
        }

        public ParameterBuilder add(String name, List<String> values) {
            params.put(name, values);
            return this;
        }

        public Map<String, List<String>> build() {
            return params;
        }
    }

    public class Detail {

        private final String name;
        private final String value;

        public Detail(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public Detail(String name, double value) {
            this.name = name;
            this.value = String.valueOf(value);
        }

        public Detail(String name, int value) {
            this.name = name;
            this.value = String.valueOf(value);
        }

        public Detail(String name, long value) {
            this.name = name;
            this.value = String.valueOf(value);
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

    }
}
