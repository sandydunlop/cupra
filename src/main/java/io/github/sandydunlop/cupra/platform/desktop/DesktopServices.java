package io.github.sandydunlop.cupra.platform.desktop;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;
import javax.swing.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import dorkbox.notify.Notify;
import dorkbox.notify.Position;
import dorkbox.notify.Theme;

import io.github.sandydunlop.cupra.common.CupraScreen;
import io.github.sandydunlop.cupra.common.logging.Logger;
import io.github.sandydunlop.cupra.common.widgets.CWidget;
import io.github.sandydunlop.cupra.common.logging.LogManager;
import io.github.sandydunlop.cupra.platform.PlatformServices;


public class DesktopServices extends PlatformServices {
	private static final Logger LOGGER = LogManager.getLogger("Cupra");
    HashMap<CupraScreen,CupraWindow> windows = new HashMap<>();
    WindowManager windowManager = null;
    KeyboardInputHandler keyboardManager = null;
    private boolean cursorVisible = true;
    
    private DesktopServices() {
        // Nothing to do here, but required for singleton pattern
        System.setProperty("apple.awt.application.appearance", "system");
        System.setProperty( "apple.laf.useScreenMenuBar", "true" );
        getKeyboardManager().listen();
        flashingCursor();
    }


    public static synchronized DesktopServices getInstance() {
        if (instance == null) {
            instance = new DesktopServices(); // Lazy initialization
            LOGGER.debug("Created DesktopServices instance");
        }
        return (DesktopServices)instance;
    }


    private void flashingCursor() {
        Timer timer = new Timer(500, e -> {
            CupraWindow activeWindow = getWindowManager().getActiveWindow();
            if (activeWindow != null && activeWindow.getScreen() != null) {
                CWidget focusedWidget = activeWindow.getScreen().getFocus();
                cursorVisible = !cursorVisible;
                activeWindow.renderWidget(focusedWidget);
            }
        });
        timer.start();
    }


    @Override
    public boolean isCursorVisible() {
        return cursorVisible;
    }


    //
    // === Window Management ===
    //


    public WindowManager getWindowManager() {
        if (windowManager == null) {
            windowManager = new WindowManager();
        }
        return windowManager;
    }


    @Override
    public void open(CupraScreen screen) {
        getWindowManager().open(screen);
    }


    @Override
    public void close(CupraScreen screen) {
        getWindowManager().close(screen);
    }


    @Override
    public void setWindowSize(CupraScreen screen, int width, int height) {
        getWindowManager().setWindowSize(screen, width, height);
    }


    @Override
    public void setWindowTitle(CupraScreen screen, String title) {
        getWindowManager().setWindowTitle(screen, title);
    }


    //
    // === Keyboard ===
    //


    public KeyboardInputHandler getKeyboardManager() {
        if (keyboardManager == null) {
            keyboardManager = new KeyboardInputHandler();
        }
        return keyboardManager;
    }


    //
    // === Overriden methods from PlatformServices
    //

    
    @Override
    public void render(){
        render(null);
    }


    @Override
    public void render(CWidget widget){
        CupraWindow activeWindow = getWindowManager().getActiveWindow();
        if (activeWindow != null && activeWindow.getScreen() != null) {
            activeWindow.renderWidget(widget);
        }
    }


    @Override
    public Path getConfigDir() {
        return java.nio.file.Paths.get(System.getProperty("user.home"), ".config");
    }


    @Override
    public void openLinkInBrowser(String address) {
        try {
            Desktop.getDesktop().browse(new URI(address));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void showNotification(String message) {
        super.showNotification(message);
        String appName = "TODO: APP NAME & ICON";
        final java.util.concurrent.ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        try {
            Position position = isMacOS() ? Position.TOP_RIGHT : Position.BOTTOM_RIGHT;
            executor.scheduleAtFixedRate(() ->
                Notify.Companion.create()
                        .title(appName)
                        .text(message)
                        .theme(Theme.Companion.getDefaultDark())
                        .position(position)
                        .hideAfter(5000).show()
            , 0, 2, TimeUnit.SECONDS);

            // Schedule shutdown after a reasonable time (e.g., 5 seconds)
            executor.schedule(executor::shutdown, 5, TimeUnit.SECONDS);
        } finally {
            // Ensure shutdown in case of exception
            executor.shutdown();
        }
    }


    @Override
    public String getClipboardText() {
        String data = "";
        try {
            data = (String) Toolkit.getDefaultToolkit()
                        .getSystemClipboard().getData(DataFlavor.stringFlavor); 
        } catch (UnsupportedFlavorException | HeadlessException | IOException ignore) {
            // Don't complain
        }
        return data;
    }


    @Override
    public void copyTextToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}
