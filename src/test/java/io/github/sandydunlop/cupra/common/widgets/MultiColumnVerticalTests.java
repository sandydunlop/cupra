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

class MultiColumnVerticalTests {
    private static final int LAYOUT_ALGORITHM = 2;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static CContainer screen;
	private static CContainer body;
	private static CContainer middle;
	private static CContainer middle2;
	private static CButton button1m;
	private static CButton button2m;
	private static CButton button1m2;
	private static CButton button2m2;
    
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

		middle2 = new CContainer(body, false);
		middle2.setId("middle2");
		middle2.setPadding(0);
		middle2.setExpandable(false);
		button1m2 = new CButton(middle2, "button1m2", null);
		button2m2 = new CButton(middle2, "button2m2", null);

        screen.layout();
    }

    @BeforeEach
    void init() {
    }

    @Test
    void columnOrder() {
        assertTrue(middle2.getX() > middle.getX());
    }

    @Test
    void buttonOrder() {
        assertTrue(button2m2.getY() > button1m2.getY());
        assertEquals(button1m.getY(), button1m2.getY());
        assertEquals(button2m.getY(), button2m2.getY());
    }

    @Test
    void position() {
        assertEquals(middle2.getX(), button1m2.getCalculatedX());
        assertEquals(middle2.getX(), button2m2.getCalculatedX());
        assertEquals(middle2.getY(), button1m2.getCalculatedY());
        assertEquals(middle2.getY() + button1m2.getHeight(), button2m2.getCalculatedY());
    }

    @Test
    void containerSize() {
        assertEquals(button2m2.getWidth(), middle2.getWidth()); //button2m is the wider one
        // assertEquals(button1m2.getHeight() + button2m2.getHeight(), middle2.getHeight());
        assertEquals(SCREEN_HEIGHT, middle2.getHeight());
        assertEquals(middle.getHeight(), middle2.getHeight());
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }}
