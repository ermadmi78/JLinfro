package com.github.linfro.core.dsl;

import com.github.linfro.core.HasValue;
import com.github.linfro.core.common.Equality;
import com.github.linfro.core.common.NullSafeFunction;
import com.github.linfro.core.common.NvlFunction;
import com.github.linfro.core.common.ObjectUtil;
import com.github.linfro.core.value.HasFilteredValue;
import com.github.linfro.core.value.HasMetaInfoValue;
import com.github.linfro.core.value.HasStrongValue;
import com.github.linfro.core.value.HasTransformedValue;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-22
 * @since 1.0.0
 */
public interface HasValueDSL<T> extends HasValueHolder<T>, GetValueDSL<T> {
    @Override
    public default HasValueFlow<T> flow() {
        return Flow.from(this);
    }

    @Override
    public default HasValue<T> strong() {
        return strong(ObjectUtil.DEFAULT_EQUALITY);
    }

    @Override
    public default HasValue<T> strong(Equality<? super T> equality) {
        return new HasStrongValue<>(this, equality);
    }

    public default <M> HasValue<M> map(Function<T, M> outFunc, Function<M, T> inFunc) {
        return new HasTransformedValue<>(this, outFunc, inFunc);
    }

    public default <M> HasValue<M> mapNotNull(Function<T, M> outFunc, Function<M, T> inFunc) {
        return new HasTransformedValue<>(this, new NullSafeFunction<T, M>(outFunc), new NullSafeFunction<M, T>(inFunc));
    }

    public default HasValue<T> nvl(T outNullValue, T inNullValue) {
        return new HasTransformedValue<>(this, new NvlFunction<T>(outNullValue), new NvlFunction<T>(inNullValue));
    }

    public default HasValue<T> filter(Predicate<? super T> outPredicate, Predicate<? super T> inPredicate) {
        return new HasFilteredValue<>(this, outPredicate, inPredicate);
    }

    public default HasValue<T> biMap(Function<T, T> inOutFunction) {
        return new HasTransformedValue<>(this, inOutFunction, inOutFunction);
    }

    public default HasValue<T> biMapNotNull(Function<T, T> inOutFunction) {
        Function<T, T> nullSafeFunction = new NullSafeFunction<>(inOutFunction);
        return new HasTransformedValue<>(this, nullSafeFunction, nullSafeFunction);
    }

    public default HasValue<T> biNvl(T inOutNullValue) {
        return new HasTransformedValue<>(this, new NvlFunction<T>(inOutNullValue), new NvlFunction<T>(inOutNullValue));
    }

    public default HasValue<T> biFilter(Predicate<? super T> predicate) {
        return new HasFilteredValue<>(this, predicate, predicate);
    }

    // Meta info support

    @Override
    public default HasValue<T> putMetaInfo(String metaInfoKey, Object metaInfoValue) {
        return new HasMetaInfoValue<>(this, metaInfoKey, metaInfoValue);
    }

    @Override
    public default HasValue<T> named(String name) {
        return putMetaInfo(META_NAME, name);
    }
}
