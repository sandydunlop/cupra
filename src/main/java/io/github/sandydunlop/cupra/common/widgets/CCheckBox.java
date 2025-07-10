package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.CupraException;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.Dimension;
import io.github.sandydunlop.cupra.common.util.Symbol;


public class CCheckBox extends CWidget {
    private String text = null;
    boolean checked = false;


    public CCheckBox (CContainer parent, String text){
        this(parent, text, false);
    }

    
    public CCheckBox (CContainer parent, String text, boolean checked){
        super(parent);
        this.checked = checked;
        this.resizeToContents = true;
        if (parent != null) {
            parent.add(this);
        }
        this.setFocusable(true);
        this.setText(text);
    }


    public void setText(String text) {
        this.text = text;
        recalculateSize();
    }


    public String getText(){
        return this.text;
    }


    public void setChecked(boolean checked) {
        this.checked = checked;
    }


    public boolean isChecked() {
        return this.checked;
    }

   
    @Override
    public void recalculateSize() {
        this.giveWidthHint(text, font.getSize() + 4);
        this.giveHeightHint(text, 0);
    }

    
    @Override
    public void layout(){
        if (width == 0 && resizeToContents){
            BitmapFont f = BitmapFontFactory.load(font);
            Dimension d = f.stringDimensions(this.text);
            this.width = d.getWidth() + font.getSize() + 4;
            this.height = d.getHeight();
        }
    }


    @Override
    public void render(BaseRenderer renderer, CMouseEvent mouse) {
        int renderHeight = getHeight();
        int boxSize = font.getSize();
        int renderX = this.getCalculatedX();
        int renderY = this.getCalculatedY();
        renderer.fill(renderX, renderY, 
                renderX + boxSize, renderY + boxSize,
                CWidget.getPalette().INPUT_BACKGROUND);
        renderer.drawRectangle(renderX, renderY, 
                renderX + boxSize, renderY + boxSize, 
                CWidget.getPalette().SELECTED_BACKGROUND);
        if (text != null) {
            int textX = renderX + boxSize + 4;
            int textY = renderY - 2;
            renderer.setFont(this.font);
            renderer.drawText(text, textX, textY);
        }
        if (checked) {
            renderer.drawSymbol(Symbol.CHECK_MARK, renderX, renderY - 1, renderHeight - 1, CWidget.getPalette().REGULAR_TEXT);
        }
        if (this.getDebug() != 0){
            renderer.drawRectangle(getCalculatedX(), getCalculatedY(), getCalculatedX()+getWidth(), getCalculatedY()+getHeight(), this.getDebug());
        }
    }


    @Override
    public boolean mousePressed(CMouseEvent mouse) {
        if (this.enabled && this.visible) {
            if (this.isMouseOver(mouse)) {
                this.checked = !this.checked;
                return true;
            }
            return false;
        } else {
            return false;
        }
   }


   public String toString(){
        return "CCheckBox: " + text;
    }
}

