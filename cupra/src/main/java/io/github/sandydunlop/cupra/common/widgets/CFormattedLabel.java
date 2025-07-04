package io.github.sandydunlop.cupra.common.widgets;

import java.util.ArrayList;
import java.util.List;

import io.github.sandydunlop.cupra.common.CupraException;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.Dimension;
import io.github.sandydunlop.cupra.common.util.DepthLimit;


public class CFormattedLabel extends CContainer {
    private int margin = 0;

	public CFormattedLabel(CContainer parent) {
		super(parent, false);
        this.setPadding(0);
        this.setUsesCustomLayout(true);
	}


    public synchronized void customLayout(){
        int lineHeight = 0;
        int renderX = margin;
        int renderY = 0;
        String wordChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        wordChars += ",.!?:;";
        List<CWidget> formatted = new ArrayList<>();

        for (int i = 0; i<this.contents.size(); i++) {
            CWidget widget = this.contents.get(i);
            if (widget instanceof CLabel element){
                BitmapFont font = BitmapFontFactory.load(element.getFontOptions());
                lineHeight = font.getHeight() + 3;
                boolean splitting = true;
                while(splitting){
                    splitting = false;
                    int widthRamaining = this.width - renderX;
                    int displayabeCharCount = font.getDisplayableCharCount(element.getText(), widthRamaining);
                    if (displayabeCharCount < element.getText().length()) {
                        // It splits
                        int previousNonWC;
                        for (previousNonWC = displayabeCharCount; 
                                previousNonWC > 0 && wordChars.indexOf(element.getText().charAt(previousNonWC)) > -1;
                                previousNonWC--);
                        if (previousNonWC > 0) {
                            String a = element.getText().substring(0, previousNonWC);
                            if (element.getText().charAt(previousNonWC) == ' '){
                                previousNonWC++;
                            }
                            String b = element.getText().substring(previousNonWC);
                            CLabel part = duplicate(element);
                            part.setText(a);
                            part.setX(renderX);
                            part.setY(renderY);
                            Dimension d = font.stringDimensions(a);
                            part.setHeight(d.getHeight());
                            part.setWidth(d.getWidth());
                            formatted.add(part);
                            renderX += d.getWidth();
                            element.setText(b);
                            splitting = true;
                        }else{
                            if (renderX == 0){
                                //Just add it
                                Dimension d = font.stringDimensions(element.getText());
                                element.setHeight(d.getHeight());
                                element.setWidth(d.getWidth());
                                element.setX(0);
                                element.setY(renderY);
                                formatted.add(element);
                            }else{
                                renderX = margin;
                                renderY += lineHeight;
                                splitting = true;
                            }
                        }
                    }else{
                        // It fits
                        Dimension d = font.stringDimensions(element.getText());
                        element.setHeight(d.getHeight());
                        element.setWidth(d.getWidth());
                        element.setX(renderX);
                        element.setY(renderY);
                        formatted.add(element);
                        renderX += d.getWidth();
                    }
                }
            } else if (widget instanceof CNewLine) {
                renderX = margin;
                renderY += lineHeight;
                formatted.add(widget);
            }
        }
        if (this.resizeToContents) {
            this.setHeight(renderY + lineHeight + 10);
        }
        this.setWidth(this.parent.getWidth());
        this.contents.clear();
        for(CWidget widget : formatted){
            this.contents.add(widget);
            widget.parent = this;
        }      
    }


    private CLabel duplicate(CLabel element){
        CLabel copy = new CLabel(null, element.getText());
        copy.setFont(element.getFontOptions());
        copy.setX(element.getX());
        copy.setY(element.getY());
        return copy;
    }


    public static CNewLine newLine(){
        return new CNewLine();
    }


    @Override
    public void layout(){
        this.width = this.parent.getWidth();
        if (this.width > 0){
            customLayout();
        }
    }


	@Override
	public CWidget hoveredWidget(CMouseEvent mouse, DepthLimit depth) {
        CWidget hovered = null;
		for (CWidget widget : this.contents) {
			if (widget.isMouseOver(mouse)) {
                hovered = widget;
				CWidget inner = widget.hoveredWidget(mouse, depth);
				if (inner != null){
					return inner;
				}
				return hovered;
			}
		}
		return hovered;
    }

    public static class CNewLine extends CWidget {

        public CNewLine() {
            super(null);
        }

        protected CNewLine(CContainer parent) {
            super(parent);
        }

        @Override
        public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) throws CupraException {
            // DO nothing
        }

    }
}
