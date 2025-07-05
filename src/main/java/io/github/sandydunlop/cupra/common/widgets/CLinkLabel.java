package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.util.MousePointer;


public class CLinkLabel extends CLabel{
    private String title = null;
    private String url = null;    
    private CPressAction onClickLink;



	public CLinkLabel(CContainer parent, String text, String title, String url, CPressAction onClickLink) {
		super(parent, text);
        this.title = title;
        this.url = url;
        this.onClickLink = onClickLink;
        this.mousePointer = MousePointer.POINTING_HAND;
	}


    public void setTitle(String title){
        this.title = title;
    }


    public String getTitle(){
        return this.title;
    }


    public void setUrl(String url){
        this.url = url;
    }


    public String getUrl(){
        return this.url;
    }


    public interface CPressAction {
        void onClickLink(CLinkLabel click);
    }


    @Override
    public boolean mousePressed(CMouseEvent mouse) {
        if (this.enabled && this.visible) {
            if (this.isMouseOver(mouse)) {
                this.onClickLink.onClickLink(this);
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
}
