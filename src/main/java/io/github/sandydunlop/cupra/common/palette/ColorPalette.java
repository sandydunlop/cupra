package io.github.sandydunlop.cupra.common.palette;

public class ColorPalette {
    public int REGULAR_BACKGROUND = 0xFF000000;
    public int REGULAR_TEXT = 0xFFFFFFFF;
    public int HOVERED_BACKGROUND = 0xFF000000;
    public int HOVERED_BORDER = 0xFFFFFFFF;
    public int SELECTED_BACKGROUND = 0xFFFFFFFF;
    public int SELECTED_TEXT = 0xFF000000;
    public int INPUT_BACKGROUND = 0xFF000000;
    public int WIDGET_TEXT = 0xFFFFFFFF;

    public static final Copper Copper = new Copper();
    public static final Watermelon Watermelon = new Watermelon();
    public static final Avocado Avocado = new Avocado();
    public static final Blueberry Blueberry = new Blueberry();
    public static final Banana Banana = new Banana();

    public static int argbLerp(float delta, int start, int end) {
        int alpha = lerp(delta, getAlpha(start), getAlpha(end));
        int red = lerp(delta, getRed(start), getRed(end));
        int green = lerp(delta, getGreen(start), getGreen(end));
        int blue = lerp(delta, getBlue(start), getBlue(end));
        return getArgb(alpha, red, green, blue);
    }

    public static int lerp(float delta, int start, int end) {
        return start + (int)Math.floor(delta * (end - start));
    }

    public static int getAlpha(int argb) {
        return argb >>> 24;
    }

    public static int getRed(int argb) {
        return argb >> 16 & 255;
    }

    public static int getGreen(int argb) {
        return argb >> 8 & 255;
    }

    public static int getBlue(int argb) {
        return argb & 255;
    }

    public static int getArgb(int alpha, int red, int green, int blue) {
        return alpha << 24 | red << 16 | green << 8 | blue;
    }
}
