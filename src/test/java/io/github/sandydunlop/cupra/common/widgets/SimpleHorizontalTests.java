package io.github.sandydunlop.cupra.common.widgets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.plugins.util.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.sandydunlop.cupra.common.widgets.CContainer;
import io.github.sandydunlop.cupra.common.widgets.CLabel;

class SimpleHorizontalTests {
    private static final int LAYOUT_ALGORITHM = 2;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static CContainer screen;
	private static CContainer footer;
	private static CContainer footerLeft;
	private static CContainer footerRight;

    @BeforeAll
    static void initAll() {
        screen = new CContainer(null);
        screen.setWidth(SCREEN_WIDTH);
        screen.setHeight(SCREEN_HEIGHT);
        screen.setPadding(0);

		footer = new CContainer(screen, true);
		footer.setPadding(0);
		footer.setExpandable(true);

		footerLeft = new CContainer(footer, false);
		footerLeft.setPadding(0);
		footerLeft.setExpandable(true);

		footerRight = new CContainer(footer, false);
		footerRight.setPadding(0);
		footerRight.setExpandable(true);

        screen.layout();
    }

    @BeforeEach
    void init() {
    }

    @Test
    void simpleOrder() {
        assertTrue(footerRight.getX() > footerLeft.getX());
    }

    @Test
    void simpleHeight() {
        assertEquals(SCREEN_HEIGHT, footer.getHeight());
        assertEquals(SCREEN_HEIGHT, footerLeft.getHeight());
        assertEquals(SCREEN_HEIGHT, footerRight.getHeight());
    }

    @Test
    void simpleSpread() {
        assertEquals(screen.getWidth(), 
                footerLeft.getWidth() +
                footerRight.getWidth());
    }

    @Test
    void simplePositionRight() {
        assertEquals(screen.getWidth(), footerRight.getX() + footerRight.getWidth());
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }
}
