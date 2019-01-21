package org.kelvinho.graph2d.geometries;

public class Geometry {
    public static final int DEFAULT_COLOR = 0;

    private int color = DEFAULT_COLOR;

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
