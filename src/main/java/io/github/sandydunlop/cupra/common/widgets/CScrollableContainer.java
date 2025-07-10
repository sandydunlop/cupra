package io.github.sandydunlop.cupra.common.widgets;

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
    private int contentWidth = 0;
    private int renderX = 0;
    private int renderY = 0;
    protected double verticalScrollAmount = 0;
    protected double horizontalScrollAmount = 0;
    protected boolean scrolling = false;
    protected boolean horizontalScrollingEnabled = true;


    public CScrollableContainer(CContainer parent){
        super(parent);
        verticalScrollBar = new CScrollBar(null, Orientation.VERTICAL);
        verticalScrollBar.onPositionChanged(newVerticalScrollAmount -> {
            setVerticalScrollAmount(newVerticalScrollAmount);
        });
        horizontalScrollBar = new CScrollBar(null, Orientation.HORIZONTAL);
        horizontalScrollBar.onPositionChanged(newHorizontalScrollAmount -> {
            setHorizontalScrollAmount(newHorizontalScrollAmount);
        });
    }


    public void setHorizontalScrollingEnabled(boolean enabled) {
        horizontalScrollingEnabled = enabled;
    }


    public boolean getHorizontalScrollingEnabled() {
        return horizontalScrollingEnabled;
    }


    //
    // === Layout ===
    //


    @Override
    public void setX(int x) {
        super.setX(x);
        verticalScrollBar.setX(getCalculatedX() + width - verticalScrollBar.getWidth());
        horizontalScrollBar.setX(getCalculatedX());
    }


    @Override
    public void setY(int y) {
        super.setY(y);
        verticalScrollBar.setY(getCalculatedY());
        horizontalScrollBar.setY(getCalculatedY() + height - horizontalScrollBar.getHeight());
    }


    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        verticalScrollBar.setX(getCalculatedX() + width - verticalScrollBar.getWidth());
        horizontalScrollBar.setWidth(width);
    }


    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        verticalScrollBar.setHeight(height);
        horizontalScrollBar.setY(getCalculatedY() + height - verticalScrollBar.getHeight());
    }


    protected int verticalScrollBarWidthUsed() {
        if (!horizontalScrollingEnabled) return 0;
        if (verticalScrollBar.getMaxScroll() == 0) return 0;
        return verticalScrollBar.getWidth();
    }


    protected int horizontalScrollBarHeightUsed() {
        if (!horizontalScrollingEnabled) return 0;
        if (horizontalScrollBar.getMaxScroll() == 0) return 0;
        return horizontalScrollBar.getHeight();
    }


    @Override
    public void layout() {
        verticalScrollBar.setX(getCalculatedX() + width - verticalScrollBar.getWidth());
        verticalScrollBar.setY(getCalculatedY());
        verticalScrollBar.setHeight(height);
        verticalScrollBar.setMax(Math.max(0, getContentHeight()));
        verticalScrollBar.setScrollAmount(getVerticalScrollAmount());

        horizontalScrollBar.setX(getCalculatedX());
        horizontalScrollBar.setY(getCalculatedY() + height - horizontalScrollBar.getHeight());
        horizontalScrollBar.setWidth(width);
        horizontalScrollBar.setMax(Math.max(0, getContentWidth()));
        horizontalScrollBar.setScrollAmount(getHorizontalScrollAmount());
    }


    //
    // === Rendering ===
    //


    @Override
    public void render(BaseRenderer renderer, CMouseEvent mouse) {
        if (this.isVisible()) {
            renderX = getCalculatedX();
            renderY = getCalculatedY();
            int renderWidth = getWidth();
            int renderHeight = getHeight();
            if (containerHasBackground) {
                renderer.fill(renderX, renderY + topOffset, renderX + renderWidth, renderY + renderHeight, containerBackgroundColor);
            }
            if (content != null) {
                renderer.enableClipping(renderX, renderY + topOffset, renderX + width, renderY + height);
                content.render(renderer, mouse);
                renderer.disableClipping();
                verticalScrollBar.render(renderer, mouse);
                if (horizontalScrollingEnabled) {
                    horizontalScrollBar.render(renderer, mouse);
                }
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
        this.setVerticalScrollAmount(this.getVerticalScrollAmount() + mouse.getVerticalAmount() * 0.05 * this.getContentHeight() / 2.0);
        this.setHorizontalScrollAmount(this.getHorizontalScrollAmount() + mouse.getHorizontalAmount() * 0.05 * this.getContentWidth() / 2.0);
        verticalScrollBar.setScrollAmount(verticalScrollAmount);
        horizontalScrollBar.setScrollAmount(horizontalScrollAmount);
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


    // This should be called for web page type widgets but not list boxes etc
    protected synchronized void calculateWidth() {
        if (this.content != null){
            if (contentWidth == 0) {
                int widest = 0;
                for (CWidget widget : content.contents()) {
                    if (widget.getX() + widget.getWidth() > widest) {
                        widest = widget.getX() + widget.getWidth() ;
                    }
                }
                contentWidth = widest;
            }
            content.setWidth(contentWidth);
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


    // This should be called for listbox type widgets, but not web pages etc
    public synchronized void setContentWidth(int width) {
        contentWidth = width;
        if (content != null) {
            content.setWidth(width);
            horizontalScrollBar.setMax(Math.max(0, getContentWidth()));
        }
    }


    public synchronized int getContentHeight() {
        return contentHeight;
    }


    public synchronized int getContentWidth() {
        return contentWidth;
    }


    public int getVerticalMaxScroll() {
        int mp = this.getVerticalMaxPosition();
        return Math.max(0, mp - (this.getHeight()));
    }


    public int getHorizontalMaxScroll() {
        int mp = this.getHorizontalMaxPosition();
        return Math.max(0, mp - (this.getWidth()));
    }


    protected int getVerticalMaxPosition() {
        return this.getContentHeight();
    }


    protected int getHorizontalMaxPosition() {
        return this.getContentWidth();
    }


    public synchronized void scrollTo(CWidget widget){
        if (widget != null) {
            if (widget.getY() - this.verticalScrollAmount < 0) {
                setVerticalScrollAmount(Math.max(0, widget.getY()));
            } else if (widget.getY() + widget.getHeight() - this.verticalScrollAmount > this.getHeight()) {
                setVerticalScrollAmount(Math.min(getVerticalMaxScroll(), widget.getY() + widget.getHeight() - this.getHeight()));
            }
        }
        PlatformServices.getInstance().render(this);
    }
    

    public double getVerticalScrollAmount() {
    	return verticalScrollAmount;
    }


    public void setVerticalScrollAmount(double amount) {
    	verticalScrollAmount = Math.clamp(amount, 0.0, getVerticalMaxScroll());
        updateContentPosition();
    }


    public void updateContentPosition() {
        if (content != null) {
            content.setX((int)(getCalculatedX() - horizontalScrollAmount));
            content.setY((int)(getCalculatedY() - verticalScrollAmount + topOffset));
        }
    }


    public double getHorizontalScrollAmount() {
    	return horizontalScrollAmount;
    }


    public void setHorizontalScrollAmount(double amount) {
    	horizontalScrollAmount = Math.clamp(amount, 0.0, getHorizontalMaxScroll());
        updateContentPosition();
    }
}
