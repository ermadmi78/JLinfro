package com.github.linfro.value;

/**
 * @author Dmitry Ermakov
 *         04.01.2014
 */
public interface ValueChangeListener<T> {
    public void valueChanged(Getter<? extends T> getter);
}
