package org.braun.digikam.web.component;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.faces.application.ViewHandler;
import jakarta.faces.component.*;
import jakarta.faces.component.behavior.ClientBehavior;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.render.Renderer;
import jakarta.servlet.http.HttpServletRequest;
import org.braun.digikam.web.util.Util;

/**
 *
 * @author mbraun
 */
public class BaseRenderer extends Renderer {

   protected static final transient Logger logger = Logger.getLogger(BaseRenderer.class.getName());

   protected void renderChildren(FacesContext facesContext, UIComponent component) throws IOException {
      for (Iterator<UIComponent> iterator = component.getChildren().iterator(); iterator.hasNext();) {
         UIComponent child = iterator.next();
         renderChild(facesContext, child);
      }
   }

   protected HttpServletRequest getRequest(FacesContext context) {
      return (HttpServletRequest) context.getExternalContext().getRequest();
   }

   protected void renderChild(FacesContext facesContext, UIComponent child) throws IOException {
      if (!child.isRendered()) {
         return;
      }

      child.encodeBegin(facesContext);

      if (child.getRendersChildren()) {
         child.encodeChildren(facesContext);
      } else {
         renderChildren(facesContext, child);
      }
      child.encodeEnd(facesContext);
   }

   /**
    * checks context and component for null-values.
    * @param context
    * @param component
    */
   protected void rendererParamsNotNull(FacesContext context,
           UIComponent component) {
      Util.notNull("context", context);
      Util.notNull("component", component);
   }

   public Logger getLog() {
      return logger;
   }

   /**
    * @param component the component of interest
    *
    * @return true if this renderer should render an id attribute.
    */
   protected boolean shouldWriteIdAttribute(UIComponent component) {

      // By default we only write the id attribute if:
      //
      // - We have a non-auto-generated id, or...
      // - We have client behaviors.
      //
      // We assume that if client behaviors are present, they
      // may need access to the id (AjaxBehavior certainly does).

      String id;
      return (null != (id = component.getId())
              && (!id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX)
              || ((component instanceof ClientBehaviorHolder)
              && !((ClientBehaviorHolder) component).getClientBehaviors().isEmpty())));
   }

   /**
    * 
    * @param component
    * @return the content of the attribute value of the component
    */
   protected Object getValue(UIComponent component) {

      if (component instanceof ValueHolder) {
         Object value = ((ValueHolder) component).getValue();
         return value;
      }

      return null;

   }

   /**
    * <p>Conditionally augment an id-reference value.</p>
    * <p>If the <code>forValue</code> doesn't already include a generated
    * suffix, but the id of the <code>fromComponent</code> does include a
    * generated suffix, then append the suffix from the
    * <code>fromComponent</code> to the <code>forValue</code>.
    * Otherwise just return the <code>forValue</code> as is.</p>
    *
    * @param forValue      - the basic id-reference value.
    * @param fromComponent - the component that holds the
    *                      code>forValue</code>.
    *
    * @return the (possibly augmented) <code>forValue<code>.
    */
   protected String augmentIdReference(String forValue,
           UIComponent fromComponent) {

      int forSuffix = forValue.lastIndexOf(UIViewRoot.UNIQUE_ID_PREFIX);
      if (forSuffix <= 0) {
         // if the for-value doesn't already have a suffix present
         String id = fromComponent.getId();
         if (id != null) {
            int idSuffix = id.lastIndexOf(UIViewRoot.UNIQUE_ID_PREFIX);
            if (idSuffix > 0) {
               forValue += id.substring(idSuffix);
            }
         }
      }
      return forValue;

   }

   /**
    *
    * @param tag
    * @return the form of the tag included
    */
   protected UIComponent getParentForm(UIComponent tag) {
      UIComponent component = tag.getParent();
      if (component == null) {
         return null;
      }
      if (component instanceof UIForm) {
         return component;
      }
      return getParentForm(component);
   }

   protected String getClientId(String id, UIComponent component, FacesContext context) {
      String clientId = component.getClientId(context);
      if (clientId.endsWith(id)) {
         return clientId;
      }
      for (UIComponent element : component.getChildren()) {
         clientId = getClientId(id, element, context);
         if (null != clientId) {
            return clientId;
         }
      }
      return null;
   }

   protected boolean isEmpty(String value) {
      return null == value || value.length() == 0;
   }

   /**
    * Return the list of children of the specified component.
    * <p>
    * This default implementation simply returns component.getChildren().
    * However this method should always be used in order to allow
    * renderer subclasses to override it and provide filtered or
    * reordered views of the component children to rendering
    * methods defined in their ancestor classes.
    * <p>
    * Any method that overrides this to "hide" child components
    * should also override the getChildCount method.
    *
    * @return a list of UIComponent objects.
    */
   public List<UIComponent> getChildren(UIComponent component) {
      return component.getChildren();
   }

   /**
    * Return the number of children of the specified component.
    * <p>
    * See {@link #getChildren(UIComponent)} for more information.
    */
   public int getChildCount(UIComponent component) {
      return component.getChildCount();
   }

   /**
    * @param facesContext
    * @return String A String representing the action URL
    */
   protected String getActionUrl(FacesContext facesContext) {
      ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
      String viewId = facesContext.getViewRoot().getViewId();
      return viewHandler.getActionURL(facesContext, viewId);
   }


   /**
    * Renders the client ID as an "id".
    */
   protected void renderId(
           FacesContext context,
           UIComponent component) throws IOException {
      if (shouldRenderId(context, component)) {
         String clientId = getClientId(context, component);
         context.getResponseWriter().writeAttribute("id", clientId, "id");
      }
   }

   /**
    * Returns the client ID that should be used for rendering (if
    * {@link #shouldRenderId} returns true).
    */
   protected String getClientId(
           FacesContext context,
           UIComponent component) {
      return component.getClientId(context);
   }

   /**
    * Returns true if the component should render an ID.  Components
    * that deliver events should always return "true".
    */
   protected boolean shouldRenderId(
           FacesContext context,
           UIComponent component) {
      String id = component.getId();

      // Otherwise, if ID isn't set, don't bother
      if (id == null) {
         return false;
      }

      // ... or if the ID was generated, don't bother
      if (id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX)) {
         return false;
      }

      return true;
   }

   /**
    * Coerces an object into a URI, accounting for JSF rules
    * with initial slashes.
    */
   static public String toUri(Object o) {
      if (o == null) {
         return null;
      }

      String uri = o.toString();
      if (uri.startsWith("/")) {
         // Treat two slashes as server-relative
         if (uri.startsWith("//")) {
            uri = uri.substring(1);
         } else {
            FacesContext fContext = FacesContext.getCurrentInstance();
            uri = fContext.getExternalContext().getRequestContextPath() + uri;
         }
      }

      return uri;
   }

   /**
    * Overloads getFormattedValue to take a advantage of a previously
    * obtained converter.
    * @param context the FacesContext for the current request
    * @param component UIComponent of interest
    * @param currentValue the current value of <code>component</code>
    * @param converter the component's converter
    * @return the currentValue after any associated Converter has been
    *  applied
    *
    * @throws ConverterException if the value cannot be converted
    */
   protected String getFormattedValue(FacesContext context,
           UIComponent component,
           Object currentValue,
           Converter converter)
           throws ConverterException {

      // formatting is supported only for components that support
      // converting value attributes.
      if (!(component instanceof ValueHolder)) {
         if (currentValue != null) {
            return currentValue.toString();
         }
         return null;
      }

      if (converter == null) {
         // If there is a converter attribute, use it to to ask application
         // instance for a converter with this identifer.
         converter = ((ValueHolder) component).getConverter();
      }

      if (converter == null) {
         // if value is null and no converter attribute is specified, then
         // return a zero length String.
         if (currentValue == null) {
            return "";
         }
         // Do not look for "by-type" converters for Strings
         if (currentValue instanceof String) {
            return (String) currentValue;
         }

         // if converter attribute set, try to acquire a converter
         // using its class type.

         Class converterType = currentValue.getClass();
         converter = Util.getConverterForClass(converterType, context);

         // if there is no default converter available for this identifier,
         // assume the model type to be String.
         if (converter == null) {
            return currentValue.toString();
         }
      }

      return converter.getAsString(context, component, currentValue);
   }

   /**
    * When rendering pass thru attributes, we need to take any
    * attached Behaviors into account.  The presence of a non-empty
    * Behaviors map can cause us to switch from optimized pass thru
    * attribute rendering to the unoptimized code path.  However,
    * in two very common cases - attaching action behaviors to
    * commands and attaching value change behaviors to editable value
    * holders - the behaviors map is populated with behaviors that
    * are not handled by the pass thru attribute code - ie. the
    * behaviors are handled locally by the renderer.
    *
    * In order to optimize such cases, we check to see whether the
    * component's behaviors map actually contains behaviors only
    * for these non-pass thru attributes.  If so, we can pass a
    * null behavior map into renderPassThruAttributes(), thus ensuring
    * that we can take advantage of the optimized pass thru rendering
    * logic.
    *
    * Note that in all cases where we use this method, we actually have
    * two behavior events that we want to check for - a low-level/dom
    * event (eg. "click", or "change") plus a high-level component
    * event (eg. "action", or "valueChange").
    *
    * @param component the component that we are rendering
    * @param domEventName the name of the dom-level event
    * @param componentEventName the name of the component-level event
    */
   protected static Map<String, List<ClientBehavior>> getPassThruBehaviors(
           UIComponent component,
           String domEventName,
           String componentEventName) {

      if (!(component instanceof ClientBehaviorHolder)) {
         return null;
      }

      Map<String, List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) component).getClientBehaviors();

      int size = behaviors.size();

      if ((size == 1) || (size == 2)) {
         boolean hasDomBehavior = behaviors.containsKey(domEventName);
         boolean hasComponentBehavior = behaviors.containsKey(componentEventName);

         // If the behavior map only contains behaviors for non-pass
         // thru attributes, return null.
         if (((size == 1) && (hasDomBehavior || hasComponentBehavior))
                 || ((size == 2) && hasDomBehavior && hasComponentBehavior)) {
            return null;
         }
      }

      return behaviors;
   }

   /**
    * @param ctx the <code>FacesContext</code> for the current request
    * @param behaviorSourceId the ID of the behavior source
    * @param componentClientId the client ID of the component being decoded
    * @return <code>true</code> if the behavior source is for the component
    *  being decoded, otherwise <code>false</code>
    */
   protected boolean isBehaviorSource(FacesContext ctx,
           String behaviorSourceId,
           String componentClientId) {

      return (behaviorSourceId != null && behaviorSourceId.equals(componentClientId));

   }

   @Override
    public void decode(FacesContext context, UIComponent component) {

        rendererParamsNotNull(context, component);

        if (!shouldDecode(component)) {
            return;
        }

        String clientId = decodeBehaviors(context, component);

        if (!(component instanceof UIInput)) {
            // decode needs to be invoked only for components that are
            // instances or subclasses of UIInput.
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE,
                           "No decoding necessary since the component {0} is not an instance or a sub class of UIInput",
                           component.getId());
            }
            return;
        }

        if (clientId == null) {
            clientId = component.getClientId(context);
        }

        assert(clientId != null);
        Map<String, String> requestMap =
              context.getExternalContext().getRequestParameterMap();
        // Don't overwrite the value unless you have to!
        String newValue = requestMap.get(clientId);
        if (newValue != null) {
            setSubmittedValue(component, newValue);
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE,
                           "new value after decoding {0}",
                           newValue);
            }
        }

    }

    // Decodes Behaviors if any match the behavior source/event.
    // As a convenience, returns component id, but only if it
    // was retrieved.  This allows us to avoid duplicating
    // calls to getClientId(), which can be expensive for
    // deep component trees.
    protected final String decodeBehaviors(FacesContext context,
                                           UIComponent component)  {

        if (!(component instanceof ClientBehaviorHolder)) {
            return null;
        }

        ClientBehaviorHolder holder = (ClientBehaviorHolder)component;
        Map<String, List<ClientBehavior>> behaviors = holder.getClientBehaviors();
        if (behaviors.isEmpty()) {
            return null;
        }

        ExternalContext external = context.getExternalContext();
        Map<String, String> params = external.getRequestParameterMap();
        String behaviorEvent = params.get("javax.faces.behavior.event");

        if (null != behaviorEvent) {
            List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);

            if (behaviors.size() > 0) {
               String behaviorSource = params.get("javax.faces.source");
               String clientId = component.getClientId();
               if (isBehaviorSource(context, behaviorSource, clientId)) {
                   for (ClientBehavior behavior: behaviorsForEvent) {
                       behavior.decode(context, component);
                   }
               }

               return clientId;
            }
        }

        return null;
    }

    /**
     * Renderers override this method to store the previous value
     * of the associated component.
     *
     * @param component the target component to which the submitted value
     *  will be set
     * @param value the value to set
     */
    protected void setSubmittedValue(UIComponent component, Object value) {

        // no-op unless overridden

    }


   protected boolean shouldEncode(UIComponent component) {

      // suppress rendering if "rendered" property on the component is
      // false.
      if (!component.isRendered()) {
         return false;
      }
      return true;

   }

    protected boolean shouldDecode(UIComponent component) {

        if (Util.componentIsDisabledOrReadonly(component)) {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE,
                           "No decoding necessary since the component {0} is disabled or read-only",
                           component.getId());
            }
            return false;
        }
        return true;

    }

    protected boolean shouldEncodeChildren(UIComponent component) {

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE,
                            "Children of component {0} will not be encoded since this component's rendered attribute is false",
                            component.getId());
            }
            return false;
        }
        return true;

    }

   /**
    * Structure to hold common info used by Select* components
    * to reduce the number of times component attributes are evaluated
    * when rendering options.
    */
   public static class OptionComponentInfo {

      String disabledClass;
      String enabledClass;
      String selectedClass;
      String unselectedClass;
      boolean disabled;
      boolean hideNoSelection;

      public OptionComponentInfo(String disabledClass,
              String enabledClass,
              boolean disabled,
              boolean hideNoSelection) {

         this(disabledClass, enabledClass, null, null, disabled, hideNoSelection);

      }

      public OptionComponentInfo(String disabledClass,
              String enabledClass,
              String unselectedClass,
              String selectedClass,
              boolean disabled,
              boolean hideNoSelection) {

         this.disabledClass = disabledClass;
         this.enabledClass = enabledClass;
         this.unselectedClass = unselectedClass;
         this.selectedClass = selectedClass;
         this.disabled = disabled;
         this.hideNoSelection = hideNoSelection;

      }

      public String getDisabledClass() {
         return disabledClass;
      }

      public String getEnabledClass() {
         return enabledClass;
      }

      public boolean isDisabled() {
         return disabled;
      }

      public boolean isHideNoSelection() {
         return hideNoSelection;
      }

      public String getSelectedClass() {
         return selectedClass;
      }

      public String getUnselectedClass() {
         return unselectedClass;
      }
   }
}
