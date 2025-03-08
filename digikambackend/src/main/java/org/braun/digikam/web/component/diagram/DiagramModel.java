package org.braun.digikam.web.component.diagram;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.braun.digikam.web.component.common.XmlWriter;

/**
 *
 * @author mbraun
 * @param <N>
 */
public class DiagramModel<N extends Number & Comparable<N>> implements Serializable {

    private int height;
    private int width;
    private final Axis<Integer> axisY;
    private final Axis<N> axisX;
    private final List<Point.Count<N>> points;
    private XmlWriter out;
    private final Function<Double, String> xFormatter;

    private DiagramModel(int height, int width, Axis<N> axisX, Axis<Integer> axisY,
            Function<Double, String> xFormatter, List<Point.Count<N>> points) {
        this.height = height;
        this.width = width;
        this.axisX = axisX;
        this.axisY = axisY;
        this.points = points;
        this.xFormatter = xFormatter;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Axis<Integer> getAxisY() {
        return axisY;
    }

    public Axis<N> getAxisX() {
        return axisX;
    }

    public List<Point.Count<N>> getPoints() {
        return points;
    }

    protected String xFormat(Double d) {
        return xFormatter.apply(d);
    }
    
    public void render(Writer writer) throws IOException {
        out = new XmlWriter(writer);
        out.startElement("svg");
        out.writeAttribute("xmlns", "http://www.w3.org/2000/svg");
        out.writeAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        out.writeAttribute("class", "graph");
        out.writeAttribute("viewBox", "0 0 " + width + " " + height);
        int left = (height / 10);
        int button = left;
        int right = height / 20;
        int top = right;
        out.startElement("rect");
        out.writeAttribute("width", width - left - right);
        out.writeAttribute("height", height - button - top);
        out.writeAttribute("x", left);
        out.writeAttribute("y", top);
        out.writeAttribute("style", "fill: rgb(255, 255, 255); stroke-width: 1; stroke: #999");
        out.endElement("rect");
        renderAxisX(left, top, right, button);
        renderAxisY(left, top, right, button);
        List<Point<Double, Double>> transformedPoints = transform(left, top, right, button);
        renderGraph(transformedPoints);
        
        out.endElement("svg");
    }

    private void renderGraph(List<Point<Double, Double>> transformedPoints) throws IOException {
        out.startElement("polyline");
        out.writeAttribute("fill", "none");
        out.writeAttribute("stroke", "#666");
        out.writeAttribute("stroke-width", "1");
        List<String> temp = transformedPoints
                .stream()
                .map(p -> (p.getX()) + "," + String.valueOf(p.getY()))
                .collect(Collectors.toList());
        out.writeAttribute("points", String.join(" ", temp));
        out.endElement("polyline");
        int i = 0;
        for (Point<Double, Double> point : transformedPoints) {
            out.startElement("circle");
            out.writeAttribute("cx", String.valueOf(point.getX()));
            out.writeAttribute("cy", String.valueOf(point.getY()));
            out.writeAttribute("r", "5");
            out.writeAttribute("fill", "#eee");
            out.writeAttribute("stroke", "#666");
            out.writeAttribute("stroke-width", "1");
            out.startElement("title");
            out.characters(String.valueOf(points.get(i).getY()) + ", " + xFormat(points.get(i).getNormalizedX().doubleValue()) + " " + axisX.getUnit());
            out.endElement("title");
            out.endElement("circle");
            i++;
        }
    }
    
    private void renderAxisX(int left, int top, int right, int button) throws IOException {
        double yBase = height - button;
        double stepText = (axisX.getTo().doubleValue() - axisX.getFrom().doubleValue()) / axisX.getPoints();
        Double currentText = axisX.getFrom().doubleValue();
        double currentX = left;
        int stepX = (width - left - right) / axisX.getPoints();

        for (int i = 0; i <= axisX.getPoints(); i++) {
            if (i > 0) {
                out.startElement("line");
                out.writeAttribute("y1", top);
                out.writeAttribute("y2", yBase);
                out.writeAttribute("x1", currentX);
                out.writeAttribute("x2", currentX);
                out.writeAttribute("style", "stroke: #ccc; stroke-with: 1");
                out.endElement("line");
            }
            textAlignMiddle(currentX, yBase + button * 0.25, xFormat(currentText));
            currentText = currentText + stepText;
            currentX = currentX + stepX;
        }
        double y = yBase + button / 2;
        if (axisX.getDescription() != null) {
            int x = (width - left - right) / 2 + left;
            textAlignMiddle(x, y, axisX.getDescription());
        }
        if (axisX.getUnit() != null) {
            int x = width - right;
            textAlignRight(x, y, axisX.getUnit());
        }
        
        List<Integer> temp = points.stream().map(p -> p.getY()).toList();
        int totalCount = temp.stream().mapToInt(Integer::intValue).sum();
        textAlignMiddle((width - left - right) / 2 + left, top / 2, "Anzahl Messpunkte: " + totalCount);
    }
    
    private void renderAxisY(int left, int top, int right, int button) throws IOException {
        int axisHeight = height - button - top;
        int stepY = axisHeight / axisY.getPoints();
        int xBase = left - left / 10;
        int stepText = axisY.getTo() / axisY.getPoints();
        int currentText = axisY.getTo();
        int currentY = top;
        for (int i = 0; i < axisY.getPoints(); i++) {
            if (i > 0) {
                out.startElement("line");
                out.writeAttribute("x1", left);
                out.writeAttribute("x2", width - right);
                out.writeAttribute("y1", currentY);
                out.writeAttribute("y2", currentY);
                out.writeAttribute("style", "stroke: #ccc; stroke-with: 1");
                out.endElement("line");
            }
            textAlignRight(xBase, currentY + 10, String.valueOf(currentText));
            currentText = currentText - stepText;
            currentY = currentY + stepY;
        }
    }

    private void textAlignRight(double x, double y, String text) throws IOException {
        out.startElement("text");
        out.writeAttribute("text-anchor", "end");
        out.startElement("tspan");
        out.writeAttribute("y", y);
        out.writeAttribute("x", x);
        out.characters(text);
        out.endElement("tspan");
        out.endElement("text");
    }

    private void textAlignMiddle(double x, double y, String text) throws IOException {
        out.startElement("text");
        out.writeAttribute("text-anchor", "middle");
        out.startElement("tspan");
        out.writeAttribute("y", y);
        out.writeAttribute("x", x);
        out.characters(text);
        out.endElement("tspan");
        out.endElement("text");
    }

    private List<Point<Double, Double>> transform(int left, int top, int right, int button) {
        List<Point<Double, Double>> res = new ArrayList<>(points.size());
        double innerWidth = width - left - right;
        double stepWidth = innerWidth / (axisX.getTo().doubleValue() - axisX.getFrom().doubleValue());
        
        double innerHight = height - top - button;
        double stepHight = innerHight / (axisY.getTo().doubleValue() - axisY.getFrom().doubleValue());
        
        double indentLeft = left - stepWidth * axisX.getFrom().doubleValue();
        
        for (Point.Count point : points) {
            res.add(new Point<>(
                    stepWidth * point.getNormalizedX().doubleValue() + indentLeft, 
                    innerHight - stepHight * point.getY() + top));
        }
        return res;
    }
    
    public static class Builder<N extends Number & Comparable<N>> {

        private int height;
        private int width;
        private Axis<Integer> axisY;
        private List<Point.Count<N>> points;
        private String axisXUnit;
        private String axisXDescription;
        private Function<Double, String> axisXFormat;
        private int axisXPoints = 10;

        public Builder<N> height(int value) {
            height = value;
            return this;
        }

        public Builder<N> width(int value) {
            width = value;
            return this;
        }

        public Builder<N> axisXUnit(String value) {
            axisXUnit = value;
            return this;
        }

        public Builder<N> axisXDescription(String value) {
            axisXDescription = value;
            return this;
        }

        public Builder<N> axisXPoints(int value) {
            axisXPoints = value;
            return this;
        }

        public Builder<N> axisXFormat(Function<Double, String> value) {
            axisXFormat = value;
            return this;
        }

        public Builder<N> points(List<Point.Count<N>> value) {
            points = value;
            return this;
        }

        public Builder<N> maxCount(int value) {
            axisY = Axis.newCount(value);
            return this;
        }

        public DiagramModel<N> build() {
            if (axisY == null) {
                axisY = Axis.newCount(nextUpperLimit(maxY(points)));
            }
            if (axisXFormat == null) {
                axisXFormat = p -> String.valueOf(p);
            }
            if (height == 0) {
                height = 2 / 3 * width;
            }
            Axis<N> axisX = new Axis.Builder<N>()
                    .description(axisXDescription)
                    .unit(axisXUnit)
                    .from(minX(points))
                    .to(maxX(points))
                    .points(axisXPoints)
                    .build();
            return new DiagramModel<>(height, width, axisX, axisY, axisXFormat,
                    points.stream()
                        .sorted((p1, p2) -> 
                            p1.getNormalizedX().compareTo(p2.getNormalizedX()))
                        .toList());
        }

        private int maxY(List<Point.Count<N>> points) {
            Optional<Integer> res = points.stream().map(p -> p.getY()).max(Comparator.naturalOrder());
            if (res.isPresent()) {
                return res.get();
            } else {
                return 0;
            }
        }

        private N minX(List<Point.Count<N>> points) {
            Optional<N> res = points.stream().map(p -> p.getNormalizedX()).min(Comparator.naturalOrder());
            if (res.isPresent()) {
                return res.get();
            } else {
                return null;
            }
        }

        private N maxX(List<Point.Count<N>> points) {
            Optional<N> res = points.stream().map(p -> p.getNormalizedX()).max(Comparator.naturalOrder());
            if (res.isPresent()) {
                return res.get();
            } else {
                return null;
            }
        }

        private int nextUpperLimit(int v) {
            int i = 1;
            while (true) {
                if (v < 10) {
                    return (v + 1) * i;
                } else {
                    v = v / 10;
                }
                i = i * 10;
            }
        }
    }
}
