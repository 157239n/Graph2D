# Graph2D - quick and dirty Processing-based 2d graphing library

## Installing it

There are many ways to use this library. Either use it via Gradle, use it as a jar file, or use it directly in the Processing IDE.

### Using Gradle

Your build.gradle file should look something like this

```
plugins {
    // Apply the java plugin to add support for Java
    id 'java'

    // Apply the application plugin to add support for building an application
    id 'application'
}

// Define the main class for the application
mainClassName = 'App'

dependencies {
    // This dependency is found on compile classpath of this component and consumers.
    compile 'com.google.guava:guava:23.0'
    compile 'org.processing:core:3.3.7'
    implementation 'com.github.157239n:Graph2D:d87d1eadbc'

    // Use JUnit test framework
    testCompile 'junit:junit:4.12'
}

// In this section you declare where to find the dependencies of your project
repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()

    maven { url 'https://jitpack.io' }
}
```

The most important lines are `maven { url 'https://jitpack.io' }`, as the library is published on jitpack and `implementation 'com.github.157239n:Graph2D:d87d1eadbc'` as it will include the library.

### Use directly in the Processing IDE

On the repository there is a "Releases" section:

![](https://i.imgur.com/cLAQrOB.png)

There, you will see the file "graph2d.jar" in the latest release. Download this file.

![](https://i.imgur.com/r9H0B5V.png)

Go to Preferences:

![](https://i.imgur.com/yr3nTrZ.png)

See where is your sketchbook folder (mine is E:\Repository\Processing):

![](https://i.imgur.com/eIHHnqZ.png)

Inside the sketchbook folder there is a folder called "libraries". This is where every Processing libraries are placed.

![](https://i.imgur.com/kI3Bztt.png)

Create a folder "graph2d" here.
Inside that folder create a folder "library".
Copy the graph2d.jar file there:

![](https://i.imgur.com/LioB5gs.png)

Close every Processing window and relaunch Processing. It should be there:

![](https://i.imgur.com/mvT8g33.png)

Click on it and you are all set.

## Using it

First, you must define a graph with a width and height: `Graph2D graph = new Graph2D(this, 600, 600);`

Then you can adjust its position: `graph.moveGraphLocationOnSketchRelatively(0, 0);`

You can add points to the graph: `graph.addPoint(new Point(0, 0), color(255, 0, 0))` with color, or `graph.addPoint(new Point(0, 0))` using the default color.

You can add segments to the graph: `graph.addSegment(new Segment(new Point(0, 1), new Point(2, 3)))`

You can add functions to the graph:

```
FunctionBasedGeometry.Function function = new FunctionBasedGeometry.Function() {
    public double getValue(double... x) {
        return pow(x[0], 2) + 1;
    }
};
FunctionBasedGeometry f = new FunctionBasedGeometry(function);

graph.addFunc(f, color(0, 255, 0));
```

This example adds the function "x^2 + 1" to the graph. This example is for people who use the Processing IDE which doesn't support lambda expressions (as of 3.7). If you are not using Processing then you can do something like:

```
FunctionBasedGeometry f = new FunctionBasedGeometry(x -> Math.pow(x[0], 2) + 1);
graph.addFunc(f, color(0, 255, 0));
```

If you want to change an object, be it point, segment, or function, you can do this:

`graph.changeSegment(0, new Segment(new Point(5, 6), new Point(7, 8)));`

The "0" in the first parameter is the index of the segment when you add a segment in. So if you want to change the first segment you put in the graph, you call `graph.changeSegment(0, ...)`, if you want to change the 5th function you put in, you call `graph.changeFunc(4, ...)`

