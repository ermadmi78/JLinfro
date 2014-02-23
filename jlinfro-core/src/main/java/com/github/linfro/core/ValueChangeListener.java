package com.github.linfro.core;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-04
 * @since 1.0.0
 */
public interface ValueChangeListener<T> {
    public void valueChanged(Getter<? extends T> getter);
}
