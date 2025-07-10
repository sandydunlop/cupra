package io.github.sandydunlop.cupra.common.palette;

public class Copper extends ColorPalette{
    public Copper() {
        REGULAR_BACKGROUND = 0x88211E22;
        REGULAR_TEXT = 0xFFBBBBBB;
        HOVERED_BACKGROUND = 0xFF613122;
        HOVERED_BORDER = 0xFF88442F;
        SELECTED_BACKGROUND = 0xFFA05030;
        SELECTED_TEXT = 0xFFDDDDDD;
	    INPUT_BACKGROUND = 0xFF000000;
        INPUT_SEPARATOR = argbLerp(0.3f, REGULAR_BACKGROUND, INPUT_BACKGROUND);
        WIDGET_TEXT = 0xFFDDDDDD;
    }
}
