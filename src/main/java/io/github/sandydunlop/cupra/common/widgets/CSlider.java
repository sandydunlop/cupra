package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.Orientation;


public class CSlider extends CAbstractScrollable {
    private int scrollerOverhang = 8;
    private boolean invert = false;


    public CSlider(CContainer parent) {
        this(parent, Orientation.HORIZONTAL);
    }


    public CSlider(CContainer parent, Orientation orientation) {
        super(parent, orientation);
        if (parent != null) {
            parent.add(this);
        }
        invert = (orientation == Orientation.VERTICAL);
        defaultThickness = 20;
        width = getDefaultWidth();
        height = getDefaultHeight();
        setValue(0);
    }


    public void setValue(double value) {
        if (invert) {
            scrollAmount = max - value;
        } else {
            scrollAmount = value;
        }
    }


    public double getValue() {
        if (invert) {
            return max - scrollAmount;
        } else {
            return scrollAmount;
        }
    }


    //
    // Layout & Rendering
    //


    @Override
	public void layout(){
    }


    @Override
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) {
        if (getMax() > 0) {
            int renderX = getCalculatedX();
            int renderY = getCalculatedY();
            int renderSize = getRenderSize();
            int renderThickness = getRenderThickness();
            int renderPosition = getRenderPosition();
            int scrollerSize = getScrollerSize();
            int scrollerStart = getScrollerStart(renderSize, renderPosition, scrollerSize);

            if (orientation == Orientation.HORIZONTAL) {
                renderer.fill(renderX, renderY + scrollerOverhang, 
                        scrollerStart, renderY + renderThickness - scrollerOverhang, 
                        CWidget.getPalette().HOVERED_BACKGROUND);
                renderer.fill(scrollerStart, renderY + scrollerOverhang, 
                        renderX + renderSize, renderY + renderThickness - scrollerOverhang, 
                        CWidget.getPalette().INPUT_BACKGROUND);
                renderer.drawRectangle(renderX, renderY + scrollerOverhang, 
                        renderX + renderSize, renderY + renderThickness - scrollerOverhang, 
                        CWidget.getPalette().SELECTED_BACKGROUND);
                renderer.fill(scrollerStart, renderY, 
                        scrollerStart + scrollerSize, renderY + renderThickness, 
                        CWidget.getPalette().HOVERED_BACKGROUND);
                renderer.drawRectangle(scrollerStart, renderY, 
                        scrollerStart + scrollerSize, renderY + renderThickness, 
                        CWidget.getPalette().SELECTED_BACKGROUND);
            } else {
                renderer.fill(renderX + scrollerOverhang, renderY, 
                        renderX + renderThickness - scrollerOverhang, scrollerStart, 
                        CWidget.getPalette().INPUT_BACKGROUND);
                renderer.fill(renderX + scrollerOverhang, scrollerStart,
                        renderX + renderThickness - scrollerOverhang, renderY + renderSize, 
                        CWidget.getPalette().HOVERED_BACKGROUND);
                renderer.drawRectangle(renderX + scrollerOverhang, renderY, 
                        renderX + renderThickness - scrollerOverhang, renderY + renderSize, 
                        CWidget.getPalette().SELECTED_BACKGROUND);
                renderer.fill(renderX, scrollerStart, 
                        renderX + renderThickness, scrollerStart + scrollerSize, 
                        CWidget.getPalette().HOVERED_BACKGROUND);
                renderer.drawRectangle(renderX, scrollerStart, 
                        renderX + renderThickness, scrollerStart + scrollerSize, 
                        CWidget.getPalette().SELECTED_BACKGROUND);
            }
        }

        if (getDebug() != 0){
            renderer.drawRectangle(getCalculatedX(), getCalculatedY(), getCalculatedX()+getWidth(), getCalculatedY()+getHeight(), this.getDebug());
        }
    }


    protected int getScrollerSize() {
        return 10;
    }
}
