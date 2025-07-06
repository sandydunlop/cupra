package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.CupraException;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.Symbol;
import io.github.sandydunlop.cupra.common.util.DepthLimit;
import io.github.sandydunlop.cupra.platform.PlatformServices;


/**
 * CDropdownListBox is a widget that provides a dropdown list.
 * 
 * <p>
 * Features:
 * <ul>
 *   <li>Dropdown button to display a list of selectable entries.</li>
 *   <li>Callback interfaces for selection changes.</li>
 *   <li>Customizable width and automatic layout of child components.</li>
 * </ul>
 *
 * @author Sandy Dunlop
 */
public class CDropdownListBox extends CWidget {
    protected CTextBox textbox= null;
    protected CSymbolButton button = null;
    protected CListBox listbox = null;
    protected CSelectionChangedAction onSelectionChanged;
    protected int buttonWidth = 15;


    /**
     * CDropdownListBox is a widget that provides a dropdown list.
     * <p>
     * This component allows users to either enter text directly or select an option from a dropdown list.
     * It consists of a text box, a button to toggle the dropdown, and a list box for selection.
     * </p>
     *
     * <ul>
     *   <li>Clicking the dropdown button toggles the visibility of the list box.</li>
     *   <li>Selecting an item from the list box updates the text box and triggers an optional {@code onSelectionChanged} callback.</li>
     *   <li>The dropdown list is overlaid using {@code PlatformServices} when visible.</li>
     * </ul>
     *
     * @param parent The parent container to which this widget will be added.
     */
    public CDropdownListBox(CContainer parent) {
        super(parent);
        this.setExpandable(false);
        this.setPadding(0);
        if (parent != null) {
            parent.add(this);
        }

        textbox = new CTextBox(null, "");
        textbox.onMouseClicked(action -> {
            toggleListVisibility();
        });
        textbox.setEditable(false);
        button = new CSymbolButton(null, Symbol.DOWN, click ->{
            toggleListVisibility();
        });
        listbox = new CListBox(null, selection ->{
            textbox.setText(selection.getTitle());
            listbox.setVisible(false);
            PlatformServices.getInstance().setOverlaid(null, null);
            if (this.onSelectionChanged != null) {
                this.onSelectionChanged.onSelectionChanged(selection);
            }
        });
        listbox.setVisible(false);
        listbox.setFont(font.duplicate());
    }


    private void toggleListVisibility() {
        if (listbox.isVisible()) {
            listbox.setVisible(false);
            PlatformServices.getInstance().setOverlaid(null, null);
        } else {
            setListboxParams();
            listbox.setVisible(true);
            PlatformServices.getInstance().setOverlaid(listbox, this);
        }
    }
    /**
     * Sets the width of the dropdown text box component.
     * <p>
     * This method updates the width of the main component as well as its internal
     * textbox and listbox elements. The textbox width is set to the specified width
     * minus the width of the button, while the listbox width matches the specified width.
     *
     * @param width the total width to set for the dropdown text box, in pixels
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        textbox.setWidth(width - buttonWidth);
        listbox.setWidth(width);
    }


    public void setSelectedIndex(int index) {
        listbox.setSelectedIndex(index);
    }   


    public int getSelectedIndex() {
        return listbox.getIndexOf(listbox.getSelected());
    }


    public CListBoxEntry getSelected() {
        return listbox.getSelected();
    }


    /**
     * Sets the specified {@link CListBoxEntry} as the selected entry in the list box.
     *
     * @param entry the {@code CListBoxEntry} to be selected
     */
    public void setSelected(CListBoxEntry entry) {
        listbox.setSelected(entry);
    }


    /**
     * Adds a {@link CListBoxEntry} to the list box and updates its height.
     *
     * @param entry the entry to be added to the list box
     */
    public void add(CListBoxEntry entry) {
        listbox.add(entry);
        listbox.setHeight(listboxHeight());
    }


    /**
     * Lays out the components of the dropdown text box, adjusting their sizes and positions
     * based on the current font and widget dimensions. This method recalculates the height
     * using the loaded font and padding, updates the width and height of the textbox and button,
     * and triggers a layout update for the listbox if the font cache is invalidated.
     * Also sets the parameters for the listbox.
     */
    @Override
    public void layout() {
        BitmapFont bmf = BitmapFontFactory.load(font);
        this.height = bmf.getHeight() + (CTextBox.TEXT_PADDING*2);
        if (CWidget.isFontCacheInvalidated()) {
            listbox.layout();
        }
        this.textbox.setWidth(this.width - buttonWidth);
        this.textbox.setHeight(this.height);
        this.button.setWidth(buttonWidth);
        this.button.setHeight(this.height);
        setListboxParams();
    }


    /**
     * Configures the parameters of the listbox component.
     * <p>
     * Sets the height and width of the listbox to match the calculated height and the current width,
     * and resets the scroll amount to the top.
     * </p>
     */
    private void setListboxParams() {
        // Listbox coordinates are absolute
        this.listbox.setHeight(listboxHeight());
        this.listbox.setWidth(this.width);
        this.listbox.setScrollAmount(0.0);
    }


    /**
     * Calculates the height of the listbox based on the number of entries to display.
     * Displays up to 4 entries; if there are fewer than 4 entries, displays only those.
     * The height is computed as the item height multiplied by the number of entries displayed, plus 2 pixels for padding or border.
     *
     * @return the calculated height of the listbox in pixels
     */
    private int listboxHeight() {
        int entriesDisplayed = listbox.content.contents.size() > 4 ? 4 : listbox.content.contents.size();
        return (listbox.getItemHeight() * entriesDisplayed) + 2;
    }


    /**
     * Positions the child components (textbox, button, and listbox) relative to the parent widget.
     * <p>
     * The method calculates the absolute X and Y coordinates of the parent and sets the positions of:
     * <ul>
     *   <li>textbox: aligned with the parent's top-left corner</li>
     *   <li>button: aligned to the right edge of the parent</li>
     *   <li>listbox: positioned directly below the parent</li>
     * </ul>
     */
    private void positionChildren() {
        int absoluteX = getCalculatedX();
        int absoluteY = getCalculatedY();
        textbox.setX(absoluteX);
        textbox.setY(absoluteY);
        button.setX(absoluteX + width - buttonWidth);
        button.setY(absoluteY);
        listbox.setX(absoluteX);
        listbox.setY(absoluteY + height);
    }


    /**
     * Renders the dropdown text box and its child components.
     *
     * <p>This method first calls the superclass's render method, then positions and renders
     * the child components: textbox, button, and listbox. If debug mode is enabled (i.e., 
     * {@code getDebug()} returns a non-zero value), it draws a debug rectangle around the 
     * component using the specified debug color.</p>
     *
     * @param renderer the renderer used to draw the component
     * @param mouseX the current X position of the mouse cursor
     * @param mouseY the current Y position of the mouse cursor
     * @param delta the time elapsed since the last frame, in seconds
     * @throws CupraException if an error occurs during rendering
     */
    @Override
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) throws CupraException {
        super.render(renderer, mouseX, mouseY, delta);
        positionChildren();
        textbox.render(renderer, mouseX, mouseY, delta);
        button.render(renderer, mouseX, mouseY, delta);
        listbox.render(renderer, mouseX, mouseY, delta);
        if (this.getDebug() != 0){
            renderer.drawRectangle(getCalculatedX(), getCalculatedY(), getCalculatedX()+getWidth(), getCalculatedY()+getHeight(), this.getDebug());
        }
    }


    //
    // === Mouse & Keyboard ===
    //


    /**
     * Determines whether the mouse pointer is currently over this dropdown text box or its associated listbox.
     *
     * @param mouse the mouse event containing the current mouse position
     * @return {@code true} if the mouse is over this component or the visible listbox; {@code false} otherwise
     */
    @Override
    public boolean isMouseOver(CMouseEvent mouse) {
        if (super.isMouseOver(mouse)) {
            return true;
        }
        return listbox.isVisible() && listbox.isMouseOver(mouse);
    }


	@Override
	public CWidget hoveredWidget(CMouseEvent mouse, DepthLimit depth) {
        if (depth != DepthLimit.COMPONENT) {
            return this;
        }
        if (listbox.isVisible() && listbox.isMouseOver(mouse)){
            return listbox;
        }else if (textbox.isMouseOver(mouse)){
            return textbox;
        }else if (button.isMouseOver(mouse)){
            return button;
        }
        return null;
    }


    public interface CSelectionChangedAction {
        void onSelectionChanged(CListBoxEntry selection);
    }


    public void onSelectionChanged(CSelectionChangedAction action) {
        this.onSelectionChanged = action;
    }
}
