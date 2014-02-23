package com.github.linfro.core;

import com.github.linfro.core.common.Aggregator;
import com.github.linfro.core.value.DefaultGetAggregateValue;
import com.github.linfro.core.value.DefaultHasValue;
import com.github.linfro.core.value.ValueUtil;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-23
 * @since 1.0.0
 */
public final class Values {
    private Values() {
    }

    public static <A> DefaultHasValue<A> newHasValue() {
        return new DefaultHasValue<>();
    }

    public static <A> DefaultHasValue<A> newHasValue(A value) {
        return new DefaultHasValue<>(value);
    }

    public static <A> DefaultGetAggregateValue<A> newAggregateValue(Aggregator<A> aggregator) {
        notNull(aggregator);
        return new DefaultGetAggregateValue<>(aggregator);
    }

    public static DefaultGetAggregateValue<Boolean> andValue() {
        return newAggregateValue(ValueUtil.AGGREGATOR_AND);
    }

    public static DefaultGetAggregateValue<Boolean> orValue() {
        return newAggregateValue(ValueUtil.AGGREGATOR_OR);
    }
}
