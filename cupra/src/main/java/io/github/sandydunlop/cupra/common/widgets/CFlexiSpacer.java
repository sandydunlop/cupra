package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.render.BaseRenderer;


public class CFlexiSpacer  extends CWidget {
    public CFlexiSpacer(CContainer parent) {
        super(parent);
        this.setExpandable(true);
        parent.add(this);
    }


    @Override
    public void render(BaseRenderer renderer, int mouseX, int mouseY, float delta) {
        if (this.getDebug() != 0){
            renderer.drawRectangle(getCalculatedX(), getCalculatedY(), getCalculatedX()+getWidth(), getCalculatedY()+getHeight(), this.getDebug());
        }
    }
}
