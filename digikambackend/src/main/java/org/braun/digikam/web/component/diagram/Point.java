package org.braun.digikam.web.component.diagram;

import java.io.Serializable;

/**
 *
 * @author mbraun
 * @param <X> Numberic-type of x-axis
 * @param <Y> Numberic-type of y-axis
 */
public class Point<X extends Number, Y extends Number> implements Serializable {

    private final X x;
    private final Y y;
    private X normalizedX;

    public Point(X x, Y y) {
        this.x = x;
        this.y = y;
        normalizedX = normalizedX(x);
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }

    public String formatX(X x) {
        return String.valueOf(x);
    }

    protected X normalizedX(X x) {
        return x;
    }

    public final X getNormalizedX() {
        return normalizedX;
    }

    public static class Count<X extends Number> extends Point<X, Integer> {

        public Count(X x, Integer y) {
            super(x, y);
        }

        @Override
        public Integer getY() {
            return super.getY();
        }
    }

    public static class CountExposureTime extends Count<Double> {

        public CountExposureTime(Double x, Integer y) {
            super(x, y);
        }

        @Override
        protected Double normalizedX(Double x) {
            return Math.log(x) / Math.log(2.0);
        }

    }
    
    public static class CountIso extends Count<Double> {
        
        public CountIso(Double x, Integer y) {
            super(x, y);
        }

        @Override
        protected Double normalizedX(Double x) {
            if (x == null || x == 0.0) {
                return 0.0;
            }
            return Math.sqrt(x);
        }
        
    }
}
