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

import io.github.sandydunlop.cupra.common.util.Align;
import io.github.sandydunlop.cupra.common.widgets.CContainer;
import io.github.sandydunlop.cupra.common.widgets.CLabel;

class AlignHorizontalTests {
    private static final int LAYOUT_ALGORITHM = 2;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static CContainer screen;
	private static CContainer footer;
	private static CContainer footerLeft;
	private static CContainer footerRight;

    @BeforeAll
    static void initAll() {
    }

    @BeforeEach
    void init() {
        screen = new CContainer(null);
        screen.setWidth(SCREEN_WIDTH);
        screen.setHeight(SCREEN_HEIGHT);
        screen.setPadding(0);

		footer = new CContainer(screen, true);
		footer.setPadding(0);
		footer.setExpandable(true);

		footerLeft = new CContainer(footer, false);
		footerLeft.setPadding(0);
		footerLeft.setExpandable(false);
        new CLabel(footerLeft, "label");

		footerRight = new CContainer(footer, false);
		footerRight.setPadding(0);
		footerRight.setExpandable(false);
        new CLabel(footerRight, "label");
    }

    @Test
    void alignDefault() {
        screen.layout();
        assertTrue(footerRight.getX() > footerLeft.getX());
        assertEquals(0, footerLeft.getX());
        assertEquals(footerLeft.getWidth(), footerRight.getX());
    }

    @Test
    void alignSpread() {
        footer.setAlignHorizontal(Align.Horizontal.SPREAD);
        screen.layout();
        assertTrue(footerRight.getX() > footerLeft.getX());
        assertTrue(footerLeft.getX() > 0);
        assertTrue(footerRight.getX() + footerRight.getWidth() < SCREEN_WIDTH);
        assertTrue(footerLeft.getX() + footerLeft.getWidth() < footerRight.getX());
    }

    @Test
    void alignMiddle() {
        footer.setAlignHorizontal(Align.Horizontal.MIDDLE);
        screen.layout();
        assertTrue(footerRight.getX() > footerLeft.getX());
        assertTrue(footerLeft.getX() > 0);
        assertTrue(footerRight.getX() + footerRight.getWidth() < SCREEN_WIDTH);
        assertTrue(footerLeft.getX() + footerLeft.getWidth() <= footerRight.getX());
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }
}
