package org.kelvinho.graph2d;

import org.kelvinho.graph2d.geometries.FunctionBasedGeometry;
import org.kelvinho.graph2d.geometries.Geometry;
import org.kelvinho.graph2d.geometries.Point;
import org.kelvinho.graph2d.geometries.Segment;
import org.kelvinho.graph2d.geometries.Polygon;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

import javax.annotation.Nonnull;
import java.util.ArrayList;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Graph2D {
    /*
      type: 0: point, 1: line, 2: function, 3: polygon
      additional color property can be found in the last index of PVector
      relationship between mathematical and real coordinates (x, y is mathematical, X, Y is real):
      X=(x-center.x)*scale.x     Y=(y-center.y)*scale.y
    */
    private ArrayList<Geometry> geometries = new ArrayList<>();
    private PGraphics frame;
    private PApplet proc;
    private PVector scale = new PVector(20, -20), center = new PVector(0, 0), originalCenter = new PVector(0, 0), startCoor = new PVector(0, 0), pos = new PVector(0, 0);
    private int backgroundColor = Environment.Color.color(50), gridColor = Environment.Color.color(200), elementColor = Environment.Color.color(200);
    private int textHeight = 11;
    private boolean pMousePressed = false;
    @SuppressWarnings("FieldCanBeLocal")
    private int preferredDistribution = 30, pointRadius = 10, OxyThickness = 3;
    private boolean mousePressing = false;

    public Graph2D(@Nonnull PApplet proc, int width, int height) {
        this.proc = proc;
        frame = proc.createGraphics(width, height);
    }

    public Graph2D moveGraphLocationOnSketchRelatively(double x, double y) {
        pos = new PVector((float) x, (float) y);
        return this;
    }

    public Graph2D textSize(int textSize) {
        textHeight = textSize;
        return this;
    }

    /*change elements (14), changePoint (4), changeSegment(4), changeFunc (4), changePoly(2)*//*changePoint (4), (i, point), (i, x, y), (i, point, col), (i, x, y, col)*/
    public Graph2D changePoint(int index, @Nonnull Point point) {
        geometries.set(index, point);
        return this;
    }

    public Graph2D changePoint(int index, @Nonnull Point point, int color) {
        point.setColor(color);
        return changePoint(index, point);
    }

    public Graph2D changeSegment(int index, @Nonnull Segment segment) {
        geometries.set(index, segment);
        return this;
    }

    public Graph2D changeSegment(int index, @Nonnull Segment segment, int color) {
        segment.setColor(color);
        return changeSegment(index, segment);
    }

    public Graph2D changeFunc(int index, @Nonnull FunctionBasedGeometry functionBasedGeometry) {
        geometries.set(index, functionBasedGeometry);
        return this;
    }

    public Graph2D changeFunc(int index, @Nonnull FunctionBasedGeometry functionBasedGeometry, int color) {
        functionBasedGeometry.setColor(color);
        return changeFunc(index, functionBasedGeometry);
    }

    public Graph2D changePoly(int index, @Nonnull Polygon polygon) {
        geometries.set(index, polygon);
        return this;
    }

    public Graph2D changePoly(int index, @Nonnull Polygon polygon, int color) {
        polygon.setColor(color);
        return changePoly(index, polygon);
    }


    /*add elements (14), addPoint (4), addSegment(4), addFunc (4), addPoly(2)*/

    public Graph2D addPoint(Point point) {
        geometries.add(new Geometry());
        return changePoint(geometries.size() - 1, point);
    }

    public Graph2D addPoint(Point point, int col) {
        geometries.add(new Geometry());
        return changePoint(geometries.size() - 1, point, col);
    }

    public Graph2D addSegment(Segment segment) {
        geometries.add(new Geometry());
        return changeSegment(geometries.size() - 1, segment);
    }

    public Graph2D addSegment(Segment segment, int col) {
        geometries.add(new Geometry());
        return changeSegment(geometries.size() - 1, segment, col);
    }

    public Graph2D addFunc(FunctionBasedGeometry functionBasedGeometry, int col) {
        geometries.add(new Geometry());
        return changeFunc(geometries.size() - 1, functionBasedGeometry, col);
    }

    public Graph2D addFunc(FunctionBasedGeometry functionBasedGeometry) {
        geometries.add(new Geometry());
        return changeFunc(geometries.size() - 1, functionBasedGeometry);
    }

    public Graph2D addPoly(Polygon polygon, int color) {
        geometries.add(new Geometry());
        return changePoly(geometries.size() - 1, polygon, color);
    }

    public Graph2D addPoly(Polygon polygon) {
        geometries.add(new Geometry());
        return changePoly(geometries.size() - 1, polygon);
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    double findSuitableDistancesBetweenCoordinateLines(double boundaryLength) {
        if (boundaryLength < 0) {
            throw new IllegalArgumentException("Boundary length cannot be less than 0");
        }
        int[] cellWidthsMultiplier = {1, 2, 4, 8};// this means that when looking for a cell width, the format should be <some number in array> * 10^x
        int lowerNumberOfLines = 5, upperNumberOfLines = 15;// this means that the total number of lines for coordinates on screen must be between these values. This boundary and cell widths is loosely dependent and you should think about it before changing anything
        int powersOfTenInvolved = (int) Math.round(Math.floor(Math.log10(boundaryLength / lowerNumberOfLines + 0 * (boundaryLength / lowerNumberOfLines + boundaryLength / upperNumberOfLines) / 2)));
        for (int i = 0; i < cellWidthsMultiplier.length; i++) {
            double possibleCellWidth = cellWidthsMultiplier[i] * Math.pow(10, powersOfTenInvolved);
            double numberOfLines = boundaryLength / possibleCellWidth;
            if (lowerNumberOfLines <= numberOfLines && numberOfLines <= upperNumberOfLines) {
                return possibleCellWidth;
            }
        }
        class CannotFindSuitableWidth extends RuntimeException {
            CannotFindSuitableWidth() {
                super();
            }
        }
        throw new CannotFindSuitableWidth();
    }

    @SuppressWarnings("ConstantConditions")
    String round(double number, int powersOfTenInvolved) {
        //int powersOfTenInvolved = (int) Math.round(Math.log10(2 * Math.abs(number))) - 1;
        int decimalPlaces = powersOfTenInvolved < 0 ? -powersOfTenInvolved : 0;
        int numberBasedOnDecimalPlace = 1;
        for (int i = 0; i < decimalPlaces; i++) {
            numberBasedOnDecimalPlace = numberBasedOnDecimalPlace * 10;
        }
        if (numberBasedOnDecimalPlace == 1) {
            return String.valueOf(Math.round(number));
        } else {
            return String.valueOf(Math.round(number * numberBasedOnDecimalPlace) * 1.0 / numberBasedOnDecimalPlace);
        }
    }

    PGraphics getFrame(int m, int n) {
        /*setting up the environment*/
        if (!(frame.width == m && frame.height == n)) {
            frame = proc.createGraphics(m, n);
        }
        frame.beginDraw();
        frame.background(backgroundColor);
        frame.pushMatrix();
        frame.translate(m / 2, n / 2);
        frame.strokeWeight(1);
        class Boundary {
            double lowerBoundary, upperBoundary;

            Boundary(double lowerBoundary, double upperBoundary) {
                if (upperBoundary < lowerBoundary) {
                    this.upperBoundary = lowerBoundary;
                    this.lowerBoundary = upperBoundary;
                } else {
                    this.lowerBoundary = lowerBoundary;
                    this.upperBoundary = upperBoundary;
                }
            }
        }
        /*mathematical initialization*/
        Boundary rBorderX = new Boundary(-m / 2, m / 2);
        Boundary rBorderY = new Boundary(-n / 2, n / 2);
        Boundary mBorderX = new Boundary(mX.transform(rBorderX.lowerBoundary), mX.transform(rBorderX.upperBoundary));
        Boundary mBorderY = new Boundary(mY.transform(rBorderY.lowerBoundary), mY.transform(rBorderY.upperBoundary));
        /*draw the grid*/
        frame.stroke(gridColor);
        frame.fill(gridColor);

        {
            //drawing the coordinate lines
            double mWidth = mBorderX.upperBoundary - mBorderX.lowerBoundary;
            double mHeight = mBorderY.upperBoundary - mBorderY.lowerBoundary;
            double distanceBetweenXCoordinates = findSuitableDistancesBetweenCoordinateLines(mWidth);
            double distanceBetweenYCoordinates = findSuitableDistancesBetweenCoordinateLines(mHeight);
            int[] xIntegerRange = {(int) Math.round(mBorderX.lowerBoundary / distanceBetweenXCoordinates), (int) Math.round(mBorderX.upperBoundary / distanceBetweenXCoordinates)};
            int[] yIntegerRange = {(int) Math.round(mBorderY.lowerBoundary / distanceBetweenYCoordinates), (int) Math.round(mBorderY.upperBoundary / distanceBetweenYCoordinates)};

            // horizontal lines
            int horizontalTextPadding = 3;
            int expectedTextWidth = 17 + (int) (Math.round(Math.log10(mWidth * 0.7) * textHeight * 6.0/11));
            int finalVerticalPadding = 3;//absolute padding between the diagram's border and the text's border
            frame.textSize(textHeight);

            int respectableVerticalBorder = textHeight / 2 + finalVerticalPadding;

            double realX0 = rX.transform(0);
            double realY0 = rY.transform(0);
            boolean numbersOnXIsFixed = !(realY0 + respectableVerticalBorder * 2 < rBorderY.upperBoundary && realY0 > rBorderY.lowerBoundary);
            boolean numbersOnYIsFixed = !(realX0 + horizontalTextPadding + expectedTextWidth < rBorderX.upperBoundary && realX0 > rBorderX.lowerBoundary);

            double replacementY0 = numbersOnXIsFixed ? (realY0 < 0 ? rBorderY.lowerBoundary + respectableVerticalBorder : rBorderY.upperBoundary - respectableVerticalBorder) : 0;
            double replacementX0 = numbersOnYIsFixed ? (realX0 < 0 ? rBorderX.lowerBoundary + horizontalTextPadding : rBorderX.upperBoundary - expectedTextWidth) : 0;

            frame.textAlign(PConstants.LEFT, PConstants.CENTER);
            for (int i = xIntegerRange[0]; i <= xIntegerRange[1]; i++) {
                double x = rX.transform(i * distanceBetweenXCoordinates);
                drawSegment(new Segment(new Point(x, rBorderY.lowerBoundary), new Point(x, rBorderY.upperBoundary)), Mode.REAL);
                if (numbersOnXIsFixed) {
                    frame.text(round(i * distanceBetweenXCoordinates, (int) Math.round(Math.log10(distanceBetweenXCoordinates * 0.7))), (float) x + horizontalTextPadding, (float) replacementY0);
                } else {
                    frame.text(round(i * distanceBetweenXCoordinates, (int) Math.round(Math.log10(distanceBetweenXCoordinates * 0.7))), (float) x + horizontalTextPadding, (float) rY.transform(0) + respectableVerticalBorder);
                }
            }
            // vertical lines
            for (int i = yIntegerRange[0]; i <= yIntegerRange[1]; i++) {
                double y = rY.transform(i * distanceBetweenYCoordinates);
                drawSegment(new Segment(new Point(rBorderX.lowerBoundary, y), new Point(rBorderX.upperBoundary, y)), Mode.REAL);
                if (numbersOnYIsFixed) {
                    frame.text(round(i * distanceBetweenYCoordinates, (int) Math.round(Math.log10(distanceBetweenYCoordinates * 0.7))), (float) replacementX0, (float) y + respectableVerticalBorder);
                } else {
                    frame.text(round(i * distanceBetweenYCoordinates, (int) Math.round(Math.log10(distanceBetweenYCoordinates * 0.7))), (float) rX.transform(0) + horizontalTextPadding, (float) y + respectableVerticalBorder);
                }
            }
        }
        frame.strokeWeight(OxyThickness);
        drawSegment(new Segment(new Point(rX.transform(0), rBorderY.lowerBoundary), new Point(rX.transform(0), rBorderY.upperBoundary)), Mode.REAL);
        drawSegment(new Segment(new Point(rBorderX.lowerBoundary, rY.transform(0)), new Point(rBorderX.upperBoundary, rY.transform(0))), Mode.REAL);
        /*draw the elements*/
        useStandardColor();
        frame.strokeWeight(1);
        {
            boolean usingStandardColor = true;
            useStandardColor();
            for (Geometry geometry : geometries) {
                if (geometry.getColor() != Geometry.DEFAULT_COLOR) {
                    useColor(geometry.getColor());
                    usingStandardColor = false;
                } else {
                    if (!usingStandardColor) {
                        useStandardColor();
                        usingStandardColor = true;
                    }
                }

                if (geometry instanceof Point) {
                    drawPoint((Point) geometry, Mode.MATH);
                } else if (geometry instanceof Segment) {
                    drawSegment((Segment) geometry, Mode.MATH);
                } else if (geometry instanceof FunctionBasedGeometry) {
                    FunctionBasedGeometry.Function function = ((FunctionBasedGeometry) geometry).getFunction();
                    double now = function.getValue(mX.transform(0)), last;
                    for (int j = (int) rBorderX.lowerBoundary; j < (int) rBorderX.upperBoundary; j++) {
                        last = now;
                        now = function.getValue(mX.transform(j));
                        drawSegment(new Segment(new Point(mX.transform(j - 1), last), new Point(mX.transform(j), now)), Mode.MATH);
                    }
                } else if (geometry instanceof Polygon) {
                    drawPolygon((Polygon) geometry, Mode.MATH);
                } else {
                    throw new RuntimeException("There should not be any more geometry types/you can't use the base Geometry class");
                }
            }
        }
        frame.popMatrix();
        frame.endDraw();
        return frame;
    }

    /*getFrame derivatives (3), PGraphics getFrame(), drawFrame(int m, int n), drawFrame()*/
    PGraphics getFrame() {
        return getFrame(frame.width, frame.height);
    }

    public void drawFrame() {
        proc.image(getFrame(), pos.x, pos.y);
    }

    /*private color shorthand functions (4), useStandardColor(), useColor(PVector col), useStrokeColor(PVector col), useFillColor(PVector col)*/
    private void useStandardColor() {
        frame.fill(elementColor);
        frame.stroke(elementColor);
    }

    private void useColor(int color) {
        useStrokeColor(color);
        useFillColor(color);
    }

    private void useStrokeColor(int color) {
        frame.stroke(color);
    }

    private void useFillColor(int color) {
        frame.fill(color);
    }

    /*mouse manipulation (5), mouse dragging (3), rollMouse(1)*//*mouse dragging (3), pushMouse(), updateMouse(), popMouse()*/
    private void pushMouse() {
        if (pos.x <= proc.mouseX && pos.x + frame.width > proc.mouseX && pos.y <= proc.mouseY && pos.y + frame.height > proc.mouseY) {
            mousePressing = true;
            startCoor = new PVector(proc.mouseX, proc.mouseY);
        }
    }

    private void updateMouse() {
        if (mousePressing) {
            PVector tmp = PVector.sub(new PVector(proc.mouseX, proc.mouseY), startCoor);
            center = PVector.sub(originalCenter, new PVector(tmp.x / scale.x, tmp.y / scale.y));
        }
    }

    private void popMouse() {
        if (mousePressing) {
            updateMouse();
            originalCenter = new PVector(center.x, center.y);
        }
        mousePressing = false;
    }

    public void mouse(boolean mousePressed, int mouseX, int mouseY) {
        if (mousePressed) {
            if (!pMousePressed) {
                pushMouse();
            }
            updateMouse();
        } else {
            if (pMousePressed) {
                popMouse();
            }
        }
        pMousePressed = mousePressed;
    }

    /*rollMouse (1), rollMouse(float count)*/

    @SuppressWarnings("UnusedReturnValue")
    public Graph2D rollMouse(float rollCount) {
        scale((float) (1 - rollCount * 0.1));
        return this;
    }

    private interface TransformationFunction {
        double transform(double input);
    }

    /*private coordinate transformation (6), rX(mX), rY(mY), rP(mP), mX(rX), mY(rY), mP(rP)*/

    private TransformationFunction rX = (mX) -> (mX - center.x) * scale.x;

    private TransformationFunction rY = (mY) -> (mY - center.y) * scale.y;

    private TransformationFunction mX = (rX) -> 1.0 * rX / scale.x + center.x;

    private TransformationFunction mY = (rY) -> 1.0 * rY / scale.y + center.y;

    private TransformationFunction identity = (input) -> input;

    private enum Mode {
        REAL, MATH
    }

    /*private drawing stuff (6), drawMLine(mP1, mP2), drawRLine(rP1, rP2), drawPoint(x, y), drawRPoint(x, y), drawPolygon(mPs), RPoly(rPs)*/

    private void drawSegment(@Nonnull Segment segment, @Nonnull Mode mode) {
        TransformationFunction fx = mode == Mode.REAL ? identity : rX, fy = mode == Mode.REAL ? identity : rY;
        frame.line((float) fx.transform(segment.getPoint1().getX()), (float) fy.transform(segment.getPoint1().getY()), (float) fx.transform(segment.getPoint2().getX()), (float) fy.transform(segment.getPoint2().getY()));
    }

    @SuppressWarnings("SameParameterValue")
    private void drawPoint(@Nonnull Point point, @Nonnull Mode mode) {
        TransformationFunction fx = mode == Mode.REAL ? identity : rX, fy = mode == Mode.REAL ? identity : rY;
        frame.ellipse((float) fx.transform(point.getX()), (float) fy.transform(point.getY()), pointRadius, pointRadius);
    }

    @SuppressWarnings("SameParameterValue")
    private void drawPolygon(@Nonnull Polygon mPolygon, @Nonnull Mode mode) {
        TransformationFunction fx = mode == Mode.REAL ? identity : rX, fy = mode == Mode.REAL ? identity : rY;
        ArrayList<Point> mPoints = mPolygon.getPoints();
        useStrokeColor(mPolygon.getColor());
        frame.fill(0, 0);
        frame.beginShape();
        for (int i = 0; i < mPoints.size() - 1; i++) {
            frame.vertex((float) fx.transform(mPoints.get(i).getX()), (float) fy.transform(mPoints.get(i).getY()));
        }
        frame.vertex((float) fx.transform(mPoints.get(0).getX()), (float) fy.transform(mPoints.get(0).getY()));
        frame.endShape();
        useStandardColor();
    }

    @SuppressWarnings("UnusedReturnValue")
    public Graph2D scale(float factor) {
        scale = PVector.mult(scale, factor);
        return this;
    }
}
