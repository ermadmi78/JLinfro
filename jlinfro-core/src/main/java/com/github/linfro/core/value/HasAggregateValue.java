package com.github.linfro.core.value;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public interface HasAggregateValue<T> extends GetValue<T>, Iterable<T> {
    public HasDisposableValue<T> newMemberValue();
}
