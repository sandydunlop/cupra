package io.github.sandydunlop.cupra.common.widgets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import io.github.sandydunlop.cupra.common.CupraException;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.render.BaseRenderer;


public class CImage extends CWidget {
    BufferedImage icon = null;
    String identifier;


    public CImage(CContainer parent){
        super(parent);
		if (parent != null){
			parent.add(this);
        }
    }


    public void fromResource(String res) {
        identifier = res;
        icon = getIcon(res);
    }


    private BufferedImage getIcon(String iconName){
        BufferedImage img = null;
        try {
            URL iconUrl = CImage.class.getClassLoader().getResource(iconName);
            img = ImageIO.read(iconUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }


    @Override
    public void render(BaseRenderer renderer, CMouseEvent mouse) {
        int iconWidth = getWidth();
        int iconHeight = getHeight();
        int iconX = getCalculatedX();
        int iconY = getCalculatedY();
        if (icon != null) {
            renderer.drawImage(identifier, icon, iconX, iconY, iconWidth, iconHeight);
        }
    }
}
