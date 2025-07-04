package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.CupraException;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.fonts.FontSpec;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;


public class CButton extends CWidget {
    private CClickAction onClick;
    private String text = null;


    public CButton (CContainer parent, String text, CClickAction onClick){
        super(parent);
        this.onClick = onClick;
        this.resizeToContents = true;
        if (parent != null) {
            parent.add(this);
        }
        this.font = new FontSpec()
                .setName("Rubik")
                .setColor(CWidget.getPalette().WIDGET_TEXT);
        this.setText(text);
    }


    public interface CClickAction {
        void onClick(CButton click);
    }


    public void setFont(FontSpec font) {
        this.font.mergeFrom(font);
    }


    public void setText(String text) {
        this.text = text;
        recalculateSize();
    }


    public String getText() {
        return this.text;
    }


    @Override
    public void recalculateSize() {
        this.giveWidthHint(text, 10);
        this.giveHeightHint(text, 4);
    }


    @Override
	public void layout() {
        recalculateSize();
    }


    @Override
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) throws CupraException {
        int renderWidth = getWidth();
        int renderHeight = getHeight();
        int fillColor = CWidget.getPalette().HOVERED_BACKGROUND;
        if (enabled) {
            font.setColor(CWidget.getPalette().WIDGET_TEXT);
        } else {
            font.setColor(CWidget.getPalette().REGULAR_TEXT);
            fillColor = CWidget.getPalette().REGULAR_BACKGROUND;
        }
        renderer.fill(getCalculatedX(), getCalculatedY(), 
                getCalculatedX() + renderWidth, 
                getCalculatedY() + renderHeight,
                fillColor);
        renderer.drawRectangle(getCalculatedX(), getCalculatedY(), 
                getCalculatedX() + renderWidth, 
                getCalculatedY() + renderHeight, 
                CWidget.getPalette().SELECTED_BACKGROUND);
        if (text != null) {
            BitmapFont bmf = BitmapFontFactory.load(font);
            int textX = getCalculatedX() + ((renderWidth/2) - (bmf.stringWidth(text)/2));
            int textY = getCalculatedY() + ((renderHeight/2) - (bmf.getHeight()/2));
            if (this instanceof CSymbolButton) {
                textX++;
            }
            renderer.setFont(this.font);
            renderer.drawText(text, textX, textY);
        }
        if (this.getDebug() != 0){
            renderer.drawRectangle(getCalculatedX(), getCalculatedY(), getCalculatedX()+getWidth(), getCalculatedY()+getHeight(), this.getDebug());
        }
    }


    @Override
    public boolean mousePressed(CMouseEvent mouse) {
        if (this.enabled && this.visible) {
            if (this.isMouseOver(mouse)) {
                this.onClick.onClick(this);
                return true;
            }
            return false;
        } else {
            return false;
        }
    }


    public String toString(){
        return "CButton: " + text;
    }
}

