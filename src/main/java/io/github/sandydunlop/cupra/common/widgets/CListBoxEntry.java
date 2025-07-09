package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;


public class CListBoxEntry extends CWidget {
	private static final int LIST_ENTRY_HORIZONTAL_PADDING = 3;
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
		if (value == null) {
			BitmapFont bmf = BitmapFontFactory.load(font);
			height = bmf.getHeight() + 1;
			width = bmf.stringWidth(title);
		} else {
			// TODO: Make this work
			
		}
    }


    @Override
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) {
		renderX = getCalculatedX() + 1;
		renderY = getCalculatedY();
		if (isHovered(mouseX, mouseY)) {
			int visibleWidth = listBox.componentVisibleWidth() - 1;
            // Code will go here for more complex entries
		}
		if (font != null) {
			int textColor = isSelected() ? CWidget.getPalette().SELECTED_TEXT : CWidget.getPalette().REGULAR_TEXT;
			font.setColor(textColor);
			renderer.setFont(font);
			renderer.drawText(title, renderX + LIST_ENTRY_HORIZONTAL_PADDING, renderY);
		}
	}


	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= renderX && mouseX <= renderX + width && mouseY >= renderY && mouseY < renderY + getHeight();
	}


	public String getText() {
		return title;
	}


	public String toString() {
		return title;
	}
}