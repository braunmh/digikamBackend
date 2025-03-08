package org.braun.digikam.web.component.diagram;

import java.io.Serializable;
import java.util.function.Function;

/**
 *
 * @author mbraun
 * @param <N> Number
 */
public class Axis<N extends Number> implements Serializable {

    private final String unit;

    private final N from;

    private final N to;

    private final String description;

    private final int points;

    private final Function<Double, String> format;

    public String format(Double value) {
        return format.apply(value);
    }

    public Axis(String unit, N from, N to, String description, int points, Function<Double, String> format) {
        this.unit = unit;
        this.from = from;
        this.to = to;
        this.description = description;
        this.points = points;
        this.format = format;
    }

    public String getUnit() {
        return unit;
    }

    public N getFrom() {
        return from;
    }

    public N getTo() {
        return to;
    }

    public String getDescription() {
        return description;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "Axis{" + "unit=" + unit + ", from=" + from + ", to=" + to + ", description=" + description + ", points=" + points + '}';
    }

    public static class Builder<N extends Number> {

        private String unit;

        private N from;

        private N to;

        private String description;

        private int points = 10;

        private Function<Double, String> format;

        public Builder<N> unit(String value) {
            unit = value;
            return this;
        }
        
        public Builder<N> from(N value) {
            from = value;
            return this;
        }
        
        public Builder<N> to(N value) {
            to = value;
            return this;
        }
        
        public Builder<N> description(String value) {
            description = value;
            return this;
        }
        
        public Builder<N> points(int value) {
            points = value;
            return this;
        }
        
        public Axis<N> build() {
            return new Axis<>(unit, from, to, description, points, format);
        }
    }
    
    public static Axis<Integer> newCount(int to) {
        return new Axis.Builder<Integer>()
                .description("Anzahl")
                .from(0)
                .to(to)
                .build();
    }
}
