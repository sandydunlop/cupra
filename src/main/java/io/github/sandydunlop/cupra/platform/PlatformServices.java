package io.github.sandydunlop.cupra.platform;

import java.nio.file.Path;

import io.github.sandydunlop.cupra.common.CupraScreen;
import io.github.sandydunlop.cupra.common.logging.Logger;
import io.github.sandydunlop.cupra.common.logging.LogManager;
import io.github.sandydunlop.cupra.common.widgets.CWidget;
import io.github.sandydunlop.cupra.common.CupraApp;


public class PlatformServices {
	private static final Logger LOGGER = LogManager.getLogger("Newsfeed");
    protected static PlatformServices instance = null;
    protected CupraApp app = null;
    protected int width = 0;
    protected int height = 0;
    private boolean closeRequested = false;
    private boolean debugLogging = false;
    private boolean invertMouseScrolling = false;


    protected PlatformServices() {
    }


    public static synchronized PlatformServices getInstance() {
        if (instance == null) {
            LOGGER.error("getInstance() called on PlatformServices instead of derived class");
        }
        return instance;
    }


    public void setDebugLogging(boolean enable) {
        debugLogging = enable;
    }


    public boolean isDebugLogging() {
        return debugLogging;
    }


    public Path getConfigDir() {
        // To be overridden by platform-specific services
        return null;
    }


    public boolean isCursorVisible() {
        return false;        
    }


    public void setCloseRequested(boolean b) {
        closeRequested = b;
    }


    public boolean closeRequested() {
        return closeRequested;
    }
    
    
    public void open(CupraScreen screen) {
        // To be overridden
    }

    public void close(CupraScreen screen) {
        // To be overridden
    }


    public void openLinkInBrowser(String address) {
        // Overrriden by platform-specific services
    }


    public CupraApp getApp() {
        return this.app;
    }


    public void setApp(CupraApp app) {
        this.app = app;
    }


    public int getWidth() {
        return this.width;
    }


    public int getHeight() {
        return this.height;
    }


    public void setWindowTitle(CupraScreen screen, String title){
        // Overridden in DesktopServices
    }


    public void screenChanged(CupraScreen screen) {
        // Overridden in MinecraftServices
    }


    public void render(){
        // Overridden in DesktopServices
    }


    public void render(CWidget widget){
        // Overridden in DesktopServices
    }


    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }


    public void setOverlaid(CWidget widget, CWidget parent) {
        if (app.getScreen() != null) {
            app.getScreen().setOverlaid(widget, parent);
        }
    }


    public CWidget getOverlaid() {
        return app.getScreen().getOverlaid();
    }
    

    public void setWindowSize(CupraScreen screen, int width, int height) {
        // Overridden in DesktopServices
    }


    public void showNotification(String message) {
        LOGGER.debug("Showing notification: {}", message);
        // To be overridden by platform-specific services
    }


    public boolean isMacOS() {
        String osName = System.getProperty("os.name"); 
        return osName != null && osName.toLowerCase().indexOf("mac") == 0;
    }

 
    public String getClipboardText() {
        // To be overriden by platform-specific services
        return "";
    }

    
    public void copyTextToClipboard(String text) {
        // To be overriden by platform-specific services
    }


    // === Mouse ===


    public void setInvertMouseScrolling(boolean invert) {
        invertMouseScrolling = invert;
    }


    public boolean getInvertMouseScrolling() {
        return invertMouseScrolling;
    }
}
