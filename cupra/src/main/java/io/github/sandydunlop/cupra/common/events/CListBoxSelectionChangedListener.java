package io.github.sandydunlop.cupra.common.events;

import java.util.EventListener;


public interface CListBoxSelectionChangedListener extends EventListener {
    void selectionChanged(CListBoxSelectionChangedEvent event);
}