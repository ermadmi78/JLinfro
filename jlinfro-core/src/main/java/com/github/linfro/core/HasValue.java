package com.github.linfro.core;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-04
 * @since 1.0.0
 */
public interface HasValue<T> extends GetValue<T>, HasValueHolder<T>, HasValueDSL<T> {
    @Override
    public T getValue();

    public void setValue(T value);

    @Override
    public void addChangeListener(ValueChangeListener<? super T> listener);

    @Override
    public void removeChangeListener(ValueChangeListener<? super T> listener);

    @Override
    public default HasValue<T> getContentValue() {
        return this;
    }

    @Override
    public default IHybridFlow<T> flow() {
        return Flow.from(this);
    }
}
