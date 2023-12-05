package org.braun.digikam.backend.graphics;
/**
 *
 * @author mbraun
 */
public enum Orientation {

   angle0(0), angle90(90), angle180(180), angle270(270);

   private int angle;
   
   private Orientation(int angle) {
      this.angle = angle;
   }

   public int getValue() {
      return angle;
   }
   
   public static Orientation parse(int value) {
      switch (value) {
         case 90: return angle90;
         case 180: return angle180;
         case 270: return angle270;
         default: return angle0;
      }
   }
   
   public static Orientation parse(String value) {
      if (null == value || value.length() == 0) return angle0;
      try {
         int o = Integer.parseInt(value);
         switch (o) {
            case 0:
            case 1:
            case 2:
               return angle0;
            case 3:
            case 4:
               return angle180;
            case 5:
            case 6:
               return angle90;
            case 7:
            case 8:
               return angle270;
            default:
               return angle0;
         }
      } catch (NumberFormatException e) {
         return angle0;
      }
   }
}
