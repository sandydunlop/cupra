package io.github.sandydunlop.cupra.common.palette;

/**
 * The {@code Avocado} class defines a color palette inspired by avocado tones.
 * <p>
 * This palette provides specific ARGB color values for various UI elements such as backgrounds,
 * text, borders, and widgets. It extends the {@link ColorPalette} base class and sets the following colors:
 * <ul>
 *   <li>{@code REGULAR_BACKGROUND}: Default background color</li>
 *   <li>{@code REGULAR_TEXT}: Default text color</li>
 *   <li>{@code HOVERED_BACKGROUND}: Background color when hovered</li>
 *   <li>{@code HOVERED_BORDER}: Border color when hovered</li>
 *   <li>{@code SELECTED_BACKGROUND}: Background color when selected</li>
 *   <li>{@code SELECTED_TEXT}: Text color when selected</li>
 *   <li>{@code INPUT_BACKGROUND}: Background color for input fields</li>
 *   <li>{@code WIDGET_TEXT}: Text color for widgets</li>
 * </ul>
 * <p>
 * All color values are specified in ARGB hexadecimal format.
 */
public class Avocado extends ColorPalette{
	public Avocado() {
		REGULAR_BACKGROUND = 0x88303030;
		REGULAR_TEXT = 0xFF808080;
		HOVERED_BACKGROUND = 0xFF3B4017;
		HOVERED_BORDER = 0xFFB2C248;
		SELECTED_BACKGROUND = 0xFF626B27;
		SELECTED_TEXT = 0xFF000000;
	    INPUT_BACKGROUND = 0xFF000000;
        WIDGET_TEXT = 0xFF808080;
	}
}
