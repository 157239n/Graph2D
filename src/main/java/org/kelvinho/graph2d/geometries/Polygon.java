package org.kelvinho.graph2d.geometries;

import java.util.ArrayList;

public class Polygon extends Geometry {
    private ArrayList<Point> points;

    public Polygon(ArrayList<Point> points) {
        this.points = points;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }
}
