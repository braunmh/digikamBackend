package org.braun.digikam.web.util;

import jakarta.faces.application.Application;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;

/**
 *
 * @author mbraun
 */
public class Util {

   public static ClassLoader getCurrentLoader(Object fallbackClass) {
      ClassLoader loader =
              Thread.currentThread().getContextClassLoader();
      if (loader == null) {
         loader = fallbackClass.getClass().getClassLoader();
      }
      return loader;
   }

   public static void notNull(String varname, Object var) {

      if (var == null) {
         throw new NullPointerException(String.format("Parameter [%s] can not be null", varname));
      }

   }

   public static boolean componentIsDisabled(UIComponent component) {

      return (Boolean.valueOf(String.valueOf(component.getAttributes().get("disabled"))));

   }

   public static boolean isEmpty(String value) {
      return null == value || value.trim().length() == 0;
   }

   public static Converter getConverterForClass(Class converterClass,
           FacesContext context) {
      if (converterClass == null) {
         return null;
      }
      try {
         Application application = context.getApplication();
         return (application.createConverter(converterClass));
      } catch (Exception e) {
         return (null);
      }
   }

   /**
    * <p>
    * Adds the specified key and value to the Map stored in the request.
    * If <code>value</code> is <code>null</code>, that key/value pair will
    * be removed from the Map.
    * </p>
    *
    * @param ctx the <code>FacesContext</code> for the current request
    * @param key the key for the value
    * @param value the value to store
    */
   public static void set(FacesContext ctx, String key, Object value) {

      if (ctx == null || key == null) {
         return;
      }
      if (value == null) {
         remove(ctx, key);
      }
      ctx.getAttributes().put(key, value);

   }

   /**
    * <p>
    * Remove the value associated with the specified key.
    * </p>
    *
    * @param ctx the <code>FacesContext</code> for the current request
    * @param key the key for the value
    * @return the value previous associated with the specified key, if any
    */
   public static Object remove(FacesContext ctx, String key) {

      if (ctx == null || key == null) {
         return null;
      }

      return ctx.getAttributes().remove(key);

   }
   
   public static boolean componentIsDisabledOrReadonly(UIComponent component) {
      return Boolean.valueOf(String.valueOf(component.getAttributes().get("disabled"))) || Boolean.valueOf(String.valueOf(component.getAttributes().get("readonly")));
   }
}
