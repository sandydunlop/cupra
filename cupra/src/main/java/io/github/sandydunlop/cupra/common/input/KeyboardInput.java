package io.github.sandydunlop.cupra.common.input;

import io.github.sandydunlop.cupra.common.events.CKeyEvent;


public interface KeyboardInput {
    public void keyPressed(CKeyEvent k);
	public void charTyped(CKeyEvent e);
}
