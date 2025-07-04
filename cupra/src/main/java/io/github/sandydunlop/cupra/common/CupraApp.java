package io.github.sandydunlop.cupra.common;

import io.github.sandydunlop.cupra.platform.PlatformServices;


public abstract class CupraApp {
    CupraScreen currentScreen;
    int width;
    int height;
    int x;
    int y;

    public abstract String getName();

    protected CupraApp() {
        PlatformServices.getInstance().setApp(this);
    }

    public void setX(int x) {
        this.x = x;
    }


    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }


    public void setHeight(int height) {
        this.height = height;
    }


    public CupraScreen getScreen() {
        return currentScreen;
    }

    public void setScreen(CupraScreen screen) {
        if (screen != currentScreen) {
            PlatformServices.getInstance().screenChanged(screen);
        }
        currentScreen = screen;
    }

    public void openScreen(CupraScreen screen) {
        openScreen(null, screen);
    }

    public void openScreen(CupraScreen parentScreen, CupraScreen screen) {
        width = PlatformServices.getInstance().getWidth();
        height = PlatformServices.getInstance().getHeight();
        screen.setApp(this);
        screen.setParentScreen(parentScreen);
        screen.setFocus(null);
        setScreen(screen);
        screen.onShow();
        PlatformServices.getInstance().open(screen);
    }

    public void onShow(){
        // To be overridden
    }

    public abstract void run();

	public static void main(String[] args) {
        // To be overridden
    }
}
