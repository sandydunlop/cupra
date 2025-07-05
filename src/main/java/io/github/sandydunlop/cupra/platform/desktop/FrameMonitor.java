package io.github.sandydunlop.cupra.platform.desktop;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.prefs.Preferences;
import io.github.sandydunlop.cupra.common.CupraScreen;

public class FrameMonitor {

    public static void registerFrame(JFrame frame, CupraScreen screen, String frameUniqueId,
                                   int defaultX, int defaultY, int defaultW, int defaultH) {
        Preferences prefs = Preferences.userRoot()
                                        .node(FrameMonitor.class.getSimpleName() + "-" + frameUniqueId);
        frame.setLocation(getFrameLocation(prefs, defaultX, defaultY));
        frame.setSize(getFrameSize(prefs, defaultW, defaultH));
        resizeCupraWindow(frame, screen);

        CoalescedEventUpdater updater = new CoalescedEventUpdater(400,
                () -> updatePref(frame, prefs));

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeCupraWindow(frame, screen);
                screen.layout();
                updater.update();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                updater.update();
            }
        });
    }


    private static void resizeCupraWindow(JFrame frame, CupraScreen screen) {
        Dimension size = frame.getSize();
        screen.setWidth((int)size.getWidth());
        screen.setHeight((int)size.getHeight() - 40);
    }


    private static void updatePref(JFrame frame, Preferences prefs) {
        Point location = frame.getLocation();
        prefs.putInt("x", location.x);
        prefs.putInt("y", location.y);
        Dimension size = frame.getSize();
        prefs.putInt("w", size.width);
        prefs.putInt("h", size.height);
    }


    private static Dimension getFrameSize(Preferences pref, int defaultW, int defaultH) {
        int w = pref.getInt("w", defaultW);
        int h = pref.getInt("h", defaultH);
        return new Dimension(w, h);
    }

    private static Point getFrameLocation(Preferences pref, int defaultX, int defaultY) {
        int x = pref.getInt("x", defaultX);
        int y = pref.getInt("y", defaultY);
        return new Point(x, y);
    }
}
