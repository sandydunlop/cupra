package io.github.sandydunlop.cupra.platform.desktop;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;

import io.github.sandydunlop.cupra.common.CupraScreen;
import io.github.sandydunlop.cupra.common.logging.Logger;
import io.github.sandydunlop.cupra.common.logging.LogManager;


public class WindowManager {
	private static final Logger LOGGER = LogManager.getLogger("Cupra");
    HashMap<CupraScreen,CupraWindow> screensToWindows = new HashMap<>();
    HashMap<CupraWindow,CupraScreen> windowsToScreens = new HashMap<>();
    HashMap<JFrame,CupraWindow> framesToWindows = new HashMap<>();
    WindowListener listener = null; 
    private JMenu windowMenu = null;
    private CupraWindow activeWindow = null;


    public WindowManager () {
        listener = new WindowAdapter() {
            List<Window> windows = new ArrayList<>();

            @Override
            public void windowOpened(WindowEvent e) {
                LOGGER.debug("windowOpened");
                windows.add(e.getWindow());
            }

            @Override
            public void windowClosing(WindowEvent e) {
                LOGGER.debug("windowClosing");
                if (windows.size() > 1) {
                    windows.remove(e.getWindow());
                    e.getWindow().dispose();
                }
            }

            @Override
            public void windowActivated(WindowEvent e) {
                LOGGER.debug("windowActivated");
                JFrame frame = (JFrame) e.getWindow();
                activeWindow = framesToWindows.get(frame);
                DesktopServices.getInstance().getKeyboardManager().setWindow(activeWindow);
            }
        };
        LOGGER.debug("Created WindowManager instance");
    }


    public CupraWindow getActiveWindow() {
        return activeWindow;
    }


    public void open(CupraScreen screen) {
        if (screen != null && screensToWindows.containsKey(screen)) {
            CupraWindow desktopScreen = screensToWindows.get(screen);
            if (desktopScreen != null) {
                desktopScreen.getFrame().setVisible(true);
                desktopScreen.getFrame().toFront();
                desktopScreen.getFrame().requestFocus();
                desktopScreen.getFrame().repaint();
                desktopScreen.getFrame().requestFocusInWindow();
                return;
            }
        }
        CupraWindow window = new CupraWindow(screen);
        JFrame frame = window.getFrame();
        screensToWindows.put(screen, window);
        windowsToScreens.put(window, screen);
        framesToWindows.put(frame, window);
        frame.addWindowListener(listener);

        // JMenuItem menuItem = new JMenuItem(screen.getTitle());
        // windowMenu.add(menuItem);
        // menuItem.addActionListener(e -> {
        //     desktopScreen.getFrame().setVisible(true);
        //     desktopScreen.getFrame().toFront();
        //     desktopScreen.getFrame().requestFocus();
        //     desktopScreen.getFrame().repaint();
        //     //desktopScreen.getFrame().requestFocusInWindow();
        // });
    }


    public CupraWindow getWindow(CupraScreen screen) {
        if (screen != null && screensToWindows.containsKey(screen)) {
            return screensToWindows.get(screen);
        }
        LOGGER.debug("Request for non-extant CupraWindow");
        LOGGER.debug("{}", Arrays.asList(Thread.currentThread().getStackTrace()));
        return null;
    }


    public void close(CupraScreen screen) {
        if (screen != null && screensToWindows.containsKey(screen)) {
            CupraWindow window = screensToWindows.get(screen);
            if (window != null) {
                window.getFrame().dispose();
            }
            framesToWindows.remove(screensToWindows.get(screen).getFrame());
            screensToWindows.remove(screen);
            windowsToScreens.remove(window);
            // Remove the corresponding menu item from the windowMenu
            // for (int i = 0; i < windowMenu.getItemCount(); i++) {
            //     JMenuItem item = windowMenu.getItem(i);
            //     if (item != null && item.getText().equals(screen.getTitle())) {
            //         windowMenu.remove(i);
            //         break;
            //     }
            // }
        }
    }


    public void setWindowSize(CupraScreen screen, int width, int height) {
        CupraWindow window = getWindow(screen);
        if (window != null) {
            window.setWindowSize(width, height);
        }
    }


    public void setWindowTitle(CupraScreen screen, String title) {
        CupraWindow window = getWindow(screen);
        if (window != null) {
            window.setWindowTitle(title);
        }
    }


    public JMenu getWindowMenu() {
        if (windowMenu == null) {
            windowMenu = new JMenu("Window");
            windowMenu.setMnemonic('W');
            //windowMenu.setToolTipText("Open and close windows");
        }
        return windowMenu;
    }
}
