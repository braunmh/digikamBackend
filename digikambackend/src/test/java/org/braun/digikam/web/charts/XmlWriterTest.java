package org.braun.digikam.web.charts;

import org.braun.digikam.web.component.diagram.DiagramModel;
import org.braun.digikam.web.component.diagram.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 *
 * @author mbraun
 */
public class XmlWriterTest {

    @Test
    public void testExposureTimeDiagram() {
        StringWriter out = new StringWriter();
        DiagramModel<Double> etd = newExposureTimeDiagram();
        try (Writer fileWriter = new FileWriter("/data/exchange/temp/etd.xml")) {
            etd.render(out);
            fileWriter.write(out.toString());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        System.out.println(out.toString());
    }

    @Test
    public void testIsoDiagram() {

        StringWriter out = new StringWriter();
        DiagramModel<Double> id = newIsoDiagram();
        try (Writer fileWriter = new FileWriter("/data/exchange/temp/id.xml")) {
            id.render(out);
            fileWriter.write(out.toString());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        System.out.println(out.toString());
    }

    @Test
    public void testFocalLengthDiagram() {

        StringWriter out = new StringWriter();
        DiagramModel<Double> fld = newFocalLengthDiagram();
        try (Writer fileWriter = new FileWriter("/data/exchange/temp/fld.xml")) {
            fld.render(out);
            fileWriter.write(out.toString());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        System.out.println(out.toString());
    }

    @Test
    public void testApertureDiagram() {

        StringWriter out = new StringWriter();
        DiagramModel<Double> ad = newApertureDiagram();
        try (Writer fileWriter = new FileWriter("/data/exchange/temp/ad.xml")) {
            ad.render(out);
            fileWriter.write(out.toString());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        System.out.println(out.toString());
    }

    private DiagramModel<Double> newFocalLengthDiagram() {
        return new DiagramModel.Builder<Double>()
                .height(1000)
                .width(1500)
                .points(getPointsFocalLength35())
                .axisXDescription("Brennweite")
                .axisXUnit("mm")
                .axisXFormat(n -> String.valueOf(n.longValue()))
                .build();
    }

    private DiagramModel<Double> newIsoDiagram() {
        return new DiagramModel.Builder<Double>()
                .height(1000)
                .width(1500)
                .points(getPointsIso())
                .axisXDescription("ISO")
                .axisXUnit("ISO")
                .axisXFormat(n -> {
                    Double t = n * n;
                    return String.valueOf(t.longValue());
                })
                .build();
    }

    private DiagramModel<Double> newApertureDiagram() {
        return new DiagramModel.Builder<Double>()
                .height(1000)
                .width(1500)
                .points(getPointsAperture())
                .axisXDescription("f")
                .axisXUnit("Blende")
                .axisXFormat(n -> {
                    Double t = n * n;
                    return String.valueOf(t.longValue());
                })
                .build();
    }

    private DiagramModel<Double> newExposureTimeDiagram() {
        return new DiagramModel.Builder<Double>()
                .height(1000)
                .width(1500)
                .points(getPointsExposureTime())
                .axisXDescription("Belichtungszeit")
                .axisXUnit("s")
                .axisXFormat(d -> {
                    if (d == null || d == 0) {
                        return "";
                    }
                    d = Math.pow(2, d);
                    if (d > 1) {
                        return String.format("%.1f", d);
                    }
                    return "1/" + Math.round(1 / d);
                })
                .build();
    }

    private List<Point.Count<Double>> getPointsFocalLength35() {
        return Arrays.asList(
                new Point.Count<>(35.0, 144),
                new Point.Count<>(36.0, 19),
                new Point.Count<>(37.0, 12),
                new Point.Count<>(38.0, 6),
                new Point.Count<>(39.0, 12),
                new Point.Count<>(40.0, 8),
                new Point.Count<>(41.0, 10),
                new Point.Count<>(42.0, 9),
                new Point.Count<>(43.0, 8),
                new Point.Count<>(44.0, 8),
                new Point.Count<>(45.0, 14),
                new Point.Count<>(46.0, 14),
                new Point.Count<>(47.0, 12),
                new Point.Count<>(48.0, 12),
                new Point.Count<>(49.0, 8),
                new Point.Count<>(50.0, 9),
                new Point.Count<>(51.0, 8),
                new Point.Count<>(52.0, 8),
                new Point.Count<>(53.0, 2),
                new Point.Count<>(54.0, 7),
                new Point.Count<>(55.0, 12),
                new Point.Count<>(56.0, 7),
                new Point.Count<>(57.0, 8),
                new Point.Count<>(58.0, 6),
                new Point.Count<>(59.0, 9),
                new Point.Count<>(60.0, 13),
                new Point.Count<>(61.0, 7),
                new Point.Count<>(62.0, 11),
                new Point.Count<>(63.0, 14),
                new Point.Count<>(64.0, 11),
                new Point.Count<>(65.0, 13),
                new Point.Count<>(66.0, 13),
                new Point.Count<>(67.0, 6),
                new Point.Count<>(68.0, 12),
                new Point.Count<>(69.0, 10),
                new Point.Count<>(70.0, 13),
                new Point.Count<>(71.0, 2),
                new Point.Count<>(72.0, 4),
                new Point.Count<>(73.0, 5),
                new Point.Count<>(74.0, 5),
                new Point.Count<>(75.0, 6),
                new Point.Count<>(76.0, 11),
                new Point.Count<>(77.0, 6),
                new Point.Count<>(78.0, 3),
                new Point.Count<>(79.0, 15),
                new Point.Count<>(80.0, 6),
                new Point.Count<>(81.0, 9),
                new Point.Count<>(82.0, 8),
                new Point.Count<>(83.0, 16),
                new Point.Count<>(84.0, 5),
                new Point.Count<>(85.0, 6),
                new Point.Count<>(86.0, 14),
                new Point.Count<>(87.0, 8),
                new Point.Count<>(88.0, 9),
                new Point.Count<>(89.0, 14),
                new Point.Count<>(90.0, 5),
                new Point.Count<>(91.0, 5),
                new Point.Count<>(92.0, 22),
                new Point.Count<>(93.0, 5),
                new Point.Count<>(94.0, 7),
                new Point.Count<>(95.0, 4),
                new Point.Count<>(96.0, 2),
                new Point.Count<>(98.0, 4),
                new Point.Count<>(99.0, 13),
                new Point.Count<>(100.0, 4),
                new Point.Count<>(101.0, 4),
                new Point.Count<>(102.0, 4),
                new Point.Count<>(103.0, 5),
                new Point.Count<>(105.0, 8),
                new Point.Count<>(106.0, 1),
                new Point.Count<>(107.0, 13),
                new Point.Count<>(110.0, 9),
                new Point.Count<>(113.0, 12),
                new Point.Count<>(116.0, 8),
                new Point.Count<>(119.0, 13),
                new Point.Count<>(123.0, 8),
                new Point.Count<>(126.0, 10),
                new Point.Count<>(129.0, 12),
                new Point.Count<>(132.0, 5),
                new Point.Count<>(135.0, 10),
                new Point.Count<>(139.0, 3),
                new Point.Count<>(143.0, 5),
                new Point.Count<>(150.0, 158)
        );
    }

    private List<Point.Count<Double>> getPointsAperture() {
        return Arrays.asList(
                new Point.CountIso(2.0, 4),
                new Point.CountIso(2.2, 22),
                new Point.CountIso(2.5, 24),
                new Point.CountIso(2.8, 30),
                new Point.CountIso(3.2, 4),
                new Point.CountIso(3.5, 4),
                new Point.CountIso(4.0, 101),
                new Point.CountIso(4.5, 73),
                new Point.CountIso(5.0, 13),
                new Point.CountIso(5.6, 323),
                new Point.CountIso(6.3, 89),
                new Point.CountIso(7.1, 233),
                new Point.CountIso(8.0, 59),
                new Point.CountIso(9.0, 9),
                new Point.CountIso(10.0, 4),
                new Point.CountIso(11.0, 1),
                new Point.CountIso(13.0, 12),
                new Point.CountIso(14.0, 1)
        );
    }

    private List<Point.Count<Double>> getPointsIso() {
        return Arrays.asList(
                new Point.CountIso(100.0, 43),
                new Point.CountIso(125.0, 9),
                new Point.CountIso(160.0, 6),
                new Point.CountIso(200.0, 9),
                new Point.CountIso(250.0, 2),
                new Point.CountIso(320.0, 3),
                new Point.CountIso(400.0, 6),
                new Point.CountIso(500.0, 5),
                new Point.CountIso(640.0, 10),
                new Point.CountIso(800.0, 5),
                new Point.CountIso(1000.0, 4),
                new Point.CountIso(1250.0, 6),
                new Point.CountIso(1600.0, 6),
                new Point.CountIso(2000.0, 6),
                new Point.CountIso(2500.0, 7),
                new Point.CountIso(3200.0, 6),
                new Point.CountIso(4000.0, 6),
                new Point.CountIso(5000.0, 5),
                new Point.CountIso(6400.0, 18)
        );
    }

    private List<Point.Count<Double>> getPointsExposureTime() {
        return Arrays.asList(
                new Point.CountExposureTime(4.0, 1),
                new Point.CountExposureTime(3.2, 1),
                new Point.CountExposureTime(0.25, 3),
                new Point.CountExposureTime(0.166666666666667, 11),
                new Point.CountExposureTime(0.125, 3),
                new Point.CountExposureTime(0.1, 6),
                new Point.CountExposureTime(0.0666666666666667, 1),
                new Point.CountExposureTime(0.04, 2),
                new Point.CountExposureTime(0.0333333333333333, 4),
                new Point.CountExposureTime(0.025, 102),
                new Point.CountExposureTime(0.02, 49),
                new Point.CountExposureTime(0.0166666666666667, 46),
                new Point.CountExposureTime(0.0125, 89),
                new Point.CountExposureTime(0.01, 118),
                new Point.CountExposureTime(0.008, 91),
                new Point.CountExposureTime(0.00625, 227),
                new Point.CountExposureTime(0.005, 56),
                new Point.CountExposureTime(0.004, 56),
                new Point.CountExposureTime(0.003125, 34),
                new Point.CountExposureTime(0.0025, 30),
                new Point.CountExposureTime(0.002, 20),
                new Point.CountExposureTime(0.0015625, 13),
                new Point.CountExposureTime(0.00125, 10),
                new Point.CountExposureTime(0.001, 13),
                new Point.CountExposureTime(0.0008, 5),
                new Point.CountExposureTime(0.000625, 2),
                new Point.CountExposureTime(0.0005, 4),
                new Point.CountExposureTime(0.0004, 3),
                new Point.CountExposureTime(0.0003125, 2),
                new Point.CountExposureTime(0.00025, 1),
                new Point.CountExposureTime(0.0002, 1),
                new Point.CountExposureTime(0.000125, 2)
        );

    }
}
