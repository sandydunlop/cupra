package io.github.sandydunlop.cupra.common.input;

import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.util.DepthLimit;
import io.github.sandydunlop.cupra.common.widgets.CWidget;


public interface MouseInput {
    public CWidget hoveredWidget(CMouseEvent mouse, DepthLimit depth);
    public boolean isMouseOver(CMouseEvent mouse);
    public boolean mousePressed(CMouseEvent mouse);
    public boolean mouseReleased(CMouseEvent mouse);
    public boolean mouseClicked(CMouseEvent mouse);
    public boolean mouseDoubleClicked(CMouseEvent mouse);
    public boolean mouseTripleClicked(CMouseEvent mouse);
    public boolean mouseScrolled(CMouseEvent mouse);
    public boolean mouseHovered(CMouseEvent mouse);
    public boolean mouseDragged(CMouseEvent mouse);
}
