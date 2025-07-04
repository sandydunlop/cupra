package io.github.sandydunlop.cupra.common.fonts;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


import io.github.sandydunlop.cupra.common.fonts.BitmapFont.Glyph;


public class BitmapFontFactory {
	private BitmapFontFactory() {
		// Hide the public constructor
	}


    private static HashMap<String,BitmapFont> fontAtlas = new HashMap<>();

	public static BitmapFont load(FontSpec font) {
		final String fontGlyphs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890"
				+ "!@£$%^&*()-_=+[]{};:'\"\\|,.<>/?~`±§#"
				+ "“”‘’«»‹›„…";
		return load(font, fontGlyphs);
	}


	public static BitmapFont load(FontSpec font, String glyphs) {
		String key = font.getKey();
		if (fontAtlas.containsKey(key)){
			return fontAtlas.get(key);
		}
		BitmapFont f;
		try {
            InputStream ttfStream = BitmapFont.class.getClassLoader().getResourceAsStream("fonts/" + font.getName() + ".ttf");
			f = create(font, ttfStream, glyphs);
			fontAtlas.put(key, f);
		}
		catch (Exception e) {
			System.err.println("FONT LOADING ERROR for '" + font.getName() + "': " + e.getMessage());
			return null;
		}
		return f;
	}


	private static BitmapFont create(FontSpec fontSpec, InputStream fontStream, String glyphs) throws FontFormatException, IOException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
		int style = Font.PLAIN;
		if (fontSpec.getBold()){
			style |= Font.BOLD;
		}
		if (fontSpec.getItalic()){
			style |= Font.ITALIC;
		}
		font = font.deriveFont(style, fontSpec.getSize()+ 1f);
		return create(fontSpec, font, glyphs);
	}
	

	private static BitmapFont create(FontSpec fontSpec, Font font, String glyphs) {
		FontMetrics fm = new Canvas().getFontMetrics(font);
		final int ascent = fm.getAscent();
		final int descent = fm.getDescent();
		final int verticalSpacing = 4;
		final int area = ((verticalSpacing * glyphs.length())+ fm.stringWidth(glyphs)) * (ascent + descent + verticalSpacing);	
		final int width = Integer.highestOneBit((int)Math.ceil(Math.sqrt(area))) << 1;
		final int height = width;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = (Graphics2D)image.getGraphics();
		graphics.setColor(new Color(fontSpec.getColor(), true));
		graphics.setFont(font);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, fontSpec.getAntialias() ? 
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);	
		Map<Character, Glyph> glyphMap = new HashMap<>();
		final int glyphCount = glyphs.length();
		int x = 0;
		int y = ascent;
		for(int i = 0; i < glyphCount; i++) {
			char glyph = glyphs.charAt(i);
			String glyphString = Character.toString(glyph);
			Rectangle2D r2d = fm.getStringBounds(glyphString, graphics);
			int glyphWidth = r2d.getBounds().width;
			if (x + glyphWidth > width) {
				x = 0;
				y = y + ascent + descent + verticalSpacing;
			}
			if (fontSpec.getShadow()) {
				graphics.setColor(new Color(0xCC000000, true));
				graphics.drawString(glyphString, x+1, y+1);
				graphics.setColor(new Color(fontSpec.getColor(), true));
			}
			graphics.drawString(glyphString, x, y);
			glyphMap.put(glyph, new BitmapFont.Glyph(glyph, x, y - ascent, glyphWidth));
			x += glyphWidth + verticalSpacing;
		}
		return new BitmapFont(fontSpec, image, glyphMap, ascent + descent);
	}
}
