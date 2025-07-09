package io.github.sandydunlop.cupra.common.widgets;

import java.util.ArrayList;
import java.util.List;

import io.github.sandydunlop.cupra.common.events.CListBoxSelectionChangedEvent;
import io.github.sandydunlop.cupra.common.events.CListBoxSelectionChangedListener;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;


public class CListBox extends CScrollableContainer {
	private List<CListBoxSelectionChangedListener> listeners = new ArrayList<>();
    private SelectionChangedAction onSelectionChanged;
    private ClickAction onClick;
    private CListBoxEntry selected = null;
	private int itemHeight = 0;
	protected boolean listHasBackground = true;
	protected int listBackgroundColor = CWidget.getPalette().INPUT_BACKGROUND;
	protected boolean listHasBorder = true;
	protected int listBorderColor = CWidget.getPalette().HOVERED_BORDER;
    private int renderX;
    private int renderY;


    public CListBox(CContainer parent) {
        this(parent, null);
    }


    public CListBox(CContainer parent, SelectionChangedAction onSelectionChanged) {
		super(parent);
        this.onSelectionChanged = onSelectionChanged;
        this.setExpandable(false);
        this.setFocusable(true);
        this.setMouseOverEffects(true);
        this.setPadding(0);
        content = new CContainer();
        content.setPadding(0);
    }
    

    public interface SelectionChangedAction {
        void onSelectionChanged(CListBoxEntry selection);
    }


    public interface ClickAction {
        void onClick(CListBoxEntry selection);
    }


    public void onClick(ClickAction action) {
        this.onClick = action;
    }


    /*
     * Sets the background color of the CListBox. This is rendered before the CListBox's container
     * component, and hides the inherited setBackgroundColor method.
     * @param [color] ARGB representation of the color.
     */
    @Override
	public void setBackgroundColor(int color) {
		listBackgroundColor = color;
	}


    /*
     * Sets the border color of the CListBox. This is rendered after the CListBox's container
     * component, and hides the inherited setBorderColor method.
     * @param [color] ARGB representation of the color.
     */
    @Override
	public void setBorderColor(int color) {
		listBorderColor = color;
	}


    /*
     * Specifies which entry within the listbox is selected.
     * @param entry The entry to select
     */
    public void setSelected(CListBoxEntry entry) {
        if (selected != entry) {
            if (selected != null) {
                selected.setSelected(false);
            }
            selected = entry;
            if (selected != null) {
                selected.setSelected(true);
                scrollTo(selected);
            }
            CListBoxSelectionChangedEvent event = new CListBoxSelectionChangedEvent(this);
            fireListBoxSelectionChangedEvent(event);
            if (onSelectionChanged != null) {
                onSelectionChanged.onSelectionChanged(entry);
            }
        }
    }


    /*
     * Specifies which entry within the listbox is selected.
     * @param index The index of the entry to select
     */
    public void setSelectedIndex(int index) {
        if (index < 0 || index >= content.contents().size()) {
            return;
        }
        CListBoxEntry entry = getEntry(index);
        setSelected(entry);
    }   


    /*
     * Retrieves the seleted entry.
     * @return The selected entry
     */
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
        item.setParent(content);
        item.setListBox(this);
        content.contents().add(item);
        setContentHeight(content.contents().size() * item.getHeight());

        if (horizontalScrollingEnabled) {
            item.recalculateSize();
        }
        if (item.getWidth() > getContentWidth()) {
            setContentWidth(item.getWidth());
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
        }
        if (item.getWidth() > getContentWidth()) {
            setContentWidth(item.getWidth());
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


    protected CListBoxEntry entryAtMousePointer(int mouseX, int mouseY) {
        if (!isMouseOver(mouseX, mouseY)) return null;
        int yWithinBox = mouseY - renderY;
        int yWithinList = yWithinBox + (int)verticalScrollAmount;
        int posWithinList = yWithinList / itemHeight;
        return getEntry(posWithinList);
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


    //
    // === Layout & Render ===
    //

    
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
            int componentY = 0;
            int widest = 0;
            for (CWidget widget : content.contents()) {
                widget.setY(componentY);
                if (!horizontalScrollingEnabled) {
                    widget.setWidth(this.width - verticalScrollBar.getWidth());
                }
                widget.setHeight(itemHeight);
                componentY += widget.getHeight();
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
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) {
        if (visible) {
            renderX = getCalculatedX();
            renderY = getCalculatedY();
            renderer.enableClipping(renderX, renderY, renderX + width, renderY + height);
            renderBackground(renderer);
            renderSelectedBackground(renderer);
            renderHoveredBackground(renderer, entryAtMousePointer(mouseX, mouseY));
            super.render(renderer, mouseX, mouseY, delta);
            renderBorder(renderer);
            renderer.disableClipping();
        }
    }


    private void renderBackground(BaseRenderer renderer) {
        if (listHasBackground) {
            int componentWidth = getWidth();
            int componentHeight = getHeight();
            renderer.fill(renderX, getCalculatedY(), 
                    renderX + componentWidth, renderY + componentHeight, 
                    listBackgroundColor);
        }
    }


    private void renderSelectedBackground(BaseRenderer renderer) {
        if (selected != null) {
            int componentWidth = componentVisibleWidth();
            int componentHeight = selected.getHeight() + 1;
            int componentY = renderY + selected.getY() - 1 - (int)getVerticalScrollAmount();
            renderer.fill(renderX, componentY, 
                    renderX + componentWidth, componentY + componentHeight, 
                    CWidget.getPalette().SELECTED_BACKGROUND);
        }
    }


    private void renderHoveredBackground(BaseRenderer renderer, CListBoxEntry hoveredWidget) {
        if (hoveredWidget != null) {
            int componentWidth = componentVisibleWidth();
            int componentHeight = hoveredWidget.getHeight();
            int componentY = hoveredWidget.getCalculatedY();
            renderer.fill(renderX + 1, componentY, 
                    renderX + componentWidth, componentY + componentHeight - 1, 
                    CWidget.getPalette().HOVERED_BACKGROUND);
            renderer.drawRectangle(renderX + 1, componentY, 
                    renderX + componentWidth, componentY + componentHeight - 1, 
                    CWidget.getPalette().HOVERED_BORDER);
        }
    }


    private void renderBorder(BaseRenderer renderer) {
        if (listHasBorder) {
            renderer.drawRectangle(renderX, renderY, 
                    renderX + width, renderY + height, 
                    CWidget.getPalette().SELECTED_BACKGROUND);
        }
    }


    public int componentVisibleWidth() {
        return getWidth() - verticalScrollBarWidthUsed();
    }


    // === Mouse ===


    private boolean isMouseOver(int mouseX, int mouseY) {
        return (this.visible && 
                mouseX >= this.getCalculatedX() && 
                mouseY >= this.getCalculatedY() && 
                mouseX < (this.getCalculatedX() + this.getWidth()) && 
                mouseY < (this.getCalculatedY() + this.getHeight()));
    }


    @Override
    public boolean mousePressed(CMouseEvent mouse) {
        super.mousePressed(mouse);
        if (!isMouseOver(mouse)) return false;
        if (mouse.getX() <= getWidth() - verticalScrollBarWidthUsed()) {
            CListBoxEntry entry = entryAtMousePointer((int)mouse.getX(), (int)mouse.getY());
            if (entry != null) {
                setSelected(entry);
            }
            if (this.onClick != null) {
                this.onClick.onClick(entry);
            }
            return true;
        }
        return false;
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