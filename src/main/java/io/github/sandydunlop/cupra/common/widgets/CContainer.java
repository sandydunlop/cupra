package io.github.sandydunlop.cupra.common.widgets;

import java.util.ArrayList;
import java.util.List;

import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.Align;
import io.github.sandydunlop.cupra.common.util.DepthLimit;
import io.github.sandydunlop.cupra.common.CupraException;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.fonts.FontSpec;


public class CContainer extends CWidget {
    private boolean isHorizontal = false;
	private Align.Horizontal alignHorizontal = Align.Horizontal.LEFT;
	private Align.Vertical alignVertical = Align.Vertical.TOP;
	protected boolean hasBackground = false;
	protected int backgroundColor = CWidget.getPalette().REGULAR_BACKGROUND;
	protected boolean hasBorder = false;
	protected int borderColor = CWidget.getPalette().HOVERED_BORDER;


    public CContainer(CContainer parent, boolean isHorizontal) {
		super(parent);
		if (parent != null){
			parent.add(this);
			this.setMaxHeight(parent.getHeight());
			this.setMaxWidth(parent.getWidth());
		}
		this.parent = parent;
        this.isHorizontal = isHorizontal;
		this.padding = 4;
		this.expandable = true;
	}


    public CContainer(CContainer parent) {
		this(parent, false);
	}

    public CContainer() {
		this(null, false);
	}


	// === Properties ===


	public void setParent(CContainer parent){
		this.parent = parent;
	}

	public FontSpec getFont(){
		return this.font;
	}


	public CContainer setFont(FontSpec font){
		this.font = font;
		return this;
	}


	public CContainer setAlignVertical(Align.Vertical align){
		this.alignVertical = align;
		return this;
	}


	public Align.Vertical getAlignVertical() {
		return this.alignVertical;
	}


	public CContainer setAlignHorizontal(Align.Horizontal align){
		this.alignHorizontal = align;
		return this;
	}


	public Align.Horizontal getAlignHorizontal() {
		return this.alignHorizontal;
	}


	public void setHorizontal(boolean flag){
        isHorizontal = flag;
    }


	public boolean isHorizontal(){
		return this.isHorizontal;
	}


	public void setBackgroundColor(int color) {
		this.backgroundColor = color;
		this.hasBackground = true;
	}


	public void setBorderColor(int color) {
		this.borderColor = color;
		this.hasBorder = true;
	}


	@Override
	public CContainer setDebug(int color) {
		super.setDebug(color);
		int c = color;
		if (c > 0) {
			c = 0xFFFF0000;
		}
		for (CWidget widget : contents){
			if (widget instanceof CSpacer ||
			    widget instanceof CFlexiSpacer ||
				widget instanceof CContainer){
				widget.setDebug(c);
			}
		}
		return this;
	}


	// === Contents and Layout ===

	
	@Override
    public int getCalculatedX() {
        if (parent != null){
            return parent.getCalculatedX() + x;
        }else{
            return x;
        }
    }


	@Override
    public int getCalculatedY(){
        if (parent != null){
            return parent.getCalculatedY() + y;
        }else{
            return y;
        }
    }


	public void clear(){
		this.contents = new ArrayList<CWidget>();
	}


	public void add(CWidget widget){
		this.contents.add(widget);
	}


	@Override
    public int getCalculatedWidth() {
		if (!expandable && width > 0) {
			return width;
		}
		if (!isHorizontal) {
			int widest = 0;
			for (CWidget widget : contents) {
				int w = widget.getCalculatedWidth();
				if (w > widest) {
					widest = w;
				}
			}
			return widest + (padding*2);
		} else {
			int w = 0;
			for(CWidget widget : contents) {
				w += widget.getCalculatedWidth() + padding;
			}
			return w > 0 ? w : getDefaultWidth();
		}
    }


	@Override
    public int getCalculatedHeight() {
		if (!expandable && height > 0) {
			return height;
		}
		if (isHorizontal) {
			int tallest = 0;
			for (CWidget widget : contents) {
				int h = widget.getCalculatedHeight();
				if (h > tallest) {
					tallest = h;
				}
			}
			return tallest + (padding*2);
		} else {
			int h = 0;
			for(CWidget widget : contents) {
				h += widget.getCalculatedHeight() + padding;
			}
			return h > 0 ? h : getDefaultHeight();
		}
    }
	
	
	@Override
	public void layout(){
		if (isHorizontal) {
			layoutHorizontal();
		}else{
			layoutVertical();
		}
	}


	private void layoutVertical() {
		if ("middle".equals(id)){
			id = id;
		}
		int nonexSize = 0;
		List<CWidget> expandableWidgets = new ArrayList<>();
		for (CWidget widget : contents) {
			if (widget.isExpandable()) {
				expandableWidgets.add(widget);
			} else {
				if (widget.getWidth() == 0 || widget instanceof CContainer) {
					widget.setWidth(width  - padding*2> 0 ? width - (padding*2) : widget.getDefaultWidth()); // Why not calculatedWidth?
				}
				int h = widget.getCalculatedHeight();
				if (widget instanceof CContainer) {
					widget.setHeight(h);
				}
				nonexSize += widget.getHeight() + padding;
			}
		}
		if (nonexSize > 0) {
			nonexSize -= padding;
		}
		int nExpandables = expandableWidgets.size();
		int expandableSize = 0;
		if (nExpandables > 0) {
			expandableSize = ((height - (padding) - nonexSize) / nExpandables) - padding;
			for (CWidget widget : this.contents) {
				if (widget.isExpandable()) {
					widget.setHeight(expandableSize);
					widget.setWidth(width - (padding*2));
				}
			}
		}

		int pos = padding;
		int widgetSpacing = 0;
		if (expandableSize == 0) {
			if (alignVertical == Align.Vertical.SPREAD) {
				int remainingSpace = height - (padding*2) - nonexSize;
				widgetSpacing = remainingSpace / (contents.size() + 1);
				pos += widgetSpacing;
			} else if (alignVertical == Align.Vertical.MIDDLE) {
				pos += (height - (padding*2))/2 - nonexSize/2;
			}
		}
		for (CWidget widget : contents) {
			widget.setX(padding);
			widget.setY(pos);
			pos += widget.getHeight() + widgetSpacing + padding;
		}
		for (CWidget widget : contents) {
			widget.layout();
		}
	}


	private void layoutHorizontal() {
		int nonexSize = 0;
		List<CWidget> expandableWidgets = new ArrayList<>();
		for (CWidget widget : this.contents) {
			if (widget.isExpandable()) {
				expandableWidgets.add(widget);
			} else {
				int w = widget.getCalculatedWidth();
				if (widget instanceof CContainer) {
					widget.setWidth(w);
				}
				if (widget.getHeight() == 0 || widget instanceof CContainer) {
					widget.setHeight(height > 0 ? height - (padding*2) : widget.getDefaultHeight());
				}
				nonexSize += widget.getWidth() + padding;
			}
		}
		if (nonexSize > 0) {
			nonexSize -= padding;
		}
		int nExpandables = expandableWidgets.size();
		int expandableSize = 0;
		if (nExpandables > 0) {
			expandableSize = ((width - (padding) - nonexSize) / nExpandables) - padding;
			for (CWidget widget : contents) {
				if (widget.isExpandable()) {
					widget.setWidth(expandableSize);
					widget.setHeight(height - (padding*2));
				}
			}
		}

		int pos = padding;
		int widgetSpacing = 0;
		if (expandableSize == 0) {
			if (alignHorizontal == Align.Horizontal.SPREAD) {
				int remainingSpace = width - (padding*2) - nonexSize;
				widgetSpacing = remainingSpace / (contents.size() + 1);
				pos += widgetSpacing;
			} else if (alignHorizontal == Align.Horizontal.MIDDLE) {
				pos += (width - (padding*2))/2 - nonexSize/2;
			}
		}
		for (CWidget widget : contents) {
			widget.setX(pos);
			widget.setY(padding);
			pos += widget.getWidth() + widgetSpacing + padding;
		}
		for (CWidget widget : contents) {
			widget.layout();
		}
	}


	@Override
	public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) throws CupraException {
        if (this.isVisible()) {
			int renderWidth = getWidth()>0 ? getWidth() - (padding*2): 80;
			int renderHeight = getHeight()>0 ? getHeight() - (padding*2): 20;
			if (this.hasBackground) {
				renderer.fill(getCalculatedX(), getCalculatedY(), getCalculatedX() + renderWidth, getCalculatedY() + renderHeight, backgroundColor);
			}
			for (int i = 0; i < contents.size(); i++) {
				CWidget widget = contents.get(i);
				int widgetY = widget.getCalculatedY();
				if (widgetY > (this.getCalculatedY() + this.getHeight()*2)){ //TODO: This *2 is dodgy, but it works for now
					break;
				}
				if (widgetY + widget.getHeight() >= 0) {
					widget.render(renderer, mouseX, mouseY, delta);
				}
			}
			if (this.hasBorder) {
				renderer.drawRectangle(getCalculatedX(), getCalculatedY(), getCalculatedX() + renderWidth, getCalculatedY() + renderHeight, borderColor);
			}
		}
		if (this.getDebug() != 0){
			renderer.drawRectangle(getCalculatedX(), getCalculatedY(), getCalculatedX()+getWidth(), getCalculatedY()+getHeight(), this.getDebug());
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
	
	
    public String toString(){
        return "CContainer [" + contents.size() + "]";
    }
}
