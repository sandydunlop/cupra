package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.CupraException;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;


public class CListBoxEntry extends CWidget {
	private CListBox listBox = null;
	private final String title;
	private String key = null;
	private Object value;
	private boolean selected = false;
	private int renderX = 0;
	private int renderY = 0;


	public CListBoxEntry(String title, String key, Object value) {
		super(null);
		this.title = title;
		this.key = key;
		this.value = value;
		this.font = null;
	}


	public CListBoxEntry(String title, Object value) {
		this(title, null, value);
	}


	public void setParent(CContainer parent) {
		this.parent = parent;
	}


	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	public boolean isSelected() {
		return this.selected;
	}


	public Object getValue() {
		return value;
	}


	public String getKey() {
		return this.key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public void setListBox(CListBox listBox) {
		this.listBox = listBox;
	}


	@Override
    public void recalculateSize() {
		// BitmapFont bmf = BitmapFontFactory.load(font);
		// this.height = bmf.getHeight() + 1;
		if (value == null) {
			BitmapFont bmf = BitmapFontFactory.load(font);
			height = bmf.getHeight() + 1;
			width = bmf.stringWidth(title);
		} else {
			// TODO: Make this work
			
		}
    }


    @Override
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta)  throws CupraException {
		int backgroundColor = CWidget.getPalette().INPUT_BACKGROUND;
		int textColor = CWidget.getPalette().REGULAR_TEXT;
		renderX = getCalculatedX();
		renderY = getCalculatedY();
		int w = getWidth();
		if (isSelected()) {
			backgroundColor = CWidget.getPalette().SELECTED_BACKGROUND;
			textColor = CWidget.getPalette().SELECTED_TEXT;
		}
		if (isHovered(mouseX, mouseY)) {
			backgroundColor = CWidget.getPalette().HOVERED_BACKGROUND;
		}
        renderer.fill(renderX, renderY, renderX + w, renderY + getHeight(), backgroundColor);
		if (isHovered(mouseX, mouseY)) {
        	renderer.drawRectangle(renderX, renderY, renderX + w, renderY + getHeight() - 1, CWidget.getPalette().HOVERED_BORDER);
		}
		if (font != null) {
			font.setColor(textColor);
			renderer.setFont(font);
			renderer.drawText(title, renderX + 4, renderY + 1);
		}
	}


	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= renderX&& mouseX <= renderX + getWidth() && mouseY >= renderY && mouseY < renderY + getHeight();
	}


	public String getTitle() {
		return title;
	}
}