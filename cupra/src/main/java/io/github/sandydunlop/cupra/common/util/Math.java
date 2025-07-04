package io.github.sandydunlop.cupra.common.util;

public class Math {
    public static int min(int a, int b) {
        return a<b ? a : b;
    }


    public static int max(int a, int b) {
        return a>b ? a : b;
    }


    public static double min(double a, double b) {
        return a<b ? a : b;
    }


    public static double max(double a, double b) {
        return a>b ? a : b;
    }


    public static int floor(double value) {
        int i = (int)value;
        return value < (double)i ? i - 1 : i;
    }


    public static int clamp(int value, int min, int max) {
        return value < min ? min : Math.min(value, max);
    }

    public static double clamp(double value, double min, double max) {
        return value < min ? min : Math.min(value, max);
    }
}
