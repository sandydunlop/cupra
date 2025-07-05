package io.github.sandydunlop.cupra.common.util;

/**
 * Utility class for alignment options used in layout or positioning logic.
 * <p>
 * Provides enumerations for horizontal and vertical alignment:
 * <ul>
 *   <li>{@link Horizontal} - LEFT, MIDDLE, SPREAD, RIGHT</li>
 *   <li>{@link Vertical} - TOP, MIDDLE, SPREAD, BOTTOM</li>
 * </ul>
 * This class cannot be instantiated.
 */
public class Align {
    private Align() {
    }
    /**
     * Represents the possible horizontal alignment options.
     * <ul>
     *   <li>{@link #LEFT} - Align content to the left.</li>
     *   <li>{@link #MIDDLE} - Center content horizontally.</li>
     *   <li>{@link #SPREAD} - Distribute content evenly across the horizontal space.</li>
     *   <li>{@link #RIGHT} - Align content to the right.</li>
     * </ul>
     */
    public enum Horizontal {
        LEFT,
        MIDDLE,
        SPREAD,
        RIGHT,
    }

    /**
     * Specifies the vertical alignment options.
     * <ul>
     *   <li>{@link #TOP} - Aligns content to the top.</li>
     *   <li>{@link #MIDDLE} - Centers content vertically.</li>
     *   <li>{@link #SPREAD} - Evenly distributes content vertically.</li>
     *   <li>{@link #BOTTOM} - Aligns content to the bottom.</li>
     * </ul>
     */
    public enum Vertical {
        TOP,
        MIDDLE,
        SPREAD,
        BOTTOM,
    }
}
