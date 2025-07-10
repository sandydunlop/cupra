package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.CupraException;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.DepthLimit;

public class CGrid extends CContainer {
    private int horizontalPadding = 5;
    private int verticalPadding = 5;
    private int widthInCells;


    public CGrid(CContainer parent, int widthInCells){
        super(parent);
        this.parent = parent;
        this.widthInCells = widthInCells;
    }

    
    public CGrid(CContainer parent){
        this(parent, 1);
    }


    public void setWidthInCells(int widthInCells){
        this.widthInCells = widthInCells;
    }


    public int getWidthInCells(){
        return widthInCells;
    }


    public int getHhorizontalPadding(){
        return this.horizontalPadding;
    }


	public void add(CWidget widget){
		contents.add(widget);
	}


    @Override
	public void layout(){
        if (widthInCells > 0){
            int cellHeight = (int)(this.width / widthInCells);
            this.height = (((int)((contents.size()+1) / widthInCells)))* cellHeight;
        }
    }


    @Override
    public CWidget hoveredWidget(CMouseEvent mouse, DepthLimit depth) {
        CWidget hovered = null;
		for (CWidget widget : contents()) {
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
	public void render(BaseRenderer renderer, CMouseEvent mouse) {
		if (this.getDebug() != 0){
			int renderWidth = getWidth()>0 ? getWidth() : 80;
			int renderHeight = getHeight()>0 ? getHeight() : 20;
			int borderColor = this.getDebug(); //CWidget.palette.SELECTED_BACKGROUND;
			renderer.drawHorizontalLine(getX(), getX() + renderWidth, getY(), borderColor);
			renderer.drawHorizontalLine(getX(), getX() + renderWidth, getY() + renderHeight, borderColor);
			renderer.drawVerticalLine(getX(), getY(), getY() + renderHeight, borderColor);
			renderer.drawVerticalLine(getX() + renderWidth, getY() + renderHeight, getY(), borderColor);
		}
        int cellWidth = 0;
        if (widthInCells > 0){
            float a = (this.getWidth() )/ widthInCells;
            cellWidth =  (int)(a - horizontalPadding);
        }
        int cellX = 0;
        int cellY = 0;
        int onThisRow=0;
        for (int i = 0; i < contents.size(); i++){
            CWidget widget = contents.get(i);
            widget.setX(cellX);
            widget.setY(cellY);
            if (this.getDebug() != 0){
                int renderWidth = cellWidth;
                int renderHeight = cellWidth;
                int borderColor = CWidget.getPalette().HOVERED_BORDER;
                renderer.drawHorizontalLine(this.getCalculatedX() + cellX, this.getCalculatedX() + cellX + renderWidth, this.getCalculatedY() + cellY, borderColor);
                renderer.drawHorizontalLine(this.getCalculatedX() + cellX, this.getCalculatedX() + cellX + renderWidth, this.getCalculatedY() + cellY+ renderHeight, borderColor);
                renderer.drawVerticalLine(this.getCalculatedX() + cellX, this.getCalculatedY() + cellY, this.getCalculatedY() + cellY + renderHeight, borderColor);
                renderer.drawVerticalLine(this.getCalculatedX() + cellX + renderWidth, this.getCalculatedY() + cellY + renderHeight, this.getCalculatedY() + cellY, borderColor);
            }
            widget.setWidth(cellWidth);
            widget.setHeight(cellWidth);
            widget.render(renderer, mouse);
            onThisRow++;
            cellX += cellWidth + horizontalPadding;
            if (onThisRow >= widthInCells){
                onThisRow = 0;
                cellX = getX();
                cellY += cellWidth + verticalPadding;
            }
            this.setHeight(cellY + cellWidth);
		}
    }
}
