package org.braun.digikam.web.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.braun.digikam.backend.CameraFactory;
import org.braun.digikam.backend.CameraLensFactory;
import org.braun.digikam.backend.ejb.ImageMetadataFacade;
import org.braun.digikam.web.component.diagram.DiagramModel;
import org.braun.digikam.web.component.diagram.Point;
import org.braun.digikam.web.model.CatDiagram;
import org.braun.digikam.web.model.DiagramParameter;
import org.braun.digikam.web.model.ValidationException;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author mbraun
 */
@Named("diagramBean")
@ViewScoped
public class DiagramBean implements Serializable {

    private static final Logger LOG = LogManager.getLogger();

    private DiagramParameter content;

    @Inject
    private ImageMetadataFacade imageMetadatafacade;

    @Inject
    private SessionUserBean userBean;

    private DiagramModel<Double> model;

    @PostConstruct
    public void init() {
        content = new DiagramParameter();
    }

    public String execute() {
        try {
            content.isValid();
            List<Point.Count<Double>> points = null;
            DiagramModel.Builder<Double> builder = new DiagramModel.Builder<Double>()
                    .height(2 * userBean.getInnerWidth() / 3)
                    .width(userBean.getInnerWidth());
            switch (content.getDiagram().getId()) {
                case CatDiagram.EXPOSURE_TIME -> {
                    points = imageMetadatafacade.getExposureTime(content.getMake(), content.getModel(), content.getLens());
                    builder.axisXFormat(d -> {
                        if (d == null || d == 0) {
                            return "";
                        }
                        d = Math.pow(2, d);
                        if (d > 1) {
                            return String.format("%.1f", d);
                        }
                        return "1/" + Math.round(1 / d);
                    })
                            .axisXUnit("s")
                            .axisXDescription("Belichtungszeit");
                }
                case CatDiagram.APERTURE -> {
                    points = imageMetadatafacade.getAperture(content.getMake(), content.getModel(), content.getLens());
                    builder.axisXUnit("f")
                            .axisXFormat(n -> {
                                Double t = n * n;
                                return String.format("%.1f", t);
                            })
                            .axisXDescription("Blende");
                }
                case CatDiagram.FOCALLENGTH -> {
                    points = imageMetadatafacade.getFocalLenth(content.getMake(), content.getModel(), content.getLens());
                    builder.axisXFormat(d -> String.valueOf(d.longValue()))
                            .axisXUnit("mm")
                            .axisXDescription("Brennweite");
                }
                case CatDiagram.ISO -> {
                    points = imageMetadatafacade.getIso(content.getMake(), content.getModel(), content.getLens());
                    builder.axisXUnit("ISO")
                            .axisXFormat(n -> {
                                Double t = n * n;
                                return String.valueOf(t.longValue());
                            })
                            .axisXDescription("ISO");
                }
                default -> {
                }
            }

            if (points == null || points.size() < 2) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,
                                "Zu der Kombination liegen keine Daten vor.",
                                "Zu der Kombination liegen keine Daten vor."));
                model = null;
                return null;
            }
            model = builder.points(points).build();
        } catch (ValidationException e) {
            FacesContext.getCurrentInstance().addMessage(
                    e.getField(),
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
        }
        return null;
    }

    public String reset() {
        init();
        return null;
    }

    public void changeAction(AjaxBehaviorEvent event) {
        LOG.info("Values changed by autocomplete.ajax. ClientId: {}, Id: {}",
                event.getComponent().getClientId(), event.getComponent().getId());
    }

    public List<String> completeCamera(String query) {
        return CameraFactory.getInstance().findByMakerAndModel(query).stream().map(c -> c.getName()).toList();
    }

    public List<String> completeLens(String query) {
        return CameraLensFactory.getInstance().getLensByCameraAndLens(content.getCamera(), query);
    }

    public List<CatDiagram> getDiagramValues() {
        return CatDiagram.values;
    }

    public DiagramParameter getContent() {
        return content;
    }

    public void setContent(DiagramParameter content) {
        this.content = content;
    }

    public DiagramModel<Double> getModel() {
        return model;
    }

    public void setModel(DiagramModel<Double> model) {
        this.model = model;
    }
}
