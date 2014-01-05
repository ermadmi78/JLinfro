package com.github.linfro.core.value;

/**
 * @author Dmitry Ermakov
 *         04.01.2014
 */
public interface HasValue<T> extends GetValue<T> {
    @Override
    public T getValue();

    public void setValue(T value);

    @Override
    public void addChangeListener(ValueChangeListener<? super T> listener);

    @Override
    public void removeChangeListener(ValueChangeListener<? super T> listener);
}
