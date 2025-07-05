package io.github.sandydunlop.cupra.common.events;

public class CKeyEvent {
    public static final int KEY_ESCAPE = 0x1B;
	public static final int KEY_LEFT = 37;
	public static final int KEY_UP = 38;
	public static final int KEY_RIGHT = 39;
	public static final int KEY_DOWN = 40;

	public static final int KEY_BACKSPACE = 8;
	public static final int KEY_ENTER = 10;
    public static final int KEY_A = 0x41;
    public static final int KEY_C = 0x43;
    public static final int KEY_V = 0x56;
    public static final int KEY_X = 0x58;
    public static final int KEY_Z = 0x5A;

    public static final int CONTROL_PRESSED_MASK = 1 << 7;
    public static final int SHIFT_PRESSED_MASK = 1 << 6;
    public static final int COMMAND_PRESSED_MASK = 1 << 8;
    public static final int ALT_PRESSED_MASK = 1 << 9;

    private int keyCode = 0;
    private int extendedKeyCode = 0;
    private int modifiers = 0;
    private int scanCode = 0;
    private char character = 0;


    public CKeyEvent(){
    }


    public CKeyEvent(int keyCode, int extendedKeyCode, int modifiers, int scanCode, char character){
            this.keyCode = keyCode;
            this.extendedKeyCode = extendedKeyCode;
            this.modifiers = modifiers;
            this.scanCode = scanCode;
            this.character = character;
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


    public int getKeyCode() {
        return keyCode;
    }


    public CKeyEvent setKeyCode(int keyCode) {
        this.keyCode = keyCode;
        return this;
    }


    public int getExtendedKeyCode() {
        return extendedKeyCode;
    }


    public CKeyEvent setExtendedKeyCode(int extendedKeyCode) {
        this.extendedKeyCode = extendedKeyCode;
        return this;
    }


    public int getModifiers() {
        return modifiers;
    }


    public CKeyEvent setModifiers(int modifiers) {
        this.modifiers = modifiers;
        return this;
    }


    public int getScanCode() {
        return scanCode;
    }


    public CKeyEvent setScanCode(int scanCode) {
        this.scanCode = scanCode;
        return this;
    }


    public char getCharacter() {
        return character;
    }


    public CKeyEvent setCharacter(char character) {
        this.character = character;
        return this;
    }


    public boolean isActionKey() {
        return (0xFFFF == character) || keyCode == KEY_ESCAPE;
    }    
}
