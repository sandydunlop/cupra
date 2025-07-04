package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.CupraScreen;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.fonts.FontSpec;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.Align;
import io.github.sandydunlop.cupra.common.util.Dimension;


public class CLabel extends CWidget {
    protected String text = null;
    protected Align.Horizontal align = Align.Horizontal.LEFT;


    public CLabel(CContainer parent, String text) {
        this(parent, text, Align.Horizontal.LEFT);
    }


    public CLabel(CContainer parent, String text, Align.Horizontal align) {
        super(parent);
        this.resizeToContents = true;
        this.align = align;
        this.color = getPalette().REGULAR_TEXT;
        this.font = CupraScreen.defaultFontOptions();
        if (parent != null){
            parent.add(this);
            this.font.mergeFrom(parent.getFont());
        }else{
            this.font = new FontSpec()
                    .setName("Rubik")
                    .setColor(CWidget.getPalette().WIDGET_TEXT);
        }
        this.setText(text);
    }


    public CLabel setFont(FontSpec spec){
        if (this.font == null){
            this.font = new FontSpec();
        }
        this.font.mergeFrom(spec);
        this.giveWidthHint(text, 0);
        this.giveHeightHint(text, 0);
        return this;
    }
    
    
    public FontSpec getFontOptions(){
        return this.font;
    }


    public CLabel fontSize(int size){
        this.font.setSize(size);
        this.giveWidthHint(text, 0);
        this.giveHeightHint(text, 0);
        return this;
    }


    public CLabel color(int color){
        this.font.setColor(color);
        return this;
    }


    public CLabel bold(){
        this.font.setBold(true);
        this.giveWidthHint(text, 0);
        this.giveHeightHint(text, 0);
        return this;
    }


    public CLabel italic(){
        this.font.setItalic(true);
        this.giveWidthHint(text, 0);
        this.giveHeightHint(text, 0);
        return this;
    }


    public CLabel shadow() {
        this.font.setShadow(true);
        return this;
    }


    public CLabel fontName(String fontName){
        this.font.setName(fontName);
        return this;
    }
    
    
    public CLabel align(Align.Horizontal alignment){
        this.align = alignment;
        return this;
    }


    public CLabel setText(String text) {
        this.text = text;
        this.giveWidthHint(text, 0);
        this.giveHeightHint(text, 0);
        return this;
    }


    public String getText(){
        return this.text;
    }


    // === Contents and Layout ===


    @Override
	public void layout(){
        if (this.resizeToContents) {
            BitmapFont bmf = BitmapFontFactory.load(this.font);
            if (this.width == 0){
                this.width = parent.width;
                this.height = bmf.getHeight();
            }else{
                int wrap = -1; // Or set to this.width
                Dimension d = bmf.getStringDimensions(this.text, wrap);
                this.width = d.getWidth();
                this.height = d.getHeight();
            }
        }
    }

    
    @Override
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) {
        if (text != null && !text.isBlank()) {
            int renderX = this.getCalculatedX();
            BitmapFont f = BitmapFontFactory.load(this.font);
            
            if (this.align == Align.Horizontal.MIDDLE){
                renderX += (int)((this.getWidth() / 2.0) - (f.stringWidth(text)/2.0));
            }else if (this.align == Align.Horizontal.RIGHT){
                int renderWidth = this.getWidth();
                int textWidth = f.stringWidth(text);
                renderX +=  renderWidth - textWidth;
            }

            renderer.setFont(this.font);
            renderer.drawText(this.text, renderX, getCalculatedY(), -1);
        }
        if (this.getDebug() != 0){
            renderer.drawRectangle(getCalculatedX(), getCalculatedY(), getCalculatedX()+getWidth(), getCalculatedY()+getHeight(), this.getDebug());
        }
    }


    public String toString(){
        if (this.text != null) {
            String t = text.length() > 20 ? text.substring(0, 20) : text;
            return "CLabel: " + t;
        }
        return "CLabel";
    }
}
