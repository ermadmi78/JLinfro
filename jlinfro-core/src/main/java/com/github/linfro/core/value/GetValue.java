package com.github.linfro.core.value;

import com.github.linfro.core.Flow;
import com.github.linfro.core.IOutFlow;
import com.github.linfro.core.common.AutoDisposable;
import com.github.linfro.core.common.NullSafeFunction;
import com.github.linfro.core.common.NvlFunction;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-04
 * @since 1.0.0
 */
public interface GetValue<T> extends Getter<T>, AutoDisposable {
    @Override
    public T getValue();

    public void addChangeListener(ValueChangeListener<? super T> listener);

    public void removeChangeListener(ValueChangeListener<? super T> listener);

    @Override
    public default boolean isAutoDispose() {
        return false;
    }

    public default void dispose() {
        // Do nothing
    }

    public default IOutFlow<T> flow() {
        return Flow.from(this);
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
    public default GetValue<List<T>> union(GetValue... args) {
        return new GetUnionValue<>(this, args);
    }
}
