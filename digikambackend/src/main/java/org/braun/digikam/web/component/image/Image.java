/*
 * Copyright 2010 Michael H. Braun
 * 
 */

package org.braun.digikam.web.component.image;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponentBase;

/**
 *
 * @author mbraun
 */
@FacesComponent(Image.COMPONENT_TYPE)
public class Image extends UIComponentBase {
   public static final String COMPONENT_FAMILY = "org.braun.component";
   public static final String COMPONENT_TYPE = "org.braun.component.Image";

   protected enum PropertyKeys {
      style, styleClass, height, width, url, alt, title, library;
   }

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   public Image() {
      super();
      setRendererType(COMPONENT_TYPE);
   }

   public String getStyle() {
      return (String)getStateHelper().eval(PropertyKeys.style);
   }

   public void setStyle(String value) {
		getStateHelper().put(PropertyKeys.style, value);
   }

   public String getStyleClass() {
      return (String)getStateHelper().eval(PropertyKeys.styleClass);
   }

   public void setStyleClass(String value) {
		getStateHelper().put(PropertyKeys.styleClass, value);
   }

   public String getHeight() {
      return (String)getStateHelper().eval(PropertyKeys.height);
   }

   public void setHeight(String value) {
		getStateHelper().put(PropertyKeys.height, value);
   }

   public String getWidth() {
      return (String)getStateHelper().eval(PropertyKeys.width);
   }

   public void setWidth(String value) {
		getStateHelper().put(PropertyKeys.width, value);
   }

   public String getUrl() {
      return (String)getStateHelper().eval(PropertyKeys.url);
   }

   public void setUrl(String value) {
		getStateHelper().put(PropertyKeys.url, value);
   }

   public String getAlt() {
      return (String)getStateHelper().eval(PropertyKeys.alt);
   }

   public void setAlt(String value) {
		getStateHelper().put(PropertyKeys.alt, value);
   }

   public String getTitle() {
      return (String)getStateHelper().eval(PropertyKeys.title);
   }

   public void setTitle(String value) {
		getStateHelper().put(PropertyKeys.title, value);
   }

   public String getLibrary() {
      return (String)getStateHelper().eval(PropertyKeys.library, "/image");
   }

   public void setLibrary(String value) {
		getStateHelper().put(PropertyKeys.library, value);
   }

   @Override
   public String toString() {
      return String.format("style=%s, styleClass=%s, height=%s, width=%s, url=%s, alt=%s, title=%s, library",
              getStyle(), getStyleClass(), getHeight(), getWidth(), getUrl(), getAlt(), getTitle(), getLibrary());
   }

}
