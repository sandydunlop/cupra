package io.github.sandydunlop.cupra.common;

import io.github.sandydunlop.cupra.common.events.CKeyEvent;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.fonts.FontSpec;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.DepthLimit;
import io.github.sandydunlop.cupra.common.widgets.CContainer;
import io.github.sandydunlop.cupra.common.widgets.CWidget;
import io.github.sandydunlop.cupra.platform.PlatformServices;


public class CupraScreen extends CContainer {
    private boolean isInitialized;
    private boolean hasCompletedMain = false;
    private int displayPadding;
    private CWidget focusedWidget = null;
    protected CWidget overlaidWidget = null;
    protected CWidget overlaidParent = null;
    private CupraApp app = null;
    private CupraScreen parentScreen = null;
    private int suggestedWidth = 0;
    private int suggestedHeight = 0;
    private String title = "";


    protected CupraScreen(){
        this.isInitialized = false;
        this.displayPadding = 10;
        FontSpec font = new FontSpec();
        font.setName("Rubik");
        font.setColor(CWidget.getPalette().REGULAR_TEXT);
        this.setFont(font);
    }


    protected void suggestSize(int width, int height) {
        this.suggestedWidth = width;
        this.suggestedHeight = height;
    }


    public int getSuggestedWidth() {
        return this.suggestedWidth;
    }


    public int getSuggestedHeight() {
        return this.suggestedHeight;
    }


    public void setTitle(String title) {
        this.title = title;
        PlatformServices.getInstance().setWindowTitle(this, title);
    }


    public String getTitle() {
        return this.title;
    }   


    public void setApp(CupraApp app) {
        this.app = app;
    }


    public CupraApp getApp() {
        return app;
    }


    public void setParentScreen(CupraScreen parentScreen) {
        this.parentScreen = parentScreen;
    }


    public CupraScreen getParentScreen() {
        return parentScreen;
    }

    public void display() {
        PlatformServices.getInstance().open(this);
    }


    public static FontSpec defaultFontOptions(){
        FontSpec fontOptions = new FontSpec();
        fontOptions.setName("Rubik");
        fontOptions.setColor(CWidget.getPalette().REGULAR_TEXT);
        return fontOptions;
    }


    public boolean isInitialized(){
        return this.isInitialized;
    }


    public void recalculate(){
        this.isInitialized = false;
    }


    public boolean hasCompletedMain() {
        return hasCompletedMain;
    }


    public void setHasCompletedMain(boolean hasCompletedMain) {
        this.hasCompletedMain = hasCompletedMain;
    }


    public int getDisplayPadding(){
        return displayPadding;
    }

    public void setDisplayPadding(int padding){
        displayPadding = padding;
    }


    public void init(){
        layout();
        CWidget.setFontCacheInvalidated(false);
        this.isInitialized = true;
    }

    
    public void close(){
        if (parentScreen != null) {
            PlatformServices.getInstance().getApp().setScreen(parentScreen);
        }
        this.onClose();
        PlatformServices.getInstance().close(this);
        setApp(null);
        setParentScreen(null);
        hideOverlaidWidget();
        setFocus(null);
    }


    @Override
    public void charTyped(CKeyEvent k) {
        if (focusedWidget != null) {
            focusedWidget.charTyped(k);
        } else {
            onCharTyped(k);
        }
	}


    @Override
	public void keyPressed(CKeyEvent k) {
        if (k.getKeyCode() == CKeyEvent.KEY_ESCAPE) {
            close();
            return;
        }
        if (focusedWidget != null) {
            focusedWidget.keyPressed(k);
        } else {
            onKeyPressed(k);
        }
	}


    public void onCharTyped(CKeyEvent e) {
        // This is to be overridden by widgets that need it
    }


    public void onKeyPressed(CKeyEvent k) {
        // This is to be overridden by widgets that need it
    }


    public void setFocus(CWidget widget) {
        if (this.focusedWidget != null) {
            this.focusedWidget.setFocused(false);
        }
        this.focusedWidget = widget;
        if (widget != null) {
            this.focusedWidget.setFocused(true);
        }
        PlatformServices.getInstance().render();
    }


    public CWidget getFocus() {
        return this.focusedWidget;
    }


    public void setOverlaid(CWidget widget, CWidget parent) {
        this.overlaidWidget = widget;
        this.overlaidParent = parent;
    }


    public CWidget getOverlaid() {
        return this.overlaidWidget;
    }


    public void hideOverlaidWidget() {
        if (this.overlaidWidget != null) {
            this.overlaidWidget.setVisible(false);
            this.overlaidWidget = null;
            this.overlaidParent = null;
        }
    }


    public boolean isMouseOverOverlaid(CMouseEvent mouse) {
        if (overlaidWidget != null && overlaidWidget.isMouseOver(mouse)) {
            return true;
        } else {
            return (overlaidParent != null && overlaidParent.isMouseOver(mouse));
        }
    }


    @Override
    public CWidget hoveredWidget(CMouseEvent mouse, DepthLimit depth){
        if (getOverlaid()  != null && getOverlaid() .isMouseOver(mouse)) {
            return getOverlaid();
        }
        return super.hoveredWidget(mouse, depth);
    }


    public void onShow() {
        // To be overridden
    }


    public void onClose() {
        // To be overridden
    }

    
    @Override
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) throws CupraException {
        super.render(renderer, mouseX, mouseY, delta);
        if (overlaidWidget != null){
            overlaidWidget.render(renderer, mouseX, mouseY, delta);
        }
    }
}
