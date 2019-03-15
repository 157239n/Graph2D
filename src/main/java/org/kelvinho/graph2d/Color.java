package org.kelvinho.graph2d;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Color {
    public static int color(int r, int g, int b) {
        return -1 - (255 - b) - (255 - g) * 256 - (255 - r) * 256 * 256;
    }

    public static int color(int c) {
        return color(c, c, c);
    }

    public static int color(double c) {
        return color((int) c, (int) c, (int) c);
    }

    public static int red(int color) {
        return 255 - (-color - 1) / (256 * 256);
    }

    public static int green(int color) {
        return 255 - ((-color - 1) % (256 * 256)) / 256;
    }

    public static int blue(int color) {
        return 255 - (-color - 1) % 256;
    }

    public static int makeGrey(int color) {
        return color(0.2989 * red(color) + 0.5870 * green(color) + 0.1141 * blue(color));
    }
}
