package io.github.sandydunlop.cupra.common.widgets;

import io.github.sandydunlop.cupra.common.fonts.FontSpec;
import io.github.sandydunlop.cupra.common.util.Symbol;

public class CSymbolButton extends CButton {
    FontSpec symbolFont;

    public CSymbolButton(CContainer parent, Symbol symbol, CClickAction onClick) {
        super(parent, "", onClick);
        this.symbolFont = new FontSpec()
                .setName("modernpics")
                .setSize(20)
                .setColor(CWidget.getPalette().WIDGET_TEXT);
        this.setFont(symbolFont);
        this.setText(Symbol.asString(symbol));
    }
}
