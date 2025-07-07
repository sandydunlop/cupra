package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.Orientation;
import io.github.sandydunlop.cupra.platform.PlatformServices;


public class CScrollBar extends CAbstractScrollable {
    protected boolean showButtons = false;


    public CScrollBar(CContainer parent) {
        this(parent, Orientation.HORIZONTAL);
    }


    public CScrollBar(CContainer parent, Orientation orientation) {
        super(parent);
        if (parent != null) {
            parent.add(this);
        }
        this.defaultThickness = 6;
        this.orientation = orientation;
        this.width = getDefaultWidth();
        this.height = getDefaultHeight();
    }


    public void setShowButtons(boolean show) {
        showButtons = show;
    }


    public boolean getShowButtons() {
        return showButtons;
    }


    //
    // Layout & Rendering
    //


    @Override
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) {
        if (getMaxScroll() > 0) {
            int renderX = getCalculatedX();
            int renderY = getCalculatedY();
            int renderSize = getRenderSize();
            int renderThickness = getRenderThickness();
            int renderPosition = getRenderPosition();
            int scrollerSize = getScrollerSize();
            int scrollerStart = getScrollerStart(renderSize, renderPosition, scrollerSize);
            if (orientation == Orientation.HORIZONTAL) {
                renderer.fill(renderX, renderY, 
                        renderX + renderSize, renderY + renderThickness, 
                        CWidget.getPalette().INPUT_BACKGROUND);
                renderer.fill(scrollerStart, renderY, 
                        scrollerStart + scrollerSize, renderY + renderThickness, 
                        CWidget.getPalette().HOVERED_BACKGROUND);
            } else {
                renderer.fill(renderX, renderY, renderX + 
                        renderThickness, renderY + renderSize, 
                        CWidget.getPalette().INPUT_BACKGROUND);
                renderer.fill(renderX, scrollerStart, 
                        renderX + renderThickness, scrollerStart + scrollerSize, 
                        CWidget.getPalette().HOVERED_BACKGROUND);
            }
        }

        if (getDebug() != 0){
            renderer.drawRectangle(getCalculatedX(), getCalculatedY(), getCalculatedX()+getWidth(), getCalculatedY()+getHeight(), this.getDebug());
        }
    }


    public synchronized void scrollTo(CWidget widget){
        if (widget != null) {
            if (widget.getY() - this.scrollAmount < 0) {
                scrollAmount = Math.max(0, widget.getY());
            } else if (widget.getY() + widget.getHeight() - scrollAmount > getHeight()) {
                scrollAmount = Math.min(getMax(), widget.getY() + widget.getHeight() - getHeight());
            }
        }
        PlatformServices.getInstance().render(this);
    }
}
