package org.braun.digikam.web.component.diagram;

import java.io.IOException;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;
import org.braun.digikam.web.component.BaseRenderer;

/**
 *
 * @author mbraun
 */
@FacesRenderer(componentFamily=Diagram.COMPONENT_FAMILY, rendererType=Diagram.COMPONENT_TYPE)
public class DiagramRenderer extends BaseRenderer {
    
   @Override
   public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
      rendererParamsNotNull(context, component);
      Diagram diagram = (Diagram) component;
      ResponseWriter writer = context.getResponseWriter();
      DiagramModel model = diagram.getModel();
      if (isFilled(diagram.getHeight())) {
          model.setHeight(diagram.getHeight());
      }
      if (isFilled(diagram.getWidth())) {
          model.setHeight(diagram.getWidth());
      }
      writer.startElement("div", component);
      model.render(writer);
      writer.endElement("div");
   }
   
   private boolean isFilled(Integer value) {
       return value != null && value > 0;
   }
}
