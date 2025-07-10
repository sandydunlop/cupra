package io.github.sandydunlop.cupra.common.palette;

public class Watermelon extends ColorPalette{
    public Watermelon() {
        REGULAR_BACKGROUND = 0x88303030;
        REGULAR_TEXT = 0xFF808080;
        HOVERED_BACKGROUND = 0xFF701F27;
        HOVERED_BORDER = 0xFFFD4659;
        SELECTED_BACKGROUND = 0xFFC43645;
        SELECTED_TEXT = 0xFFDDDDDD;
	    INPUT_BACKGROUND = 0xFF000000;
        INPUT_SEPARATOR = argbLerp(0.3f, REGULAR_BACKGROUND, INPUT_BACKGROUND);
        WIDGET_TEXT = 0xFF808080;
    }
}
