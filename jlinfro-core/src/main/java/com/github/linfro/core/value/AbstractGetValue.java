package com.github.linfro.core.value;

import java.util.LinkedList;
import java.util.List;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public abstract class AbstractGetValue<T> implements GetValue<T> {
    protected final List<ValueChangeListener<? super T>> listeners = new LinkedList<>();
    private boolean fireEventInProgress = false;

    @Override
    public void addChangeListener(ValueChangeListener<? super T> listener) {
        listeners.add(notNull(listener));
    }

    @Override
    public void removeChangeListener(ValueChangeListener<? super T> listener) {
        listeners.remove(notNull(listener));
    }

    public void fireValueChanged() {
        if (fireEventInProgress) {
            throw new IllegalStateException("Call loop detected");
        }

        fireEventInProgress = true;
        try {
            for (ValueChangeListener<? super T> listener : listeners) {
                listener.valueChanged(this);
            }
        } finally {
            fireEventInProgress = false;
        }
    }
}
