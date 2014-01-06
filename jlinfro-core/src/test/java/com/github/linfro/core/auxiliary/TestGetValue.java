package com.github.linfro.core.auxiliary;

import com.github.linfro.core.value.AbstractGetValue;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-06
 * @since 1.0.0
 */
public class TestGetValue<T> extends AbstractGetValue<T> {
    public static <A> TestGetValue<A> newGetValue() {
        return new TestGetValue<>();
    }

    public static <A> TestGetValue<A> newGetValue(A value) {
        return new TestGetValue<>(value);
    }

    private T value;

    public TestGetValue() {
    }

    public TestGetValue(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    public void update(T value) {
        this.value = value;
        fireValueChanged();
    }
}
