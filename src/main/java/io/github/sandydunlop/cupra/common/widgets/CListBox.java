package io.github.sandydunlop.cupra.common.widgets;

import java.util.ArrayList;
import java.util.List;

import io.github.sandydunlop.cupra.common.CupraException;
import io.github.sandydunlop.cupra.common.events.CListBoxSelectionChangedEvent;
import io.github.sandydunlop.cupra.common.events.CListBoxSelectionChangedListener;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;


public class CListBox extends CScrollableContainer {
	private List<CListBoxSelectionChangedListener> listeners = new ArrayList<>();
    private CSelectionChangedAction onSelectionChanged;
    private CListBoxEntry selected = null;
	private int itemHeight = 0;


    public CListBox(CContainer parent) {
        this(parent, null);
    }


    public CListBox(CContainer parent, CSelectionChangedAction onSelectionChanged) {
		super(parent);
        this.onSelectionChanged = onSelectionChanged;
        this.setExpandable(false);
        this.setFocusable(true);
        this.setMouseOverEffects(true);
        this.setBackgroundColor(CWidget.getPalette().INPUT_BACKGROUND);
        this.setBorderColor(CWidget.getPalette().HOVERED_BORDER);
        this.setPadding(0);
        content = new CContainer();
        content.setPadding(0);
    }
    

    public interface CSelectionChangedAction {
        void onSelectionChanged(CListBoxEntry selection);
    }


    public void setSelected(CListBoxEntry entry) {
        if (this.selected != entry) {
            if (this.selected != null) {
                this.selected.setSelected(false);
            }
            this.selected = entry;
            if (this.selected != null) {
                this.selected.setSelected(true);
                scrollTo(this.selected);
            }
            CListBoxSelectionChangedEvent event = new CListBoxSelectionChangedEvent(this);
            fireListBoxSelectionChangedEvent(event);
            if (this.onSelectionChanged != null) {
                this.onSelectionChanged.onSelectionChanged(entry);
            }
        }
    }


    public void setSelectedIndex(int index) {
        if (index < 0 || index >= content.contents().size()) {
            return;
        }
        CListBoxEntry entry = getEntry(index);
        setSelected(entry);
    }   


    public CListBoxEntry getSelected() {
        return this.selected;
    }


    public int getSelectedIndex() {
        return getIndexOf(this.selected);
    }


    public int getIndexOf(CListBoxEntry entry) {
        return content.contents().indexOf(entry);
    }


    public int getItemHeight() {
        return itemHeight;
    }


    public CListBoxEntry getEntry(String key) {
        for (CWidget widget : content.contents()) {
            CListBoxEntry entry = (CListBoxEntry)widget;
            if (entry.getKey()!= null && entry.getKey().equals(key)) {
                return entry;
            }
        }
        return null;
    }


    public CListBoxEntry getEntry(int index) {
        if (index < 0 || index >= content.contents().size()) {
            return null;
        }
        return (CListBoxEntry)content.contents().get(index);
    }


    public List<CWidget> getEntries() {
        return content.contents();
    }


    public int count() {
        return content.contents().size();
    }


    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        getVerticalScrollAmount();
        if (content != null && !horizontalScrollingEnabled) {
            content.setWidth(width - verticalScrollBar.getWidth());
            for (CWidget widget : content.contents()) {
                widget.setWidth(width - verticalScrollBar.getWidth());
            }
        }
        super.layout();
    }


    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        getHorizontalScrollAmount();
        if (content != null) {
            content.setHeight(height - horizontalScrollBarHeightUsed());
        }
        super.layout();
    }


    @Override
    public int getDefaultHeight(){
        return 80;
    }


    @Override
    public int getDefaultWidth(){
        return 200;
    }


    @Override
    public void add(CWidget w) {
        // Do nothing
    }


    public synchronized void add(CListBoxEntry item) {
        if (content.contents().isEmpty()) {
            if (item.font == null) {
                item.font = this.font;
            }
            itemHeight = heightOf(item);
        }
        item.setHeight(itemHeight);
        if (item.font == null) {
            item.font = this.font;
        }
        item.setY(content.contents().size() * item.getHeight());

        if (!horizontalScrollingEnabled) {
            item.setWidth(this.width - verticalScrollBar.getWidth());
        }

        item.setParent(content);
        item.setListBox(this);
        content.contents().add(item);
        setContentHeight(content.contents().size() * item.getHeight());

        if (horizontalScrollingEnabled) {
            item.recalculateSize();
            if (item.getWidth() > getContentWidth()) {
                setContentWidth(item.getWidth());
            }
        }
    }


    public synchronized void insertAt(int index, CListBoxEntry item) {
        if (content.contents().isEmpty()) {
            if (item.font == null) {
                item.font = this.font;
            }
            itemHeight = heightOf(item);
        }
        item.setHeight(itemHeight);
        if (item.font == null) {
            item.font = this.font;
        }

        if (!horizontalScrollingEnabled) {
            item.setWidth(this.width - verticalScrollBar.getWidth());
        }

        item.setParent(content);
        item.setListBox(this);
        
        if (index < 0 || index >= content.contents().size()) {
            content.contents().add(item);
        } else {
            content.contents().add(index, item);
        }
        for (int i = index; i < content.contents().size(); i++) {
            CWidget widget = content.contents().get(i);
            widget.setY(i * item.getHeight());
        }
        setContentHeight(content.contents().size() * item.getHeight());
        if (horizontalScrollingEnabled) {
            item.recalculateSize();
            if (item.getWidth() > getContentWidth()) {
                setContentWidth(item.getWidth());
            }
        }
    }   


    private int heightOf(CListBoxEntry item) {
        if (item.getHeight() == 0) {
            item.layout();
            if (item.getHeight() == 0){
                if (this.font != null) {
                    BitmapFont bmf = BitmapFontFactory.load(font);
                    return bmf.getHeight() + 1;
                }else{
                    return 20;
                }
            }else{
                return item.getHeight() + 1;
            }
        }else{
            return item.getHeight() + 1;
        }
    }


    @Override
    public void clear() {
        content.clear();
        this.verticalScrollAmount = 0.0;
    }


    protected CListBoxEntry getEntryAtPosition(CMouseEvent mouse) {
        CWidget w = internalHoveredWidget(mouse);
        return (CListBoxEntry)w;
    }


    @Override
    public int getCalculatedWidth() {
        if (width == 0) {
            return getDefaultWidth();
        }else{
            return width;
        }
    }

    
    @Override
    public int getCalculatedHeight() {
        if (height == 0) {
            return getDefaultHeight();
        }else{
            return width;
        }
    }

    
    @Override
    public void layout() {
        if (this.width > 0) {
            if (CWidget.isFontCacheInvalidated() && !content.contents().isEmpty()) {
                CWidget widget = content.contents().get(0);
                itemHeight = widget.getCalculatedHeight();
            }
            if (!horizontalScrollingEnabled) {
                content.setWidth(getWidth() - verticalScrollBar.getWidth());
            }
            int renderY = 0;
            int widest = 0;
            for (CWidget widget : content.contents()) {
                widget.setY(renderY);
                if (!horizontalScrollingEnabled) {
                    widget.setWidth(this.width - verticalScrollBar.getWidth());
                }
                widget.setHeight(itemHeight);
                renderY += widget.getHeight();
                if (widget.getWidth() > widest) {
                    widest = widget.getWidth();
                }
            }
            setContentHeight(content.contents().size() * itemHeight);
            if (horizontalScrollingEnabled) {
                setContentWidth(widest);
            }
            double sa = getVerticalScrollAmount(); // This puts content at the correct position
            setVerticalScrollAmount(sa);
            super.layout();
        }
    }


    @Override
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) throws CupraException {
        super.render(renderer, mouseX, mouseY, delta);
        if (visible) {
            int renderWidth = getWidth();
            int renderHeight = getHeight();
            int borderColor = CWidget.getPalette().SELECTED_BACKGROUND;
            renderer.drawRectangle(getCalculatedX(), getCalculatedY(), getCalculatedX() + renderWidth, getCalculatedY() + renderHeight, borderColor);
        }
    }


    // === Mouse ===


    @Override
    public boolean mousePressed(CMouseEvent mouse) {
        super.mousePressed(mouse);
        if (!this.isMouseOver(mouse)) return false;

        if (mouse.getX() <= content.getWidth()) {
            CListBoxEntry entry = this.getEntryAtPosition(mouse);
            if (entry != null) {
                setSelected(entry);
                return true;
            }
        }
        return this.scrolling;
    }


    // === Events ===
        
    
	public void removeListBoxSelectionChangedListener(CListBoxSelectionChangedListener listener) {
		listeners.remove(listener);
	}


	public void addListBoxSelectionChangedListener(CListBoxSelectionChangedListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<>();
		}
		listeners.add(listener);
	}


	private void fireListBoxSelectionChangedEvent(CListBoxSelectionChangedEvent event) {
		for (CListBoxSelectionChangedListener listener : listeners) {
			listener.selectionChanged(event);
		}
	}
}