package com.github.linfro.core.value;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public class DefaultHasValue<T> extends AbstractHasValue<T> {
    protected T value;

    public DefaultHasValue() {
    }

    public DefaultHasValue(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
        fireValueChanged();
    }
}
