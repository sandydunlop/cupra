package io.github.sandydunlop.cupra.common.widgets;

import java.util.ArrayList;
import java.util.List;

import io.github.sandydunlop.cupra.common.events.CListBoxSelectionChangedEvent;
import io.github.sandydunlop.cupra.common.events.CListBoxSelectionChangedListener;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.DepthLimit;
import io.github.sandydunlop.cupra.common.util.MousePointer;


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

    List<CListBoxColumn> columns = new ArrayList<>();
    private int headerHeight = 0;
    private double mousePressedX = 0;
    private int resizingColumn = -1;
    private int columnOriginalWidth = 0;


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
        content.setId("content");
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
    public void add(Object... o) {
        if (o== null || o.length == 0) {
            LOGGER.error("CListBox.add: expected at least one non-null parameter");
            return;
        }
        if (o[0] instanceof CListBoxEntry) {
            LOGGER.error("How did we arrive here?");
            return;
        }
        CListBoxEntry entry = new CListBoxEntry(null, null);
        entry.setValues(o);
        add(entry);
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


    public void addColumn(String name, int width) {
        CListBoxColumn column = new CListBoxColumn(name, width);
        columns.add(column);
        headerHeight = 20; //TODO: Make this dynamic
        content.setTopOffset(headerHeightUsed());
    }


    //
    // === Layout ===
    //

    
    @Override
    public int getDefaultHeight(){
        return 80;
    }


    @Override
    public int getDefaultWidth(){
        return 200;
    }


    @Override
    public void setWidth(int width) {
        super.setWidth(width);
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
        updateContentPosition();
        super.layout();
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
            content.setTopOffset(headerHeightUsed());
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
            if(columns!=null && !columns.isEmpty()){
                int w = 0;
                for (int i=0; i<columns.size(); i++){
                    w += columns.get(i).getWidth();
                }
                setContentWidth(w);
            }
            setContentHeight(content.contents().size() * itemHeight); //TODO: plus header height?
            // if (horizontalScrollingEnabled) {
            //     setContentWidth(widest);
            // }
            double sa = getVerticalScrollAmount(); // This puts content at the correct position
            setVerticalScrollAmount(sa);
            super.layout();
        }
    }


    //
    // === Rendering ===
    //

    
    @Override
    public void render(BaseRenderer renderer, CMouseEvent mouse) {
        if (visible) {
            renderX = getCalculatedX();
            renderY = getCalculatedY();
            renderer.enableClipping(renderX, renderY, renderX + width, renderY + height);
            renderBackground(renderer);
            renderSelectedBackground(renderer);
            if (this == mouse.getWidgetUnderMouse()) {
                renderHoveredBackground(renderer, entryAtMousePointer(mouse));
            }
            renderHeaders(renderer);
            super.render(renderer, mouse);
            renderBorder(renderer);
            renderer.disableClipping();
        }
    }


    private void renderHeaders(BaseRenderer renderer) {
        int normalFontSize = font.getSize();
		font.setColor(CWidget.getPalette().REGULAR_TEXT);
        font.setSize(normalFontSize - 3);
		renderer.setFont(font);
        BitmapFont bmf = BitmapFontFactory.load(font);
        int componentX = renderX - (int)horizontalScrollAmount;
        renderer.enableClipping(renderX, renderY, 
                renderX + width - verticalScrollBarWidthUsed(), renderY + height - horizontalScrollBarHeightUsed());
        for (int i=0; i<columns.size(); i++) {
            CListBoxColumn column = columns.get(i);
            renderHeader(renderer, bmf, column, componentX);
            componentX += column.getWidth();
            renderer.drawVerticalLine(componentX, renderY + headerHeight, 
                    renderY + height - horizontalScrollBarHeightUsed(), 
                    CWidget.getPalette().INPUT_SEPARATOR);
        }
        renderer.fill(componentX, renderY, 
                renderX + width - verticalScrollBarWidthUsed(), renderY + headerHeight, 
                CWidget.getPalette().INPUT_SEPARATOR);
        renderer.disableClipping();
        font.setSize(normalFontSize);
    }


    private void renderHeader(BaseRenderer renderer, BitmapFont bmf, CListBoxColumn column, int componentX) {
        int componentWidth = column.getWidth();
        int componentHeight = headerHeightUsed();
        renderer.fill(componentX, renderY, 
                componentX + componentWidth, renderY + componentHeight, 
                CWidget.getPalette().REGULAR_BACKGROUND);
        renderer.drawRectangle(componentX, renderY, 
                componentX + componentWidth, renderY + componentHeight, 
                CWidget.getPalette().HOVERED_BORDER);
        String text = column.getName();
        int textWidth = bmf.stringWidth(text);
        int textHeight = bmf.getHeight();
        int textX = (componentX + (componentWidth/2)) - (textWidth/2);
        int textY = (renderY + (componentHeight/2)) - (textHeight/2);
        renderer.drawText(text, textX, textY);
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
            int componentY = renderY + headerHeightUsed() + selected.getY() - 1 - (int)getVerticalScrollAmount();
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


    protected int headerHeightUsed() {
        if (columns.isEmpty()) return 0;
        return headerHeight;
    }


    // === Mouse ===


    protected CListBoxEntry entryAtMousePointer(CMouseEvent mouse) {
        if (!isMouseOver(mouse)) return null;
        if (itemHeight==0) return null;
        int yWithinBox = (int)mouse.getY() - renderY - headerHeightUsed();
        if (yWithinBox < 0) return null;
        int yWithinList = yWithinBox + (int)verticalScrollAmount;
        int posWithinList = yWithinList / itemHeight;
        return getEntry(posWithinList);
    }


	@Override
	public CWidget hoveredWidget(CMouseEvent mouse, DepthLimit depth) {
        if (isMouseOverColumnHeadings(mouse) && mouseOverColumnSeparator((int)mouse.getX()) > -1) {
            mousePointer = MousePointer.MOVE_LEFT_RIGHT;
        } else {
            mousePointer = MousePointer.ARROW;
        }
        return super.hoveredWidget(mouse, depth);
    }


    private boolean isMouseOverColumnHeadings(CMouseEvent mouse) {
        if (mouse.getY() <= renderY + headerHeightUsed() && 
                mouse.getX() < renderX + width - verticalScrollBarWidthUsed()){
            return true;
        }
        return false;
    }


    private int mouseOverColumnSeparator(int mouseX) {
        int componentX = renderX - (int)horizontalScrollAmount;
        if (columns.size() < 2) return -1;
        for (int i=0; i<columns.size(); i++) {
            componentX += columns.get(i).getWidth();
            if (mouseX > componentX -2 && mouseX < componentX + 2) {
                return i;
            }
        }
        return -1;
    }
	
	
    @Override
    public boolean mousePressed(CMouseEvent mouse) {
        super.mousePressed(mouse);
        if (!isMouseOver(mouse)) return false;
        if (mouse.getX() <= renderX + getWidth() - verticalScrollBarWidthUsed()) {
            if (isMouseOverColumnHeadings(mouse)) {
                resizingColumn = mouseOverColumnSeparator((int)mouse.getX());
                if (resizingColumn > -1){
                    mousePressedX = mouse.getX();
                    columnOriginalWidth = columns.get(resizingColumn).getWidth();
                    return true;
                }
                // Click heading to order by could go here
                return false;
            }else{
                CListBoxEntry entry = entryAtMousePointer(mouse);
                if (entry != null) {
                    setSelected(entry);
                }
                if (this.onClick != null) {
                    this.onClick.onClick(entry);
                }
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean mouseDragged(CMouseEvent mouse) {
        if (mouse.getButton() != CMouseEvent.PRIMARY_BUTTON || resizingColumn == -1) return false;
        double dragDistance = mouse.getX() - mousePressedX;
        CListBoxColumn column = columns.get(resizingColumn);
        column.setWidth((int)(columnOriginalWidth + dragDistance));
        //LOGGER.debug("Resizing column {}: {} ({})", dragDistance, column.getWidth(), mousePressedX);
        return true;
    }


    @Override
    public boolean mouseReleased(CMouseEvent mouse) {
        resizingColumn = -1;
        return true;
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