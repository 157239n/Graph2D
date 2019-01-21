package org.kelvinho.graph2d.geometries;

import javax.annotation.Nonnull;

public class Segment extends Geometry {
    private Point point1, point2;
    public Segment(@Nonnull Point point1, @Nonnull Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }
    public Segment(double x1, double y1, double x2, double y2) {
        point1 = new Point(x1, y1);
        point2 = new Point(x2, y2);
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }
}