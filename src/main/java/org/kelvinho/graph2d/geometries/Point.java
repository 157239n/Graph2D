package org.kelvinho.graph2d.geometries;

import processing.core.PVector;

import javax.annotation.Nonnull;

public class Point extends Geometry {
    private double x, y, z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(double x, double y) {
        this(x, y, 0);
    }

    public Point(float x, float y, float z) {
        this((double) x, (double) y, (double) z);
    }

    public Point(float x, float y) {
        this((double) x, (double) y);
    }

    public Point(@Nonnull PVector pVector) {
        this.x = pVector.x;
        this.y = pVector.y;
        this.z = pVector.z;
    }

    public double getX(){
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}