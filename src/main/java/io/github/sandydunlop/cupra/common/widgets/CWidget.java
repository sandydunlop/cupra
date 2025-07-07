package io.github.sandydunlop.cupra.common.widgets;

import java.util.ArrayList;
import java.util.List;

import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.Dimension;
import io.github.sandydunlop.cupra.common.util.MousePointer;
import io.github.sandydunlop.cupra.common.util.DepthLimit;
import io.github.sandydunlop.cupra.common.CupraScreen;
import io.github.sandydunlop.cupra.common.CupraException;
import io.github.sandydunlop.cupra.common.events.CKeyEvent;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.fonts.FontSpec;
import io.github.sandydunlop.cupra.common.input.KeyboardInput;
import io.github.sandydunlop.cupra.common.input.MouseInput;
import io.github.sandydunlop.cupra.common.logging.LogManager;
import io.github.sandydunlop.cupra.common.logging.Logger;
import io.github.sandydunlop.cupra.common.palette.ColorPalette;


public abstract class CWidget implements KeyboardInput, MouseInput {
	protected static final Logger LOGGER = LogManager.getLogger("Cupra");
    private static ColorPalette palette = ColorPalette.Blueberry;
    private static int baseFontSize = 14;
    private static boolean cachedFontsInvalidated = false;
    private int debug = 0;
    protected CContainer parent = null;
    protected String id = null;
    protected FontSpec font;
    protected int x = 0;
    protected int y = 0;
    protected int width = 0;
    protected int height = 0;
    protected int maxWidth = 0;
    protected int maxHeight = 0;
    protected int color = 0;
	protected int padding = 0;
    protected boolean resizeToContents = false;
    protected boolean focusable = false;
    protected boolean focused = false;
    protected boolean expandable = false;
    protected boolean scrollable = false;
    protected boolean visible = true;
    protected boolean enabled = true;
    protected boolean usesCustomLayout = false;
    protected boolean mouseOverEffects = false;
    protected boolean hyperlink = false;
    protected String tooltip = null;
    protected List<CWidget> contents = new ArrayList<>();
    protected MousePointer mousePointer = MousePointer.ARROW;


    protected CWidget(CContainer parent) {
        this.parent = parent;
        this.font = CupraScreen.defaultFontOptions();
        if (parent != null){
            this.font.mergeFrom(parent.getFont());
        }
    }


    // === Properties ===


    public static void setBaseFontSize(int size) {
        baseFontSize = size;
    }


    public static int getBaseFontSize() {
        return baseFontSize;
    }


    public static void setFontCacheInvalidated(boolean invalidated) {
        cachedFontsInvalidated = invalidated;
    }


    public static boolean isFontCacheInvalidated() {
        return cachedFontsInvalidated;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }
    

    public boolean hasMosueOverEffects() {
        return mouseOverEffects;
    }


    public void setMouseOverEffects(boolean effects) {
        this.mouseOverEffects = effects;
    }


    public void setEnabled(boolean flag){
        enabled = flag;
    }


    public boolean isEnabled() {
        return enabled;
    }


    public static void setPalette(ColorPalette p) {
        palette = p;
    }


    public static ColorPalette getPalette() {
        return palette;
    }


    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }


    public String getTooltip() {
        return this.tooltip;
    }

    public CWidget getParent() {
        return this.parent;
    }


    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }


    public int getMaxWidth() {
        return this.maxWidth;
    }


    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }


    public int getMaxHeight() {
        return this.maxHeight;
    }


    public void setUsesCustomLayout(boolean uses) {
        this.usesCustomLayout = uses;
    }


    public boolean usesCustomLayout() {
        return this.usesCustomLayout;
    }


    public void setX(int x) {
        this.x = x;
    }


    public void setY(int y) {
        this.y = y;
    }


    public void setWidth(int width) {
        this.width = width;
        this.resizeToContents = false;
        // TODO: Separate width and height settings for resizing
    }


    public void setHeight(int height) {
        this.height = height;
        this.resizeToContents = false;
    }


    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }


    public boolean isExpandable() {
        return this.expandable;
    }


    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }


    public boolean isScrollable() {
        return this.scrollable;
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public int getCalculatedX() {
        if (parent != null){
            return parent.getCalculatedX() + x;
        }else{
            return x;
        }
    }


    public int getCalculatedY() {
        if (parent != null){
            return parent.getCalculatedY() + y;
        }else{
            return y;
        }
    }


    public int getHeight() {
        return height;
    }

    
    public int getWidth() {
        return width;
    }

    
    public int getRight() {
        return x + width;
    }


    public int getDefaultHeight() {
        return 0;
    }


    public int getDefaultWidth() {
        return 0;
    }


    public int getBottom() {
        return y+height;
    }


    public void setFocused(boolean focused) {
        this.focused = focused;
    }


    public CWidget setDebug(int color) {
        this.debug = color;
        return this;
    }


    public int getDebug() {
        return this.debug;
    }


	public void setPadding(int padding) {
		this.padding = padding;
	}
	

	public int getPadding() {
		return this.padding;
	}
    
    
    public void setIsHoveringOverHyperlink(boolean b) {
        this.hyperlink = b;
    }


    public boolean isHoveringOverHyperlink() {
        return this.hyperlink;
    }


    public void setResizeToContents(boolean on) {
        this.resizeToContents = on;
    }


    public boolean getResizeToContents(){
        return this.resizeToContents;
    }


    public void setMousePointer(MousePointer pointer) {
        this.mousePointer = pointer;
    }


    public MousePointer getMousePointer() {
        return this.mousePointer;
    }


    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }


    public boolean isFocusable() {
        return this.focusable;
    }


    public boolean isFocused() {
        return this.focused;
    }


    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    

    public boolean isVisible() {
        return this.visible;
    }

    
    // === Contents and Layout ===


    public int getCalculatedWidth() {
        if (CWidget.isFontCacheInvalidated()) {
            recalculateSize();
        }
        if (width > 0) {
            return width;
        } else if (getDefaultWidth() > 0) {
            return getDefaultWidth();
        } else {
            LOGGER.debug("getCalculatedWidth: Widget with unknown width. id: {}", id);
            return 0;
        }
    }


    public int getCalculatedHeight() {
        if (CWidget.isFontCacheInvalidated()) {
            recalculateSize();
        }
        if (height > 0) {
            return height;
        } else if (getDefaultHeight() > 0) {
            return getDefaultHeight();
        }else{
            LOGGER.debug("getCalculatedHeight: Widget with unknown height. id: {}", id);
            return 0;
        }
    }


    public void recalculateSize() {
        // To be overridden
    }


    protected void giveWidthHint(String text, int extra) {
        if (this.resizeToContents && this.font != null) {
            BitmapFont bmf = BitmapFontFactory.load(font);
            int stringWidth = bmf.stringWidth(text);
            this.width = stringWidth + extra;
        }
    }


    protected void giveHeightHint(String text, int extra) { //NOSONAR
        if (this.resizeToContents && this.font != null) {
            BitmapFont bmf = BitmapFontFactory.load(font);
            this.height = bmf.getHeight() + extra;
        }
    }
    

    public Dimension size(){
        return new Dimension(width, height, -1, -1);
    }


    public List<CWidget> contents() {
        return contents;
    }

    
    public void layout(){
        // This is overridden in subclasses
    }


    // === Mouse and Keyboard ===


    public boolean isMouseOver(CMouseEvent mouse) {
        return (this.visible && 
                mouse.getX() >= this.getCalculatedX() && 
                mouse.getY() >= this.getCalculatedY() && 
                mouse.getX() < (this.getCalculatedX() + this.getWidth()) && 
                mouse.getY() < (this.getCalculatedY() + this.getHeight()));
    }


    public boolean mouseReleased(CMouseEvent mouse) { //NOSONAR
        // This is to be overridden by widgets that need it
        return false;
    }

    
    public boolean mousePressed(CMouseEvent mouse) { //NOSONAR
        return false;
    }
   

    public boolean mouseClicked(CMouseEvent mouse) { //NOSONAR
        return false;
    }


    public boolean mouseDoubleClicked(CMouseEvent mouse) { //NOSONAR
        return false;
    }


    public boolean mouseTripleClicked(CMouseEvent mouse) { //NOSONAR
        return false;
    }


    public boolean mouseScrolled(CMouseEvent mouse) { //NOSONAR
        // This is to be overridden by widgets that need it
        return false;
    }


    public boolean mouseHovered(CMouseEvent mouse) { //NOSONAR
        // This is to be overridden by widgets that need it
        return false;
    }


    public boolean mouseDragged(CMouseEvent mouse) { //NOSONAR
        // This is to be overridden by widgets that need it
        return false;
    }


	public CWidget hoveredWidget(CMouseEvent mouse, DepthLimit depth) { //NOSONAR
        // This is to be overridden by widgets that need it
        return null;
    }


    public void charTyped(CKeyEvent e) {
        // This is to be overridden by widgets that need it
    }


    public void keyPressed(CKeyEvent k) {
        // This is to be overridden by widgets that need it
    }


    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) throws CupraException {
        // This is to be overridden by every widget
    }
}
