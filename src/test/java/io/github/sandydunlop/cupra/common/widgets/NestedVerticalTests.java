package io.github.sandydunlop.cupra.common.widgets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.sandydunlop.cupra.common.widgets.CButton;
import io.github.sandydunlop.cupra.common.widgets.CContainer;

class NestedVerticalTests {
    private static final int LAYOUT_ALGORITHM = 2;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static CContainer screen;
	private static CContainer body;
	private static CContainer middle;
	private static CButton button1m;
	private static CButton button2m;
    
    @BeforeAll
    static void initAll() {
        screen = new CContainer(null);
        screen.setWidth(SCREEN_WIDTH);
        screen.setHeight(SCREEN_HEIGHT);
        screen.setPadding(0);
		screen.setLayoutAlgorithm(LAYOUT_ALGORITHM);

		body = new CContainer(screen, true);
		body.setPadding(0);
		body.setExpandable(true);

		middle = new CContainer(body, false);
		middle.setId("middle");
		middle.setPadding(0);
		middle.setExpandable(false);
		button1m = new CButton(middle, "button1m", null);
		button2m = new CButton(middle, "button2m", null);

        screen.layout();
    }

    @BeforeEach
    void init() {
    }

    @Test
    void buttonOrder() {
        assertTrue(button2m.getY() > button1m.getY());
    }

    @Test
    void position() {
        assertEquals(0, button1m.getX());
        assertEquals(0, button2m.getX());
    }

    @Test
    void containerSize() {
        assertEquals(button2m.getWidth(), middle.getWidth()); //button2m is the wider one
        assertEquals(SCREEN_HEIGHT, middle.getHeight());
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }}
