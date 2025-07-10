package io.github.sandydunlop.cupra.common.widgets;

public class CListBoxColumn {
    private String name = "";
    private int width = 100;

    public CListBoxColumn() {
        // Nothing to see here
    }


    public CListBoxColumn(String name, int width) {
        this.name = name;
        this.width = width;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setWidth(int width) {
        this.width = width;
    }


    public int getWidth() {
        return width;
    }
}
