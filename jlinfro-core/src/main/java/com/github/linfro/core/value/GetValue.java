package com.github.linfro.core.value;

/**
 * @author Dmitry Ermakov
 *         04.01.2014
 */
public interface GetValue<T> extends Getter<T> {
    @Override
    public T getValue();

    public void addChangeListener(ValueChangeListener<? super T> listener);

    public void removeChangeListener(ValueChangeListener<? super T> listener);
}
