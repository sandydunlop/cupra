package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.events.CKeyEvent;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.fonts.FontSpec;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.MousePointer;
import io.github.sandydunlop.cupra.platform.PlatformServices;


public class CTextBox extends CLabel {
    private static final String WORD_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
    public static final int TEXT_PADDING = 2;
    private int startOffset = 0;
    private int caretX = 0;
    protected int caretPos = 0;
    private int caretColor = 0xFFFFFFFF;
    private BitmapFont bmf = null;
    private FontSpec selectedFont = null;
    private KeyPressedAction onKeyPressed;
    private EnterPressedAction onEnterPressed;
    private MousClickedAction onMouseClicked;
    
    protected double mousePressX = -1;
    protected double mousePressY = -1;
    protected int selectionStart = -1;
    protected int selectionEnd = -1;
    protected int selectFromPos = -1;
    protected int selectToPos = -1;
    protected boolean selecting = false;
    protected boolean editable = true;


    public CTextBox(CContainer parent) {
        this(parent, "");
    }


    public CTextBox(CContainer parent, String text) {
        super(parent, text);
        this.setFocusable(true);
        this.mousePointer = MousePointer.I_BEAM;
        this.expandable = false;
        this.resizeToContents = false;
        selectedFont = font.duplicate();
        if (text == null) {
            text = "";
        }
        this.setText(text);
    }


    @Override
    public CTextBox setText(String text) {
        super.setText(text);
        this.bmf = BitmapFontFactory.load(this.font);
        if (caretPos > text.length()) {
            caretPos = text.length();
        }
        setCaretX();
        return this;
    }


    @Override
    public String getText() {
        return this.text;
    }


    public void setEditable(boolean editable) {
        this.editable = editable;
        if (editable) {
            this.mousePointer = MousePointer.I_BEAM;
        } else {
            this.mousePointer = MousePointer.ARROW;
        }
    }


    public boolean getEditable() {
        return this.editable;
    }


    @Override
    public int getDefaultWidth() {
        return 200;
    }


    @Override
    public int getDefaultHeight() {
        bmf = BitmapFontFactory.load(font);
        return bmf.getHeight() + TEXT_PADDING*2;
    }


    //
    // === Layout & Render ===
    //


    private void setCaretX() {
        if (caretPos > 0){
            String left = text.substring(0, caretPos > text.length() ? text.length() : caretPos);
            bmf = BitmapFontFactory.load(font);
            caretX = bmf.stringWidth(left);
            if (caretX - startOffset > width - (TEXT_PADDING*2)) {
                startOffset = caretX - width + (TEXT_PADDING*2);
            }
        }else{
            caretX = 0;
        }
        if (caretX - startOffset < 0) {
            startOffset = caretX;
        }
    }


    @Override
    public void layout() {
        super.layout();
        bmf = BitmapFontFactory.load(font);
        this.height = bmf.getHeight() + (TEXT_PADDING*2);
        setCaretX();
    }


    @Override
    public void render(BaseRenderer renderer, CMouseEvent mouse) {
        bmf = BitmapFontFactory.load(font);
        int renderWidth = getWidth();
        int renderHeight = getHeight();
        int borderColor = CWidget.getPalette().SELECTED_BACKGROUND;
        renderer.fill(getCalculatedX(), getCalculatedY(), getCalculatedX() + renderWidth, getCalculatedY() + renderHeight, CWidget.getPalette().INPUT_BACKGROUND);
        renderer.drawRectangle(getCalculatedX(), getCalculatedY(), getCalculatedX() + renderWidth, getCalculatedY() + renderHeight, borderColor);

        if (text != null && !text.isBlank()) {
            int renderX = getCalculatedX();
            int renderY = getCalculatedY();
            renderer.setFont(font);
            renderer.enableClipping(renderX + TEXT_PADDING, 
                    renderY + TEXT_PADDING, 
                    renderX + renderWidth - TEXT_PADDING, 
                    renderY + renderHeight - TEXT_PADDING);
            renderX = renderX + TEXT_PADDING - startOffset;
            renderer.drawText(text, renderX, renderY + TEXT_PADDING);
            if (editable && selecting) {
                selectedFont.mergeFrom(font);
                selectedFont.setColor(CWidget.getPalette().SELECTED_TEXT);
                renderer.setFont(selectedFont);
                renderSelection(renderer, renderX);
            }
            renderer.disableClipping();
        }
        if (editable && isFocused()) {
            if (PlatformServices.getInstance().isCursorVisible()) {
                renderer.drawVerticalLine(getCalculatedX() + TEXT_PADDING + caretX - startOffset, getCalculatedY() + 1, getCalculatedY() + renderHeight - 1, caretColor);
            }
        }
        if (this.getDebug() != 0){
            renderer.drawRectangle(getCalculatedX(), getCalculatedY(), getCalculatedX()+getWidth(), getCalculatedY()+getHeight(), this.getDebug());
        }
    }


    public void renderSelection(BaseRenderer renderer, int renderX) {
        if (selectionStart > -1 && selectionEnd > -1) {
            String left = text.substring(0, selectionStart);
            String selection = text.substring(selectionStart, selectionEnd);
            bmf = BitmapFontFactory.load(this.font);
            int leftWidth = bmf.stringWidth(left);
            int selectionWidth = bmf.stringWidth(selection);
            int sx = renderX + leftWidth;
            renderer.fill(sx, getCalculatedY(), sx + selectionWidth, getCalculatedY() + getHeight(), CWidget.getPalette().SELECTED_BACKGROUND);
            renderer.drawText(selection, sx, getCalculatedY() + TEXT_PADDING);
        }
    }


    // === Keyboard ===


    public interface KeyPressedAction {
        void onKeyPressed(CKeyEvent key);
    }


    public interface EnterPressedAction {
        void onEnterPressed(CTextBox enter);
    }


    public void onKeyPressed(KeyPressedAction action) {
        this.onKeyPressed = action;
    }


    public void onEnterPressed(EnterPressedAction action) {
        this.onEnterPressed = action;
    }


    public void handleControlKeypress(CKeyEvent e) {
        switch (e.getKeyCode()) {
            case CKeyEvent.KEY_A:
                selectAll();
                break;
            case CKeyEvent.KEY_C:
                PlatformServices.getInstance().copyTextToClipboard(getSelectedText());
                break;
            case CKeyEvent.KEY_V:
                writeText(PlatformServices.getInstance().getClipboardText());
                break;
            case CKeyEvent.KEY_X:
                break;
            case CKeyEvent.KEY_Z:
                break;
            default:
                break;
        }
        if (onKeyPressed != null) {
            this.onKeyPressed.onKeyPressed(e);
        }
    }


    private void writeText(String t) {
        CKeyEvent k = new CKeyEvent(0, 0, 0, 0, (char)0);
        for (int i=0; i<text.length();i++){
            writeChar(t.charAt(i));
        }
    }


    private void writeChar(char ch) {
        String left = "";
        String right = "";
        deleteSelection();
        left = "";
        right = "";
        if (caretPos > 0) {
            left = text.substring(0, caretPos);
        }
        if (caretPos < text.length()){
            right = text.substring(caretPos);
        }
        caretPos++;

        bmf = BitmapFontFactory.load(this.font);

        int stringWidth = bmf.stringWidth(left+ch);
        if (stringWidth > width - (TEXT_PADDING * 2)) {
            startOffset = stringWidth - width + (TEXT_PADDING * 2);
        }

        caretX = stringWidth;
        text = left + ch + right;
    }


	@Override
	public void charTyped(CKeyEvent e) {
        if (!editable) {
            return;
        }
        if ((PlatformServices.getInstance().isMacOS() && e.isCommandPressed()) || 
            (!PlatformServices.getInstance().isMacOS()) && e.isControlPressed()){
            handleControlKeypress(e);
            return;
        }
        int k = e.getCharacter();
        switch (k) {
            case CKeyEvent.KEY_BACKSPACE:
                backspace();
                PlatformServices.getInstance().render();
                break;
            case CKeyEvent.KEY_ENTER:
                if (onEnterPressed != null) {
                    this.onEnterPressed.onEnterPressed(this);
                }
                PlatformServices.getInstance().render();
                break;
            default:
                char ch = e.getCharacter();
                if (ch != -1){
                    writeChar(ch);
                }
        }
        if (onKeyPressed != null) {
            this.onKeyPressed.onKeyPressed(e);
        }
        PlatformServices.getInstance().render();
	}


	@Override
	public void keyPressed(CKeyEvent k) {
        if (!editable) {
            return;
        }
        switch (k.getKeyCode()) {
            case CKeyEvent.KEY_LEFT:
                leftKeyPressed(k);
                break;
            case CKeyEvent.KEY_UP:
                break;
            case CKeyEvent.KEY_RIGHT:
                rightKeyPressed(k);
                break;
            case CKeyEvent.KEY_DOWN:
                break;
            default:
                break;
        }
        if (onKeyPressed != null) {
            this.onKeyPressed.onKeyPressed(k);
        }
        PlatformServices.getInstance().render();
	}


    private void leftKeyPressed(CKeyEvent k) {
        if (k.isShiftPressed() && selectFromPos == -1){
            selectFromPos = caretPos;
        }
        if (caretPos > 0){
            if (skipWordKeyPressed(k)) {
                moveCaretOneWord(-1);
            }else{
                caretPos--;
            }
        }
        setCaretX();
        if (k.isShiftPressed()) {
            selectToPos = caretPos;
            updateSelection();                
        } else{
            clearSelection();
        }
    }


    private void rightKeyPressed(CKeyEvent k) {
        if (k.isShiftPressed() && selectFromPos == -1){
            selectFromPos = caretPos;
        }
        if (caretPos < text.length()){
            if (skipWordKeyPressed(k)) {
                moveCaretOneWord(+1);
            }else{
                caretPos++;
            }
        }
        setCaretX();
        if (k.isShiftPressed()) {
            selectToPos = caretPos;
            updateSelection();                
        } else{
            clearSelection();
        }
    }


    private boolean skipWordKeyPressed(CKeyEvent k) {
        return ((PlatformServices.getInstance().isMacOS() && k.isAltPressed()) || 
                (!PlatformServices.getInstance().isMacOS()) && k.isControlPressed());
    }


    private void moveCaretOneWord(int direction) {
        int look = direction > 0 ? 0 : -1;
        boolean seenWordChar = false;
        if (direction  > 0) {
            while (caretPos < text.length()) {
                if (isWordChar(text.charAt(caretPos + look))){
                    seenWordChar = true;
                }
                if (!seenWordChar || isWordChar(text.charAt(caretPos + look))) {
                    caretPos += direction;
                } else {
                    break;
                }
            }
        } else {
            while (caretPos > 0) {
                if (isWordChar(text.charAt(caretPos + look))){
                    seenWordChar = true;
                }
                if (!seenWordChar || isWordChar(text.charAt(caretPos + look))) {
                    caretPos += direction;
                }else {
                    break;
                }
            }
        }
    }


    private void backspace(){
        if (selecting) {
            deleteSelection();
        }else if (caretPos > 0){
            String left = "";
            String right = "";
            bmf = BitmapFontFactory.load(this.font);
            if (caretPos > 1) {
                left = text.substring(0, caretPos - 1);
            }
            if (caretPos < text.length()){
                right = text.substring(caretPos);
            }
            caretPos--;
            int stringWidth = bmf.stringWidth(left);
            int oldCaretX = caretX;
            caretX = stringWidth;

            if (startOffset > 0) {
                startOffset -= oldCaretX - caretX;
            }
            if (startOffset < 0) {
                startOffset = 0;
            }

            text = left + right;
        }
	}


    // === Mouse ===


    private int calculateCaretPos(CMouseEvent mouse) {
        this.bmf = BitmapFontFactory.load(this.font);
        int myCaretX = (int)(mouse.getX() - (getCalculatedX() + TEXT_PADDING)) + startOffset;
        int myCaretPos = this.bmf.characterAt(text, myCaretX);
        if (myCaretPos == -1) {
            myCaretPos = text.length();
        }
        return myCaretPos;
    }
   

    public interface MousClickedAction {
        void onMouseClicked(CTextBox enter);
    }


    public void onMouseClicked(MousClickedAction action) {
        this.onMouseClicked = action;
    }


    @Override
    public boolean mouseDoubleClicked(CMouseEvent mouse) {
        if (!editable) return false;
        clearSelection();
        caretPos = calculateCaretPos(mouse);
        selectFromPos = caretPos;
        setCaretX();
        selectWord();
        return true;
    }


    @Override
    public boolean mouseTripleClicked(CMouseEvent mouse) {
        if (!editable) return false;
        selectAll();
        return true;
    }


    @Override
    public boolean mousePressed(CMouseEvent mouse) {
        if (editable) {
            caretPos = calculateCaretPos(mouse);
            if (mouse.isShiftPressed() && selectFromPos != -1) {
                selectToPos = caretPos;
                updateSelection();
            } else {
                clearSelection();
                selectFromPos = caretPos;
            }
            setCaretX();
            mousePressX = mouse.getX();
            mousePressY = mouse.getY();
        }
        PlatformServices.getInstance().render();
        if (this.onMouseClicked != null) {
            this.onMouseClicked.onMouseClicked(this);
        }
        return true;
    }


    @Override
    public boolean mouseScrolled(CMouseEvent mouse) {
        return false;
    }


    @Override
    public boolean mouseDragged(CMouseEvent mouse) {
        if (!editable) return false;
        if (mouse.getButton() != CMouseEvent.PRIMARY_BUTTON) return false;
        caretPos = calculateCaretPos(mouse);
        selectToPos = caretPos;
        updateSelection();
        setCaretX();
        if (caretX - startOffset < 0) {
            startOffset = caretX;
        }
        return true;
    }


    //
    // === Selection ===
    //


    public String getSelectedText() {
        if (text == null || text.isEmpty() || selectionStart == -1 || selectionEnd == -1) {
            return "";
        }
        return text.substring(selectionStart, selectionEnd);
    }


    private void updateSelection() {
        selecting = true;
        if (selectToPos >= selectFromPos) {
            selectionStart = selectFromPos;
            selectionEnd = selectToPos;
        } else {
            selectionStart = selectToPos;
            selectionEnd = selectFromPos;
        }
    }


    private void selectWord() {
        for (selectionStart = selectFromPos;
                // selectionStart > 0 && isWordChar(text.substring(selectionStart - 1, selectionStart)); 
                selectionStart > 0 && isWordChar(text.charAt(selectionStart - 1)); 
                selectionStart--);
        for (selectionEnd = selectFromPos; 
                // selectionEnd < text.length() && isWordChar(text.substring(selectionEnd, selectionEnd + 1));
                selectionEnd < text.length() && isWordChar(text.charAt(selectionEnd));
                selectionEnd++);
        caretPos = selectionEnd;
        selecting = true;
        setCaretX();
    }


    private void selectAll() {
        selectionStart = 0;
        selectionEnd = text.length();
        selectFromPos = 0;
        selectToPos = text.length();
        caretPos = selectionEnd;
        selecting = true;
        setCaretX();
    }


    private void deleteSelection() {
        if (selectionEnd != -1) {
            //remove/overwite selection
            String left = text.substring(0, selectionStart);
            String right = text.substring(selectionEnd);
            text = left + right;
            caretPos = selectionStart;
            setCaretX();
            clearSelection();
        }
    }


    private void clearSelection() {
        selectFromPos = -1;  //Where caret started selecting
        selectToPos = -1;    //Where caret finished selecting
        selectionStart = -1; //Leftmost selected character
        selectionEnd = -1;   //Rightmost selected character
        selecting = false;
    }


    private boolean isWordChar(char c) {
        return (WORD_CHARS.indexOf(c) > -1);
    }
}
