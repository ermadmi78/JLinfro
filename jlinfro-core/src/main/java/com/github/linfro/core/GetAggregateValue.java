package com.github.linfro.core;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public interface GetAggregateValue<T> extends GetValue<T>, Iterable<T> {
    public HasValue<T> newArgument();
}
