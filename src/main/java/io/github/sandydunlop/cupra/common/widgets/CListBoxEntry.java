package io.github.sandydunlop.cupra.common.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;


public class CListBoxEntry extends CWidget {
	private static final int LIST_ENTRY_HORIZONTAL_PADDING = 3;
	private CListBox listBox = null;
	private final String title;
	private String key = null;
	private Object value;
	private Object[] values = null;
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


	public void setValues(Object... values) {
		if (values == null) return;
		this.values = Arrays.copyOf(values, values.length);
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
		BitmapFont bmf = BitmapFontFactory.load(font);
		if (title != null) {
			height = bmf.getHeight() + 1;
			width = bmf.stringWidth(title);
		} else if (values != null) {
			height = bmf.getHeight() + 1;
			width = 0;
			for (int i=0; i<values.length; i++) {
				String text = (String)values[i];
				width += bmf.stringWidth(text);
			}			
		}
    }


    @Override
    public void render(BaseRenderer renderer, CMouseEvent mouse) {
		renderX = getCalculatedX() + 1;
		renderY = getCalculatedY();
		if (isHovered(mouse)) {
			int visibleWidth = listBox.componentVisibleWidth() - 1;
            // Code will go here for more complex entries
		}
		if (title != null) {
			int textColor = isSelected() ? CWidget.getPalette().SELECTED_TEXT : CWidget.getPalette().REGULAR_TEXT;
			font.setColor(textColor);
			renderer.setFont(font);
			renderer.drawText(title, renderX + LIST_ENTRY_HORIZONTAL_PADDING, renderY);
		} else if (values != null) {
			int textColor = isSelected() ? CWidget.getPalette().SELECTED_TEXT : CWidget.getPalette().REGULAR_TEXT;
			font.setColor(textColor);
			renderer.setFont(font);
			int componentX = renderX;
			for (int i=0; i<values.length; i++) {
				Object o = values[i];
				CListBoxColumn column = listBox.columns.get(i);
				String text = (String)o;
				if (text != null) {
					renderer.drawText(text, componentX + LIST_ENTRY_HORIZONTAL_PADDING, renderY);
				}
				componentX += column.getWidth();
			}
		}
	}


	public boolean isHovered(CMouseEvent mouse) {
		return mouse.getX() >= renderX && mouse.getX() <= renderX + width && mouse.getY() >= renderY && mouse.getY() < renderY + getHeight();
	}


	public String getText() {
		return title;
	}


	public String toString() {
		return title;
	}
}