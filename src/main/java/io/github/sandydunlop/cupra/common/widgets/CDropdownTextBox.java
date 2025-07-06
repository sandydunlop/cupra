package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.platform.PlatformServices;


/**
 * CDropdownTextBox is a composite widget that combines a text box with a dropdown list.
 * It allows users to either type text directly or select from a list of predefined entries.
 * 
 * <p>
 * Features:
 * <ul>
 *   <li>Editable text box for user input.</li>
 *   <li>Dropdown button to display a list of selectable entries.</li>
 *   <li>Callback interfaces for selection changes and enter key presses.</li>
 *   <li>Customizable width and automatic layout of child components.</li>
 * </ul>
 *
 * @author Sandy Dunlop
 */
public class CDropdownTextBox extends CDropdownListBox {
    private EnterPressedAction onEnterPressed = null;


    /**
     * CDropdownTextBox is a custom widget that combines a text box with a dropdown list.
     * <p>
     * This component allows users to either enter text directly or select an option from a dropdown list.
     * It consists of a text box, a button to toggle the dropdown, and a list box for selection.
     * </p>
     *
     * <ul>
     *   <li>When the user presses Enter in the text box, an optional {@code onEnterPressed} callback is triggered.</li>
     *   <li>Clicking the dropdown button toggles the visibility of the list box.</li>
     *   <li>Selecting an item from the list box updates the text box and triggers an optional {@code onSelectionChanged} callback.</li>
     *   <li>The dropdown list is overlaid using {@code PlatformServices} when visible.</li>
     * </ul>
     *
     * @param parent The parent container to which this widget will be added.
     */
    public CDropdownTextBox(CContainer parent) {
        super(parent);
        textbox.setEditable(true);
        textbox.onEnterPressed(action -> {
            if (this.onEnterPressed != null) {
                this.onEnterPressed.onEnterPressed(textbox);
            }
        });
        textbox.onMouseClicked(action -> {
            listbox.setVisible(false);
            PlatformServices.getInstance().setOverlaid(null, null);
        });
    }


    /**
     * Sets the text of the textbox to the specified value.
     *
     * @param text the text to set in the textbox
     */
    public void setText(String text) {
        textbox.setText(text);
    }


    /**
     * Returns the current text contained in the textbox component.
     *
     * @return the text currently entered in the textbox
     */
    public String getText() {
        return textbox.getText();
    }


    //
    // === Mouse & Keyboard ===
    //


    public interface EnterPressedAction {
        void onEnterPressed(CTextBox enter);
    }


    public void onEnterPressed(EnterPressedAction action) {
        this.onEnterPressed = action;
    }
}
