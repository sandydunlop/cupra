package io.github.sandydunlop.cupra.platform.desktop;

import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.widgets.CWidget;


/**
 * An abstract base class for handling mouse input events on widgets.
 * <p>
 * Subclasses should implement the methods to define custom behavior for
 * mouse press, scroll, move, and click events.
 * </p>
 */
public abstract class AbstractMouseInputHandler {
    private AbstractMouseInputHandler() {
    }
    /**
     * Invoked when a mouse button has been pressed on a widget
     * @param widget The widget the event pertains to
     * @param mouse Details of the mouse event
     * @return True if the event was handled
     */
    public abstract boolean mousePressed(CWidget widget, CMouseEvent mouse);

    /**
     * Invoked when a mouse wheel is scrolled over a widget
     * @param widget The widget the event pertains to
     * @param mouse Details of the mouse event
     * @return True if the event was handled
     */
    public abstract boolean mouseScrolled(CWidget widget, CMouseEvent mouse);

    /**
     * Invoked when the mouse pointer moves over a widget
     * @param widget The widget the event pertains to
     * @param mouse Details of the mouse event
     * @return True if the event was handled
     */
    public abstract boolean mouseMoved(CWidget widget, CMouseEvent mouse);

    /**
     * Invoked when the mouse button has been clicked on a widget
     * @param widget The widget the event pertains to
     * @param mouse Details of the mouse event
     * @return True if the event was handled
     */
    public abstract boolean mouseClicked(CWidget widget, CMouseEvent mouse);
}
