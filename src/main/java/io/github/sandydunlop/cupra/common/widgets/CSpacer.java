package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.render.BaseRenderer;


public class CSpacer extends CWidget {
    int gap;

    public CSpacer(CContainer parent, int gap) {
        super(parent);
        this.gap = gap;
        parent.add(this);
        layout();
    }


    @Override
	public void layout(){
        if (parent.isHorizontal()){
            this.width = this.gap;
        }else{
            this.height = this.gap;
        }
    }


    @Override
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) {
        if (this.getDebug() != 0){
            renderer.drawRectangle(getCalculatedX(), getCalculatedY(), getCalculatedX()+getWidth(), getCalculatedY()+getHeight(), this.getDebug());
        }
    }
}
