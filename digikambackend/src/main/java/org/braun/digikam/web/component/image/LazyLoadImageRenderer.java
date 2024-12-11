/*
 * Copyright 2010 Michael H. Braun
 * 
 */

package org.braun.digikam.web.component.image;

import java.io.IOException;
import jakarta.faces.application.ResourceDependencies;
import jakarta.faces.application.ResourceDependency;
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
@ResourceDependencies ({
   @ResourceDependency(target="head", name="jquery/lib/jquery-1.10.2.js"),
   @ResourceDependency(target="head", name="lib/jquery.lazyload.js"),
   @ResourceDependency(target="head", name="lib/jquery.lazyload.init.js")
})
@FacesRenderer(componentFamily=LazyLoadImage.COMPONENT_FAMILY, rendererType=LazyLoadImage.COMPONENT_TYPE)
public class LazyLoadImageRenderer extends BaseRenderer {

   @Override
   public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
      rendererParamsNotNull(context, component);
      LazyLoadImage image = (LazyLoadImage)component;
      ResponseWriter writer = context.getResponseWriter();
      writer.startElement("img", component);

      int width = getInt(image.getWidth());
      int height = getInt(image.getHeight());
      boolean isQueryFormat = isQuerformat(image.getOrientation());
      /*
      int widthOriginal = (isQueryFormat)?getInt(image.getWidthOriginal()):getInt(image.getHeightOriginal());
      int heightOriginal = (isQueryFormat)?getInt(image.getHeightOriginal()):getInt(image.getWidthOriginal());;
       */
      int widthOriginal = getInt(image.getWidthOriginal());
      int heightOriginal = getInt(image.getHeightOriginal());
      
      if (heightOriginal > 0 && widthOriginal > 0) {
         if (width > 0) {
            height = heightOriginal * width / widthOriginal;
         } else if (height > 0) {
            width = widthOriginal * height / heightOriginal;
         } else {
            height = heightOriginal;
            width = widthOriginal;
         }
      }
      
      if (!Util.isEmpty(image.getStyle()))
         writer.writeAttribute("style", image.getStyle(), "style");
      
      String styleClass =  (Util.isEmpty(image.getStyleClass()))?"lazy": "lazy " + image.getStyleClass();
      writer.writeAttribute("class", styleClass, "styleClass");

      if (!Util.isEmpty(image.getAlt()))
         writer.writeAttribute("alt", image.getAlt(), "alt");
      if (!Util.isEmpty(image.getTitle()))
         writer.writeAttribute("title", image.getTitle(), "title");
      if (width > 0)
         writer.writeAttribute("width", width, "width");
      if (height > 0)
         writer.writeAttribute("height", height, "height");
      writer.writeAttribute("data-original", getRequest(context).getContextPath() + image.getUrl(), null);
      writer.endElement("img");
   }

   private int getInt(String value) {
      try {
         return Integer.parseInt(value);
      } catch (NumberFormatException e) {
         return 0;
      }
   }
   private boolean isQuerformat(String value) {
      if (null == value || value.length() == 0) return true;
      try {
         int o = Integer.parseInt(value);
         switch (o) {
            case 0:
            case 1:
            case 2:
               return true;
            case 3:
            case 4:
               return true;
            case 5:
            case 6:
               return false;
            case 7:
            case 8:
               return false;
            default:
               return true;
         }
      } catch (NumberFormatException e) {
         return true;
      }
   }
   
}
