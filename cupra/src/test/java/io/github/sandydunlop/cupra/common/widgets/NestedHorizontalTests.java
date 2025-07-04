package io.github.sandydunlop.cupra.common.widgets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.sandydunlop.cupra.common.widgets.CButton;
import io.github.sandydunlop.cupra.common.widgets.CContainer;

class NestedHorizontalTests {
    private static final int LAYOUT_ALGORITHM = 2;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static CContainer screen;
	private static CContainer footer;
	private static CContainer buttons;
    private static CButton button1;
    private static CButton button2;

    @BeforeAll
    static void initAll() {
        screen = new CContainer(null);
        screen.setWidth(SCREEN_WIDTH);
        screen.setHeight(SCREEN_HEIGHT);
        screen.setPadding(0);
		screen.setLayoutAlgorithm(LAYOUT_ALGORITHM);

		footer = new CContainer(screen, true);
		footer.setPadding(0);
		footer.setExpandable(true);

		buttons = new CContainer(footer, true);
		buttons.setId("buttons");
		buttons.setPadding(0);
		button1 = new CButton(buttons, "button1", null);
		button2 = new CButton(buttons, "button2", null);

        screen.layout();
    }

    @BeforeEach
    void init() {
    }

    @Test
    void buttonOrder() {
        assertTrue(button2.getX() > button1.getX());
    }

    @Test
    void containerSize() {
        assertEquals(SCREEN_WIDTH, footer.getWidth());
        assertTrue(buttons.getHeight() <= footer.getHeight());
        assertTrue(buttons.getWidth() <= footer.getWidth());
        assertTrue(buttons.getWidth() >= button1.getWidth() + button2.getWidth());
        assertTrue(buttons.getHeight() >= button1.getHeight());
        assertTrue(buttons.getHeight() >= button2.getHeight());
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }}
