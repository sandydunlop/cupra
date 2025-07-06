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

class MixedVerticalTests {
    private static final int LAYOUT_ALGORITHM = 2;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static CContainer screen;
	private static CContainer header;
	private static CContainer body;
	private static CContainer footer;

    @BeforeAll
    static void initAll() {
        screen = new CContainer(null);
        screen.setWidth(SCREEN_WIDTH);
        screen.setHeight(SCREEN_HEIGHT);
        screen.setPadding(0);

		header = new CContainer(screen, true);
        header.setPadding(0);
		header.setExpandable(false);
        header.setHeight(40);
			
		body = new CContainer(screen, true);
		body.setPadding(0);
		body.setExpandable(true);

		footer = new CContainer(screen, true);
		footer.setPadding(0);
		footer.setExpandable(false);
        footer.setHeight(40);

        screen.layout();
    }

    @BeforeEach
    void init() {
    }

    @Test
    void simpleOrder() {
        assertTrue(body.getY() > header.getY());
        assertTrue(footer.getY() > body.getY());
    }

    @Test
    void simpleWidth() {
        assertEquals(screen.getWidth(), header.getWidth());
        assertEquals(screen.getWidth(), body.getWidth());
        assertEquals(screen.getWidth(), footer.getWidth());
    }

    @Test
    void simpleSpread() {
        assertEquals(screen.getHeight(), 
                header.getHeight() +
                body.getHeight() +
                footer.getHeight());
    }

    @Test
    void simplePositionBottom() {
        assertEquals(screen.getHeight(), footer.getY() + footer.getHeight());
    }

    @Test
    void simpleSizeA() {
        assertEquals(40, header.getHeight());
    }

    @Test
    void simpleSizeB() {
        assertEquals(520, body.getHeight());
    }

    @Test
    void simpleSizeC() {
        assertEquals(40, footer.getHeight());
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }
}
