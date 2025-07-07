package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.CupraException;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.Math;
import io.github.sandydunlop.cupra.common.util.Orientation;
import io.github.sandydunlop.cupra.common.util.DepthLimit;
import io.github.sandydunlop.cupra.platform.PlatformServices;


public class CScrollableContainer extends CContainer {
    protected CContainer content = null;
    protected CScrollBar verticalScrollBar = null;
    protected CScrollBar horizontalScrollBar = null;
    private int contentHeight = 0;
    protected double scrollAmount = 0;
    protected boolean scrolling = false;


    public CScrollableContainer(CContainer parent){
        super(parent);
        verticalScrollBar = new CScrollBar(null, Orientation.VERTICAL);
        verticalScrollBar.onPositionChanged(newScrollAmount -> {
            setScrollAmount(newScrollAmount);
            getScrollAmount();
        });
    }


    @Override
    public void setX(int x) {
        super.setX(x);
        verticalScrollBar.setX(x + width - verticalScrollBar.getWidth());
    }


    @Override
    public void setY(int y) {
        super.setY(y);
        verticalScrollBar.setY(y);
    }


    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        verticalScrollBar.setX(x + width - verticalScrollBar.getWidth());
    }


    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        if (horizontalScrollBar != null) {
            horizontalScrollBar.setY(y + height - verticalScrollBar.getHeight());
        }
    }


    //
    // === Rendering & Layout ===
    //


    @Override
    public void layout() {
        verticalScrollBar.setX(getCalculatedX() + width - verticalScrollBar.getWidth());
        verticalScrollBar.setY(getCalculatedY());
        verticalScrollBar.setHeight(getHeight());
        verticalScrollBar.setMax(Math.max(0, getContentHeight()));
        verticalScrollBar.setScrollAmount(getScrollAmount());
        //TODO: horizontal scrollbar
    }


    @Override
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) throws CupraException {
        if (this.isVisible()) {
            if (this.hasBackground) {
                int renderWidth = getWidth();
                int renderHeight = getHeight();
                renderer.fill(getCalculatedX(), getCalculatedY(), getCalculatedX() + renderWidth, getCalculatedY() + renderHeight, backgroundColor);
            }
            int contentWidth = this.getWidth();
            if (this.content != null) {
                renderer.enableClipping(this.getCalculatedX(), this.getCalculatedY(), this.getCalculatedX() + contentWidth, this.getCalculatedY() + this.getHeight());
                content.render(renderer, mouseX, mouseY, delta);
                verticalScrollBar.render(renderer, mouseX, mouseY, delta);
                renderer.disableClipping();
            }
        }
    }


    //
    // === Mouse ===
    //

    
    @Override
	public CWidget hoveredWidget(CMouseEvent mouse, DepthLimit depth) {
        if (verticalScrollBar != null && verticalScrollBar.isMouseOver(mouse)) {
            return verticalScrollBar;
        }
        if (horizontalScrollBar != null && horizontalScrollBar.isMouseOver(mouse)) {
            return horizontalScrollBar;
        }
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
    public boolean mouseScrolled(CMouseEvent mouse) {
        this.setScrollAmount(this.getScrollAmount() + mouse.getVerticalAmount() * 0.05 * this.getContentHeight() / 2.0);
        if (verticalScrollBar != null) {
            verticalScrollBar.setScrollAmount(scrollAmount);
        }
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
            verticalScrollBar.setMax(Math.max(0, getContentHeight()));
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
}
