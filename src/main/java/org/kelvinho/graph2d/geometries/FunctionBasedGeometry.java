package org.kelvinho.graph2d.geometries;

import javax.annotation.Nonnull;

public class FunctionBasedGeometry extends Geometry {
    public interface Function {
        double getValue(@Nonnull double... params);
    }

    private Function function;

    public FunctionBasedGeometry(@Nonnull Function function) {
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }
}