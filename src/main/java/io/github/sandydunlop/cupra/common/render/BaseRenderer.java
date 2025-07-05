package io.github.sandydunlop.cupra.common.render;

//TODO: Use something non-AWT instead of BufferedImage
import java.awt.image.BufferedImage;

import io.github.sandydunlop.cupra.common.fonts.FontSpec;
import io.github.sandydunlop.cupra.common.util.Symbol;

public interface BaseRenderer {
    public void enableClipping(int x1, int y1, int x2, int y2);
    public void disableClipping();

    public void fill(int x1, int y1, int x2, int y2, int color);
    public void drawRectangle(int x1, int y1, int x2, int y2, int color);
    public void drawHorizontalLine(int x1, int x2, int y, int color);
    public void drawVerticalLine(int x, int y1, int y2, int color);
    public void drawImage(String name, BufferedImage image, int x, int y, int width, int height);

    public void setFont(FontSpec fo);
    public void drawText(String text, int x, int y);
    public void drawText(String text, int x, int y, int wrapAt);
    public void drawSymbol(Symbol s, int x, int y, int size, int color);
}
