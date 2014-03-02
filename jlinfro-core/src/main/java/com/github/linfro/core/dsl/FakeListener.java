package com.github.linfro.core.dsl;

import com.github.linfro.core.Getter;
import com.github.linfro.core.ValueChangeListener;

/**
 * Empty listener.
 * Add this listener to some target value to prevent cascade dispose of target value.
 *
 * @author Dmitry Ermakov
 * @version 2014-03-02
 * @since 1.0.0
 */
public class FakeListener<T> implements ValueChangeListener<T> {
    @Override
    public void valueChanged(Getter<? extends T> getter) {
        // Do nothing
    }
}
