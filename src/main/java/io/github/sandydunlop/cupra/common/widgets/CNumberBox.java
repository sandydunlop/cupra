package io.github.sandydunlop.cupra.common.widgets;

import java.text.DecimalFormat;

import io.github.sandydunlop.cupra.common.CupraException;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.fonts.BitmapFont;
import io.github.sandydunlop.cupra.common.fonts.BitmapFontFactory;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;
import io.github.sandydunlop.cupra.common.util.DepthLimit;
import io.github.sandydunlop.cupra.common.util.Math;
import io.github.sandydunlop.cupra.common.util.Symbol;


public class CNumberBox extends CWidget{
    protected CTextBox textbox= null;
    protected CSymbolButton upButton = null;
    protected CSymbolButton downButton = null;
    protected int buttonWidth = 15;
    protected NumberChangedAction onNumberChanged = null;
    protected double number = 0;
    protected int precision = 0;
    protected double increment = 1;
    protected double min = 0;
    protected double max = 0;
    protected boolean rangeConstrained = false;
    protected String decimalFormat = "#";


    public CNumberBox(CContainer parent) {
        super(parent);
        this.setExpandable(false);
        this.setPadding(0);
        if (parent != null) {
            parent.add(this);
        }
        textbox = new CTextBox(null, "0");
        textbox.onKeyPressed(key ->{
            updateNumber();
            updateTextBox();
        });
        upButton = new CSymbolButton(null, Symbol.UP, click ->{
            updateNumber();
            number += increment;
            updateTextBox();
        });
        downButton = new CSymbolButton(null, Symbol.DOWN, click ->{
            updateNumber();
            number -= increment;
            updateTextBox();
        });
    }


    /**
     * Sets the width of the number box component.
     * <p>
     *
     * @param width the total width to set for the number box, in pixels
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        textbox.setWidth(width - buttonWidth);
    }


    public void setEditable(boolean editable) {
        textbox.setEditable(editable);
    }


    public boolean getEditable() {
        return textbox.editable;
    }


    public void setPrecision(int digits) {
        precision = digits;
        decimalFormat = "#";
        if (digits > 0) {
            decimalFormat += ".";
            for (int i=0; i<digits; i++) {
                decimalFormat += "0";
            }
        }
    }


    public int getPrecision() {
        return precision;
    }


    public void setIncrement(double increment) {
        this.increment = increment;
    }


    public double getIncrement() {
        return increment;
    }


    public void setMin(double min) {
        this.min = min;
        this.max = Math.max(this.min, max);
        this.rangeConstrained = true;
    }


    public double getMin() {
        return min;
    }


    public void setMax(double max) {
        this.max = max;
        this.min = Math.min(this.min, max);
        this.rangeConstrained = true;
    }


    public double getMax() {
        return max;
    }


    public void setNumber(double number) {
        this.number = number;
        updateTextBox();
    }


    public double getDouble() {
        return number;
    }


    public float getFloat() {
        return (float)number;
    }


    public int getInt() {
        return (int)number;
    }


    public void setRangeConstrained(boolean constrained) {
        this.rangeConstrained = constrained;
    }


    private void updateTextBox() {
        DecimalFormat df = new DecimalFormat(decimalFormat); // 2 digits after decimal
        textbox.setText(df.format(number));
    }


    private void updateNumber() {
        try {
            number = Double.parseDouble(textbox.getText());
        } catch(NumberFormatException ignore) {
            // Do nothing
        }
    }

    
    @Override
    public void layout() {
        BitmapFont bmf = BitmapFontFactory.load(font);
        this.height = bmf.getHeight() + (CTextBox.TEXT_PADDING*2);
        this.textbox.setWidth(this.width - buttonWidth);
        this.textbox.setHeight(this.height);
        this.upButton.setWidth(buttonWidth);
        this.downButton.setWidth(buttonWidth);
        this.upButton.setHeight(this.height/2);
        this.downButton.setHeight(this.height/2);
    }


    private void positionChildren() {
        int absoluteX = getCalculatedX();
        int absoluteY = getCalculatedY();
        textbox.setX(absoluteX);
        textbox.setY(absoluteY);
        upButton.setX(absoluteX + width - buttonWidth);
        upButton.setY(absoluteY);
        downButton.setX(absoluteX + width - buttonWidth);
        downButton.setY(absoluteY+(this.height/2));
    }


    @Override
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) {
        super.render(renderer, mouseX, mouseY, delta);
        positionChildren();
        textbox.render(renderer, mouseX, mouseY, delta);
        upButton.render(renderer, mouseX, mouseY, delta);
        downButton.render(renderer, mouseX, mouseY, delta);
        if (this.getDebug() != 0){
            renderer.drawRectangle(getCalculatedX(), getCalculatedY(), getCalculatedX()+getWidth(), getCalculatedY()+getHeight(), this.getDebug());
        }
    }


	@Override
	public CWidget hoveredWidget(CMouseEvent mouse, DepthLimit depth) {
        if (depth != DepthLimit.COMPONENT) {
            return this;
        }
        if (textbox.isMouseOver(mouse)){
            return textbox;
        }else if (upButton.isMouseOver(mouse)){
            return upButton;
        }else if (downButton.isMouseOver(mouse)){
            return downButton;
        }
        return null;
    }


    public interface NumberChangedAction {
        void onNumberChanged(CNumberBox number);
    }


    public void onNumberChanged(NumberChangedAction action) {
        this.onNumberChanged = action;
    }
}
