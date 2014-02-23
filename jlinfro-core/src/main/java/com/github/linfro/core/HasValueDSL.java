package com.github.linfro.core;

import com.github.linfro.core.common.NullSafeFunction;
import com.github.linfro.core.common.NvlFunction;
import com.github.linfro.core.value.HasFilteredValue;
import com.github.linfro.core.value.HasMetaInfoValue;
import com.github.linfro.core.value.HasTransformedValue;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-22
 * @since 1.0.0
 */
public interface HasValueDSL<T> extends GetValueDSL<T> {
    @Override
    public HasValue<T> getContentValue();

    public default <M> HasValue<M> map(Function<T, M> outFunc, Function<M, T> inFunc) {
        return new HasTransformedValue<>(getContentValue(), outFunc, inFunc);
    }

    public default <M> HasValue<M> mapNotNull(Function<T, M> outFunc, Function<M, T> inFunc) {
        return new HasTransformedValue<>(getContentValue(), new NullSafeFunction<T, M>(outFunc), new NullSafeFunction<M, T>(inFunc));
    }

    public default HasValue<T> nvl(T outNullValue, T inNullValue) {
        return new HasTransformedValue<>(getContentValue(), new NvlFunction<T>(outNullValue), new NvlFunction<T>(inNullValue));
    }

    public default HasValue<T> filter(Predicate<? super T> outPredicate, Predicate<? super T> inPredicate) {
        return new HasFilteredValue<>(getContentValue(), outPredicate, inPredicate);
    }

    public default HasValue<T> biMap(Function<T, T> inOutFunction) {
        return new HasTransformedValue<>(getContentValue(), inOutFunction, inOutFunction);
    }

    public default HasValue<T> biMapNotNull(Function<T, T> inOutFunction) {
        Function<T, T> nullSafeFunction = new NullSafeFunction<>(inOutFunction);
        return new HasTransformedValue<>(getContentValue(), nullSafeFunction, nullSafeFunction);
    }

    public default HasValue<T> biNvl(T inOutNullValue) {
        return new HasTransformedValue<>(getContentValue(), new NvlFunction<T>(inOutNullValue), new NvlFunction<T>(inOutNullValue));
    }

    public default HasValue<T> biFilter(Predicate<? super T> predicate) {
        return new HasFilteredValue<>(getContentValue(), predicate, predicate);
    }

    // Meta info support

    @Override
    public default HasValue<T> putMetaInfo(String metaInfoKey, Object metaInfoValue) {
        return new HasMetaInfoValue<>(getContentValue(), metaInfoKey, metaInfoValue);
    }

    @Override
    public default HasValue<T> named(String name) {
        return putMetaInfo(META_NAME, name);
    }
}
