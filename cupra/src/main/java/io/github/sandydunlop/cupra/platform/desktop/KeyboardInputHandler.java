package io.github.sandydunlop.cupra.platform.desktop;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import io.github.sandydunlop.cupra.common.events.CKeyEvent;
import io.github.sandydunlop.cupra.common.logging.LogManager;
import io.github.sandydunlop.cupra.common.logging.Logger;


public class KeyboardInputHandler {
	private static final Logger LOGGER = LogManager.getLogger("Cupra");
    private CupraWindow window;

    public void listen() {
        // Register a global key press listener
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (window != null && window.getScreen() != null) {
                        //Modifiers 256 = cmd, 128=ctrl, 64=shift

                        // int charCode = e.getKeyChar();
                        // LOGGER.debug("NORMAL CHAR: c={} (int={}) (mod={})", e.getKeyChar(), (int)e.getKeyChar(), e.getModifiersEx());
                        // LOGGER.debug("SPECIAL KEY: code={}, ext={}", e.getKeyCode(), e.getExtendedKeyCode());

                        CKeyEvent k = new CKeyEvent(e.getKeyCode(), e.getExtendedKeyCode(), e.getModifiersEx(), 0, e.getKeyChar());
                        if (k.isActionKey()) {
                            window.getScreen().keyPressed(k);
                        } else {
                            window.getScreen().charTyped(k);
                        }
                    }
                }
                return false;
            }
        });
    }


    public void setWindow(CupraWindow window) {
        LOGGER.debug("KeyboardManager set active window");
        this.window = window;
    }
}
