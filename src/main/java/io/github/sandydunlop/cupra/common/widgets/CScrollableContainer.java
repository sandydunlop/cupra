package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.CupraException;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.Math;
import io.github.sandydunlop.cupra.common.util.DepthLimit;
import io.github.sandydunlop.cupra.platform.PlatformServices;


public class CScrollableContainer extends CContainer {
    protected CContainer content;
    private int contentHeight = 0;
    protected double scrollAmount = 0;
    protected boolean scrolling = false;


    public CScrollableContainer(CContainer parent){
        super(parent);
    }


    // === Rendering ===


    @Override
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) throws CupraException {
        if (this.isVisible()) {
            if (this.hasBackground) {
                int renderWidth = getWidth();
                int renderHeight = getHeight();
                renderer.fill(getCalculatedX(), getCalculatedY(), getCalculatedX() + renderWidth, getCalculatedY() + renderHeight, backgroundColor);
            }
            int contentWidth = this.getWidth();
            int maxScroll = this.getMaxScroll();
            if (maxScroll > 0) {
                contentWidth -= 6;
                int scrollBarLeft = this.getScrollbarPositionX();
                int scrollerHeight = (int)((float)(this.getHeight() * this.getHeight()) / (float)this.getMaxPosition());
                scrollerHeight = Math.clamp(scrollerHeight, 32, this.getHeight() - 8);
                int scrollerTop = (int)this.getScrollAmount() * (this.getHeight() - scrollerHeight) / maxScroll + this.getCalculatedY();
                if (scrollerTop < this.getCalculatedY()) {
                    scrollerTop = this.getCalculatedY();
                }
                renderer.fill(scrollBarLeft, this.getCalculatedY(), scrollBarLeft + 6, this.getCalculatedY() + this.getHeight(), CWidget.getPalette().HOVERED_BACKGROUND);
                renderer.fill(scrollBarLeft, scrollerTop, scrollBarLeft+6, scrollerTop+scrollerHeight, CWidget.getPalette().REGULAR_TEXT);
            }
            if (this.content != null) {
                renderer.enableClipping(this.getCalculatedX(), this.getCalculatedY(), this.getCalculatedX() + contentWidth, this.getCalculatedY() + this.getHeight());
                content.render(renderer, mouseX, mouseY, delta);
                renderer.disableClipping();
            }
        }
    }


    // === Mouse and Keyboard ===

    
    @Override
	public CWidget hoveredWidget(CMouseEvent mouse, DepthLimit depth) {
        if (depth == DepthLimit.SCROLLABLE) {
            return this;
        }
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


    protected CWidget internalHoveredWidget(CMouseEvent mouse) {
        if (content != null) {
            for (CWidget widget : content.contents()) {
                if (widget.isMouseOver(mouse)) {
                    return widget;
                }
            }
        }
        return null;
    }


    @Override
    public boolean mouseReleased(CMouseEvent mouse) {
        this.scrolling = false;
        return false;
    }


    @Override
    public boolean mousePressed(CMouseEvent mouse) {
        super.mousePressed(mouse);
    	this.updateScrollingState(mouse);
        return true;
    }
        
    
    @Override
    public boolean mouseDragged(CMouseEvent mouse) {
        if (mouse.getButton() != CMouseEvent.PRIMARY_BUTTON || !this.scrolling) return false;
        if (mouse.getY() < this.getCalculatedY()) {
            // Too far up
        	setScrollAmount(0.0);
        } else if (mouse.getY() > getCalculatedY() + getHeight()) {
            // Too far down
        	setScrollAmount(getMaxScroll());
        } else {
        	double maxScroll = Math.max(1, getMaxScroll());
            int i = this.getHeight();
            int j = (Math.clamp(((int)((float)(i * i) / (float)getMaxPosition())), 32, (i - 8)));
            double e = Math.max(1.0, maxScroll / (i - j));
            setScrollAmount(getScrollAmount() + mouse.getDeltaY() * e);
        }
        return true;
    }


    @Override
    public boolean mouseScrolled(CMouseEvent mouse) {
        this.setScrollAmount(this.getScrollAmount() + mouse.getVerticalAmount() * 0.05 * this.getContentHeight() / 2.0);
        return true;
    }


    //
    // === Scrolling ===
    //


    // This should be called for web page type widgets but not list boxes etc
    protected synchronized void calculateHeight() {
        if (this.content != null){
            if (contentHeight == 0) {
                int height = 0;
                for (CWidget widget : content.contents()) {
                    if (widget.getY() + widget.getHeight() > height) {
                        height = widget.getY() + widget.getHeight() ;
                    }
                }
                contentHeight = height;
            }
            content.setHeight(contentHeight);
        }
    }


    // This should be called for listbox type widgets, but not web pages etc
    public synchronized void setContentHeight(int height) {
        contentHeight = height;
        if (content != null) {
            content.setHeight(height);
        }
    }


    public synchronized int getContentHeight() {
        return contentHeight;
    }


    public int getMaxScroll() {
        int mp = this.getMaxPosition();
        return Math.max(0, mp - (this.getHeight()));
    }


    protected int getMaxPosition() {
        return this.getContentHeight();
    }


    public synchronized void scrollTo(CWidget widget){
        getScrollAmount();
        if (widget != null) {
            if (widget.getY() - this.scrollAmount < 0) {
                this.scrollAmount = Math.max(0, widget.getY());
            } else if (widget.getY() + widget.getHeight() - this.scrollAmount > this.getHeight()) {
                this.scrollAmount = Math.min(getMaxScroll(), widget.getY() + widget.getHeight() - this.getHeight());
            }
        }
        PlatformServices.getInstance().render(this);
    }
    

    public double getScrollAmount() {
        if (content != null) {
            content.setY((int)(this.getCalculatedY() - this.scrollAmount));
            content.setX(this.getCalculatedX());
        }
    	return this.scrollAmount;
    }


    public void setScrollAmount(double amount) {
    	this.scrollAmount = Math.clamp(amount, 0.0, this.getMaxScroll());
    }


    protected int getScrollbarPositionX() {
    	return this.getCalculatedX() + this.getWidth() - 6;
    }


    protected void updateScrollingState(CMouseEvent mouse) {
    	this.scrolling = mouse.getButton() == CMouseEvent.PRIMARY_BUTTON && mouse.getX() >= this.getScrollbarPositionX() && mouse.getX() < (this.getScrollbarPositionX() + 6);
    }
}
