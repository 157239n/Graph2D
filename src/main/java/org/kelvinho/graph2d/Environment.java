package org.kelvinho.graph2d;

class Environment {
    static class Color {
        static int color(int r, int g, int b) {
            return -1 - (255 - b) - (255 - g) * 256 - (255 - r) * 256 * 256;
        }

        static int color(int c) {
            return color(c, c, c);
        }

        static int color(double c) {
            return color((int) c, (int) c, (int) c);
        }

        static int red(int color) {
            return 255 - (-color - 1) / (256 * 256);
        }

        static int green(int color) {
            return 255 - ((-color - 1) % (256 * 256)) / 256;
        }

        static int blue(int color) {
            return 255 - (-color - 1) % 256;
        }

        static int makeGrey(int color) {
            return color(0.2989 * red(color) + 0.5870 * green(color) + 0.1141 * blue(color));
        }
    }
}
