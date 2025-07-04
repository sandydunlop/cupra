/**
 * AwtRenderer is an implementation of the {@link BaseRenderer} interface that uses Java AWT's {@link Graphics2D}
 * for rendering operations. It provides methods for drawing shapes, images, and text, as well as managing clipping regions.
 * 
 * <p>This renderer supports bitmap font rendering, rectangle and line drawing, image blitting, and symbol rendering.
 * Font options can be set and merged, and text rendering supports optional wrapping and monospaced/variable width fonts.</p>
 * 
 * <p>Usage of this renderer assumes that a valid {@link Graphics2D} context is provided upon construction.</p>
 * 
 * <ul>
 *   <li>{@link #enableClipping(int, int, int, int)} and {@link #disableClipping()} manage the clipping region.</li>
 *   <li>{@link #fill(int, int, int, int, int)}, {@link #drawRectangle(int, int, int, int, int)}, {@link #drawHorizontalLine(int, int, int, int)}, and {@link #drawVerticalLine(int, int, int, int)} provide basic shape drawing.</li>
 *   <li>{@link #drawImage(String, BufferedImage, int, int, int, int)} draws a {@link BufferedImage} at a specified location and size.</li>
 *   <li>{@link #setFont(FontSpec)} sets or merges font options for subsequent text rendering.</li>
 *   <li>{@link #drawText(String, int, int)}, {@link #drawText(String, int, int, int)}, and {@link #drawText(String, int, int, int, String)} render text using bitmap fonts, with optional wrapping and glyph selection.</li>
 *   <li>{@link #drawSymbol(Symbol, int, int, int, int)} renders a symbol from a predefined set using a special font.</li>
 *   <li>{@link #colorFromInt(int)} is a utility method to convert an integer RGB value to a {@link Color}.</li>
 * </ul>
 * 
 * <p>This class is intended for use in desktop environments where AWT is available.</p>
 */
package io.github.sandydunlop.cupra.platform.desktop;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.Dimension;
import io.github.sandydunlop.cupra.common.util.Symbol;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont.Glyph;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.fonts.FontSpec;


public class AwtRenderer implements BaseRenderer {
    private Graphics2D g2d;
    private FontSpec fontOptions = null;


    /**
     * Constructs an {@code AwtRenderer} with the specified {@link Graphics2D} context.
     *
     * @param g2d the {@link Graphics2D} object used for rendering operations
     */
    public AwtRenderer(Graphics2D g2d){
        this.g2d = g2d;    
    }

    
    /**
     * Enables clipping for the graphics context, restricting rendering to the specified rectangular area.
     *
     * @param x1 the x-coordinate of the upper-left corner of the clipping rectangle
     * @param y1 the y-coordinate of the upper-left corner of the clipping rectangle
     * @param x2 the x-coordinate of the lower-right corner of the clipping rectangle
     * @param y2 the y-coordinate of the lower-right corner of the clipping rectangle
     */
    @Override
    public void enableClipping(int x1, int y1, int x2, int y2) {
        g2d.setClip(x1,y1,x2-x1,y2-y1);
    }


    /**
     * Disables any current clipping region on the graphics context, allowing drawing operations
     * to affect the entire surface without restriction.
     */
    @Override
    public void disableClipping() {
        g2d.setClip(null);
    }


    // ================================


    /**
     * Fills a rectangular area on the graphics context with the specified color.
     *
     * @param x1    the x-coordinate of the upper-left corner of the rectangle
     * @param y1    the y-coordinate of the upper-left corner of the rectangle
     * @param x2    the x-coordinate of the lower-right corner of the rectangle
     * @param y2    the y-coordinate of the lower-right corner of the rectangle
     * @param color the color to fill the rectangle, represented as an integer
     */
    @Override
    public void fill(int x1, int y1, int x2, int y2, int color) {
        g2d.setColor(colorFromInt(color));
        g2d.fillRect(x1,y1,x2-x1,y2-y1);
    }


    /**
     * Draws a rectangle on the graphics context using the specified coordinates and color.
     *
     * @param x1    the x-coordinate of the top-left corner of the rectangle
     * @param y1    the y-coordinate of the top-left corner of the rectangle
     * @param x2    the x-coordinate of the bottom-right corner of the rectangle
     * @param y2    the y-coordinate of the bottom-right corner of the rectangle
     * @param color the color of the rectangle, represented as an integer
     */
    @Override
    public void drawRectangle(int x1, int y1, int x2, int y2, int color) {
        g2d.setColor(colorFromInt(color));
        g2d.drawRoundRect(x1, y1, x2-x1, y2-y1, 0, 0);
    }


    /**
     * Draws a horizontal line between two x-coordinates at a specified y-coordinate with the given color.
     *
     * @param x1    the starting x-coordinate of the line
     * @param x2    the ending x-coordinate of the line
     * @param y     the y-coordinate at which to draw the line
     * @param color the color of the line, represented as an integer
     */
    @Override
    public void drawHorizontalLine(int x1, int x2, int y, int color) {
        g2d.setColor(colorFromInt(color));
        g2d.drawLine(x1, y, x2, y);
    }


    /**
     * Draws a vertical line on the graphics context from (x, y1) to (x, y2) with the specified color.
     *
     * @param x     the x-coordinate of the vertical line
     * @param y1    the starting y-coordinate of the line
     * @param y2    the ending y-coordinate of the line
     * @param color the color of the line, represented as an integer (ARGB format)
     */
    @Override
    public void drawVerticalLine(int x, int y1, int y2, int color) {
        g2d.setColor(colorFromInt(color));
        g2d.drawLine(x, y1, x, y2);
    }


    /**
     * Draws the specified {@link BufferedImage} at the given coordinates with the specified width and height.
     *
     * @param name   the name or identifier for the image (may be used for logging or debugging)
     * @param image  the {@link BufferedImage} to be drawn
     * @param x      the x-coordinate of the top-left corner where the image will be drawn
     * @param y      the y-coordinate of the top-left corner where the image will be drawn
     * @param width  the width to scale the image to when drawing
     * @param height the height to scale the image to when drawing
     */
    @Override
    public void drawImage(String name, BufferedImage image, int x, int y, int width, int height) {
		g2d.drawImage(image, x, y, width, height, null);
    }


    // ================================


    /**
     * Sets the font options for the renderer.
     * <p>
     * If the current font options are not set, this method duplicates the provided {@code FontSpec}
     * and assigns it. If font options are already set, it merges the new {@code FontSpec} into the existing options.
     * </p>
     *
     * @param fo the {@code FontSpec} containing font settings to apply
     */
    @Override
    public void setFont(FontSpec fo){
        if (fontOptions == null){
            fontOptions = fo.duplicate();
        }else{
            fontOptions.mergeFrom(fo);
        }
    }


    /**
     * Draws the specified text at the given (x, y) coordinates.
     * <p>
     * This method draws the text using default or previously set styling options.
     * If more control over text rendering is required (such as specifying a maximum width),
     * use the overloaded {@link #drawText(String, int, int, int)} method.
     *
     * @param text the text to be drawn
     * @param x the x-coordinate where the text should start
     * @param y the y-coordinate where the text baseline should be placed
     */
    @Override
    public void drawText(String text, int x, int y) {
        this.drawText(text, x, y, -1);
    }


    /**
     * Draws the specified text at the given (x, y) coordinates, with optional word wrapping.
     *
     * @param text   the text to be drawn
     * @param x      the x-coordinate where the text should start
     * @param y      the y-coordinate where the text should start
     * @param wrapAt the maximum width in pixels at which the text should wrap; if 0 or negative, no wrapping is applied
     */
    @Override
    public void drawText(String text, int x, int y, int wrapAt) {
        this.drawText(text, x, y, wrapAt, null);
    }


    /**
     * Renders the specified text onto an image buffer and draws it at the given coordinates.
     * Supports optional word wrapping and custom glyph sets.
     *
     * @param text     The text string to render. If blank, nothing is drawn.
     * @param x        The x-coordinate where the rendered text image will be drawn.
     * @param y        The y-coordinate where the rendered text image will be drawn.
     * @param wrapAt   The maximum width in pixels before wrapping text to a new line. 
     *                 If -1, no wrapping is applied.
     * @param glyphs   An optional string specifying the set of glyphs to use for rendering.
     *                 If null, the default glyph set is used.
     */
    public void drawText(String text, int x, int y, int wrapAt, String glyphs) {
        if (text.isBlank()){
            return;
        }
        BitmapFont font;
        if (glyphs == null) {
            font = BitmapFontFactory.load(fontOptions);
        } else {
            font = BitmapFontFactory.load(fontOptions, glyphs);
        }
        Dimension dimensions = font.getStringDimensions(text, wrapAt);
        int fontHeightExtra = 3;
        int spaceWidth = font.getSpaceWidth();
        int fontHeight = font.getHeight();
        int renderHeight = dimensions.getHeight() + 4;
        int renderWidth = dimensions.getWidth();
        BufferedImage fontImage = font.getImage();
        BufferedImage stringImage = new BufferedImage(renderWidth, renderHeight + fontHeightExtra, fontImage.getType());
        Graphics g = stringImage.getGraphics();

        int charX = 0;
        int charY = 0;
        int charWidth = font.getEmWidth();
        for (int i=0; i<text.length(); i++){
            char c = text.charAt(i);
            if (c == ' '){
                charWidth = spaceWidth;
            }else{
                Glyph glyph = font.getGlyph(c);
                if (!fontOptions.getMonospaced()){
                    charWidth = glyph == null ? spaceWidth : glyph.width;
                }
                if (glyph != null){
                    g.drawImage(fontImage, 
                        charX, charY, 
                        charX + glyph.width, charY + font.getHeight(), 
                        glyph.x, glyph.y, 
                        glyph.x + glyph.width, 
                        glyph.y + font.getHeight(), 
                        null);
                }
            }
            if (wrapAt != -1 && charX + charWidth > wrapAt){
                charX = 0;
                charY += fontHeight + fontHeightExtra;
            }else{
                charX += charWidth;
            }
        }
        
        drawImage(null, stringImage, x, y, renderWidth, renderHeight+ fontHeightExtra);
        g.dispose();
    }


    /**
     * Converts an integer representation of a color (in 0xRRGGBB format) to a {@link Color} object.
     *
     * @param color the integer value representing the color, where the highest byte is red,
     *              the middle byte is green, and the lowest byte is blue (0xRRGGBB)
     * @return a {@link Color} object corresponding to the specified RGB value
     */
    public static Color colorFromInt(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >>  8) & 0xFF;
        int b = (color >>  0) & 0xFF;
        return new Color(r, g, b);
    }

    
    /**
     * Draws a symbol at the specified coordinates with the given size and color.
     *
     * <p>This method selects a symbol from a predefined set based on the ordinal value
     * of the provided {@link Symbol} enum. It uses a custom font ("modernpics") to render
     * the symbol as text at the specified (x, y) position.</p>
     *
     * @param s     the {@link Symbol} to draw, selected by its ordinal position
     * @param x     the x-coordinate where the symbol will be drawn
     * @param y     the y-coordinate where the symbol will be drawn
     * @param size  the font size to use when drawing the symbol
     * @param color the color to use when drawing the symbol
     */
    public void drawSymbol(Symbol s, int x, int y, int size, int color) {
        final String symbols = " !#$%'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}×✎⧉";
        FontSpec symbolFont = new FontSpec()
                .setName("modernpics")
                .setSize(size)
                .setColor(color);
        setFont(symbolFont);
        drawText(symbols.substring(s.ordinal(), s.ordinal()+1), x, y, -1, symbols);
    }
}

