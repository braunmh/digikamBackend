/*
 * Copyright 2010 Michael H. Braun
 * 
 */

package org.braun.digikam.web.component.image;

import java.io.IOException;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;
import org.braun.digikam.web.component.BaseRenderer;
import org.braun.digikam.web.util.Util;

/**
 *
 * @author mbraun
 */
@FacesRenderer(componentFamily=Image.COMPONENT_FAMILY, rendererType=Image.COMPONENT_TYPE)
public class ImageRenderer extends BaseRenderer {

   static final String SHOULD_RENDER = "org.braun.faces.component.image";
   @Override
   public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
      rendererParamsNotNull(context, component);
      Image image = (Image)component;
      Boolean shouldRender = null != image.getUrl() && image.getUrl().length() > 0;
      ResponseWriter writer = context.getResponseWriter();
      writer.startElement("div", component);
      writer.writeAttribute("id", image.getClientId(), "id");

      if (!shouldRender) {
         writer.writeAttribute("style", "display: none", null);
         return;
      }
      String width = image.getWidth();
      String height = image.getHeight();
      if (null != image.getStyle())
         writer.writeAttribute("style", image.getStyle(), "style");
      if (null != image.getStyleClass())
         writer.writeAttribute("class", image.getStyleClass(), "styleClass");
      writer.startElement("img", component);
      if (null != image.getAlt())
         writer.writeAttribute("alt", image.getAlt(), "alt");
      if (null != image.getTitle())
         writer.writeAttribute("title", image.getTitle(), "title");
      if (!Util.isEmpty(width) && !"0".equals(width))
         writer.writeAttribute("width", width, "width");
      if (!Util.isEmpty(height) && !"0".equals(height))
         writer.writeAttribute("height", height, "height");
      writer.writeAttribute("src", getRequest(context).getContextPath() + image.getLibrary() + "/" + image.getUrl(), null);
      writer.endElement("img");
   }

   @Override
   public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
      rendererParamsNotNull(context, component);
      ResponseWriter writer = context.getResponseWriter();
      writer.endElement("div");
   }

}
