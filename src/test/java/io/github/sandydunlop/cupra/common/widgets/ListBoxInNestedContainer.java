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

class ListBoxInNestedContainer {
    private static final int LAYOUT_ALGORITHM = 2;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static CContainer screen;
	private static CContainer body;
	private static CContainer middle;
    private static CListBox listBox;
	private static CContainer middle2;
	private static CButton button1m;
	private static CButton button2m;
	private static CButton button1m2;
	private static CButton button2m2;
    
    @BeforeAll
    static void initAll() {
    }

    @BeforeEach
    void init() {
    }

    @Test
    @Disabled
    void nonExpandable() {
        screen = new CContainer(null);
        screen.setWidth(SCREEN_WIDTH);
        screen.setHeight(SCREEN_HEIGHT);
        screen.setPadding(0);

		body = new CContainer(screen, true);
		body.setPadding(0);
		body.setExpandable(true);

        middle = new CContainer(body, false);
		middle.setId("middle");
		middle.setPadding(0);
		middle.setExpandable(false);
		listBox = new CListBox(middle);
		listBox.setExpandable(false);

		middle2 = new CContainer(body, false);
		middle2.setId("middle2");
		middle2.setPadding(0);
		middle2.setExpandable(false);
		button1m2 = new CButton(middle2, "button1m2", null);
		button2m2 = new CButton(middle2, "button2m2", null);

        screen.layout();

        assertTrue(listBox.getWidth() > 0);
        assertTrue(listBox.getHeight() > 0);
        assertTrue(listBox.getCalculatedX() > 0);
        assertTrue(listBox.getCalculatedX() < SCREEN_WIDTH/2);
        assertTrue(listBox.getCalculatedX() + listBox.getWidth() <= button1m2.getCalculatedX());
    }

    //TODO: WHy is calculatedX zero here when it should be 253?
    @Test
    @Disabled
    void expandable() {
        screen = new CContainer(null);
        screen.setWidth(SCREEN_WIDTH);
        screen.setHeight(SCREEN_HEIGHT);
        screen.setPadding(0);

		body = new CContainer(screen, true);
		body.setPadding(0);
		body.setExpandable(true);

        middle = new CContainer(body, false);
		middle.setId("middle");
		middle.setPadding(0);
		middle.setExpandable(false);
		listBox = new CListBox(middle);
		listBox.setExpandable(true);

		middle2 = new CContainer(body, false);
		middle2.setId("middle2");
		middle2.setPadding(0);
		middle2.setExpandable(false);
		button1m2 = new CButton(middle2, "button1m2", null);
		button2m2 = new CButton(middle2, "button2m2", null);

        screen.layout();

        assertTrue(listBox.getWidth() > 0);
        assertEquals(middle.getHeight(), listBox.getHeight());
        assertEquals(253, listBox.getCalculatedX());
        // assertTrue(listBox.getCalculatedX() > 0);
        // assertTrue(listBox.getCalculatedX() < SCREEN_WIDTH/2);
        // assertTrue(listBox.getCalculatedX() + listBox.getWidth() <= button1m2.getCalculatedX());
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }}
