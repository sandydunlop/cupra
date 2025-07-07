package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.DepthLimit;
import io.github.sandydunlop.cupra.common.util.Orientation;


public abstract class CAbstractScrollable extends CWidget {
    protected int defaultThickness = 16;
    protected int defaultSize = 200;
    protected Orientation orientation = Orientation.HORIZONTAL;
    protected PositionChangedAction onPositionChanged;
    protected boolean showButtons = false;
    protected double min = 0;
    protected double max = 100;
    protected int contentHeight = 0;
    protected double scrollAmount = 0;
    protected boolean scrolling = false;
    private double mousePressedPosition = 0;
    private double mousePressedScrollAmount = 0;


    protected CAbstractScrollable(CContainer parent) {
        this(parent, Orientation.HORIZONTAL);
    }


    protected CAbstractScrollable(CContainer parent, Orientation orientation) {
        super(parent);
        this.orientation = orientation;
        this.width = getDefaultWidth();
        this.height = getDefaultHeight();
    }


    public interface PositionChangedAction {
        void onPositionChanged(double position);
    }


    public void onPositionChanged(PositionChangedAction action) {
        onPositionChanged = action;
    }


    public void setShowButtons(boolean show) {
        showButtons = show;
    }


    public boolean getShowButtons() {
        return showButtons;
    }


    public void setMin(double min) {
        this.min = min;
    }


    public double getMin() {
        return min;
    }


    public void setMax(double max) {
        this.max = max;
    }


    public double getMax() {
        return max;
    }


    protected double getMaxScroll() {
        return Math.max(0, getMax() - getHeight());
    }


    @Override
    public int getDefaultWidth() {
        return orientation == Orientation.HORIZONTAL ? defaultSize : defaultThickness;
    }


    @Override
    public int getDefaultHeight() {
        return orientation == Orientation.HORIZONTAL ? defaultThickness : defaultSize;
    }


    protected double getScrollAmount() {
    	return scrollAmount;
    }


    protected void setScrollAmount(double amount) {
    	scrollAmount = Math.clamp(amount, 0.0, getMaxScroll());
    }


    protected void updateScrollingState(CMouseEvent mouse) {
    	scrolling = mouse.getButton() == CMouseEvent.PRIMARY_BUTTON && isMouseOver(mouse);
    }


    //
    // Layout & Rendering
    //


    @Override
	public void layout(){
    }


    @Override
    public abstract void render(BaseRenderer renderer, int mouseX, int mouseY, float delta);


    protected int getRenderSize() {
        return orientation == Orientation.HORIZONTAL ? getWidth() : getHeight();
    }


    protected int getRenderThickness() {
        return orientation == Orientation.HORIZONTAL ? getHeight() : getWidth();
    }


    protected int getRenderPosition() {
        return orientation == Orientation.HORIZONTAL ? getCalculatedX() : getCalculatedY();
    }


    protected int getScrollerStart(int renderSize, int renderPosition, int scrollerSize) {
        int scrollerStart = (int)scrollAmount * (renderSize - scrollerSize) / (int)getMaxScroll() + renderPosition;
        if (scrollerStart < renderPosition) {
            scrollerStart = renderPosition;
        }
        return scrollerStart;
    }


    protected int getScrollerSize() {
        int renderSize = getRenderSize();
        int scrollerSize = (int)(renderSize * renderSize / getMax());
        return Math.clamp(scrollerSize, 32, renderSize);
    }


    //
    // === Mouse ===
    //

    
    @Override
	public CWidget hoveredWidget(CMouseEvent mouse, DepthLimit depth) {
        if (depth == DepthLimit.SCROLLABLE) {
            return this;
        }
        CWidget hovered = null;
		for (CWidget widget : contents) {
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


    @Override
    public boolean mouseReleased(CMouseEvent mouse) {
        scrolling = false;
        return false;
    }


    @Override
    public boolean mousePressed(CMouseEvent mouse) {
        super.mousePressed(mouse);
    	this.updateScrollingState(mouse);
        if (orientation == Orientation.HORIZONTAL) {
            mousePressedPosition = mouse.getX();
        } else {
            mousePressedPosition = mouse.getY();
        }
        mousePressedScrollAmount = getScrollAmount();
        return true;
    }
        
    
    @Override
    public boolean mouseDragged(CMouseEvent mouse) {
        if (mouse.getButton() != CMouseEvent.PRIMARY_BUTTON || !scrolling) return false;
        int renderSize = getRenderSize();
        double mousePosition = orientation == Orientation.HORIZONTAL ? mouse.getX() : mouse.getY();
        double howMuchItCanMove = (double)renderSize - getScrollerSize();
        double dragDistance = mousePosition - mousePressedPosition;
        double ratioDistanceToAmount = getMaxScroll() / howMuchItCanMove;
        double scrollDistance = dragDistance * ratioDistanceToAmount;
        double newScrollAmount = mousePressedScrollAmount + scrollDistance;
        setScrollAmount(newScrollAmount);
        if (onPositionChanged != null) {
            this.onPositionChanged.onPositionChanged(scrollAmount);
        }
        return true;
    }


    @Override
    public boolean mouseScrolled(CMouseEvent mouse) {
        double change = mouse.getVerticalAmount() * 0.05 * getMax() / 2.0;
        double newScrollAmount = getScrollAmount() + change;
        setScrollAmount(newScrollAmount);
        if (onPositionChanged != null) {
            this.onPositionChanged.onPositionChanged(scrollAmount);
        }
        return true;
    }
}
