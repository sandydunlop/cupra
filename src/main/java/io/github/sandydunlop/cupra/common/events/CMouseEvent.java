package io.github.sandydunlop.cupra.common.events;

import io.github.sandydunlop.cupra.common.widgets.CWidget;

public class CMouseEvent {
    public static final int PRIMARY_BUTTON = 1;
    public static final int SECONDARY_BUTTON = 2;
    public static final int TERTIARY_BUTTON = 3;

    public static final int SHIFT_PRESSED_MASK = 1 << 6;
    public static final int CONTROL_PRESSED_MASK = 1 << 7;
    public static final int COMMAND_PRESSED_MASK = 1 << 8;
    public static final int ALT_PRESSED_MASK = 1 << 9;

    private double x = 0;
    private double y = 0;
    private double deltaX = 0;
    private double deltaY = 0;
    private int button = 0;
    private int clickCount = 0;
    private int modifiers = 0;
    private double horizontalAmount = 0;
    private double verticalAmount = 0;
    private CWidget widgetUnderMouse = null;


    public CMouseEvent() {

    }


    public boolean isShiftPressed() {
        return (modifiers & SHIFT_PRESSED_MASK) > 0;
    }


    public boolean isControlPressed() {
        return (modifiers & CONTROL_PRESSED_MASK) > 0;
    }


    public boolean isCommandPressed() {
        return (modifiers & COMMAND_PRESSED_MASK) > 0;
    }


    public boolean isAltPressed() {
        return (modifiers & ALT_PRESSED_MASK) > 0;
    }


    public CMouseEvent(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public CMouseEvent(double x, double y, int button) {
        this.x = x;
        this.y = y;
        this.button = button;
    }


    public double getX() {
        return x;
    }

    public CMouseEvent setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public CMouseEvent setY(double y) {
        this.y = y;
        return this;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public CMouseEvent setDeltaX(double deltaX) {
        this.deltaX = deltaX;
        return this;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public CMouseEvent setDeltaY(double deltaY) {
        this.deltaY = deltaY;
        return this;
    }

    public int getButton() {
        return button;
    }

    public CMouseEvent setButton(int button) {
        this.button = button;
        return this;
    }

    public int getClickCount() {
        return clickCount;
    }

    public CMouseEvent setClickCount(int clickCount) {
        this.clickCount = clickCount;
        return this;
    }

    public int getModifiers() {
        return modifiers;
    }

    public CMouseEvent setModifiers(int modifiers) {
        this.modifiers = modifiers;
        return this;
    }
    
    
    public double getHorizontalAmount() {
        return horizontalAmount;
    }


    public CMouseEvent setHorizontalAmount(double horizontalAmount) {
        this.horizontalAmount = horizontalAmount;
        return this;
    }


    public double getVerticalAmount() {
        return verticalAmount;
    }


    public CMouseEvent setVerticalAmount(double verticalAmount) {
        this.verticalAmount = verticalAmount;
        return this;
    }


    public void setWidgetUnderMouse(CWidget widget) {
        widgetUnderMouse = widget;
    }


    public CWidget getWidgetUnderMouse() {
        return widgetUnderMouse;
    }
}
