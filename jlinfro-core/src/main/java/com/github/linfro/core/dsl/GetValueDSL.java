package com.github.linfro.core.dsl;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.common.NullSafeFunction;
import com.github.linfro.core.common.NvlFunction;
import com.github.linfro.core.value.GetFilteredValue;
import com.github.linfro.core.value.GetMetaInfoValue;
import com.github.linfro.core.value.GetTransformedValue;
import com.github.linfro.core.value.GetUnionValue;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-22
 * @since 1.0.0
 */
public interface GetValueDSL<T> extends GetValueHolder<T>, GetterDSL, Disposable {
    public default GetValueFlow<T> flow() {
        return Flow.from(this);
    }

    public default <M> GetValue<M> map(Function<T, M> function) {
        return new GetTransformedValue<>(getContentValue(), function);
    }

    public default <M> GetValue<M> mapNotNull(Function<T, M> function) {
        return new GetTransformedValue<>(getContentValue(), new NullSafeFunction<>(function));
    }

    public default GetValue<T> nvl(T nullValue) {
        return new GetTransformedValue<>(getContentValue(), new NvlFunction<T>(nullValue));
    }

    public default GetValue<T> filter(Predicate<? super T> predicate) {
        return new GetFilteredValue<>(getContentValue(), predicate);
    }

    // Meta info support

    public default GetValue<T> putMetaInfo(String metaInfoKey, Object metaInfoValue) {
        return new GetMetaInfoValue<>(getContentValue(), metaInfoKey, metaInfoValue);
    }

    public default GetValue<T> named(String name) {
        return putMetaInfo(META_NAME, name);
    }

    @SuppressWarnings("unchecked")
    public default GetValue<List<T>> union(GetValue... args) {
        return new GetUnionValue<>(getContentValue(), args);
    }

    // Auto dispose

    public default boolean isAutoDispose() {
        return false;
    }

    @Override
    public default void dispose() {
        // Do nothing
    }
}
