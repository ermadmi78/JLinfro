package com.github.linfro.core.dsl;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.common.*;
import com.github.linfro.core.value.*;

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

    public default GetValue<T> strong() {
        return strong(ObjectUtil.DEFAULT_NEW_EQUALITY);
    }

    public default GetValue<T> strong(EqualityNew<? super T> equality) {
        return new GetStrongValue<>(this, equality);
    }

    public default <M> GetValue<M> map(Function<T, M> function) {
        return new GetTransformedValue<>(this, function);
    }

    public default <M> GetValue<M> mapNotNull(Function<T, M> function) {
        return new GetTransformedValue<>(this, new NullSafeFunction<>(function));
    }

    public default GetValue<T> nvl(T nullValue) {
        return new GetTransformedValue<>(this, new NvlFunction<T>(nullValue));
    }

    public default GetValue<T> filter(Predicate<? super T> predicate) {
        return new GetFilteredValue<>(this, predicate);
    }

    // Meta info support

    public default GetValue<T> putMetaInfo(String metaInfoKey, Object metaInfoValue) {
        return new GetMetaInfoValue<>(this, metaInfoKey, metaInfoValue);
    }

    public default GetValue<T> named(String name) {
        return putMetaInfo(META_NAME, name);
    }

    @SuppressWarnings("unchecked")
    public default GetValue<List<T>> union(GetValueHolder... args) {
        return new GetUnionValue<>(this, args);
    }

    // Dispose

    public default boolean canDispose() {
        return true;
    }

    @Override
    public default void dispose() {
        // Do nothing
    }
}
