package com.github.linfro.core.dsl;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.common.*;
import com.github.linfro.core.value.*;

import java.util.List;
import java.util.Map;
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
        return strong(ObjectUtil.DEFAULT_EQUALITY);
    }

    public default GetValue<T> strong(Equality<? super T> equality) {
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

    public default GetValue<List<T>> union(GetValueHolder<? extends T> arg1) {
        return new GetUnionValue<T>(this, arg1);
    }

    public default GetValue<List<T>> union(GetValueHolder<? extends T> arg1,
                                           GetValueHolder<? extends T> arg2) {
        return new GetUnionValue<T>(this, arg1, arg2);
    }

    public default GetValue<List<T>> union(GetValueHolder<? extends T> arg1,
                                           GetValueHolder<? extends T> arg2,
                                           GetValueHolder<? extends T> arg3) {
        return new GetUnionValue<T>(this, arg1, arg2, arg3);
    }

    public default GetValue<List<T>> union(GetValueHolder<? extends T> arg1,
                                           GetValueHolder<? extends T> arg2,
                                           GetValueHolder<? extends T> arg3,
                                           GetValueHolder<? extends T> arg4) {
        return new GetUnionValue<T>(this, arg1, arg2, arg3, arg4);
    }

    public default GetValue<List<T>> union(GetValueHolder<? extends T> arg1,
                                           GetValueHolder<? extends T> arg2,
                                           GetValueHolder<? extends T> arg3,
                                           GetValueHolder<? extends T> arg4,
                                           GetValueHolder<? extends T> arg5) {
        return new GetUnionValue<T>(this, arg1, arg2, arg3, arg4, arg5);
    }

    @SuppressWarnings("unchecked")
    public default GetValue<List<T>> union(GetValueHolder<? extends T> arg1,
                                           GetValueHolder<? extends T> arg2,
                                           GetValueHolder<? extends T> arg3,
                                           GetValueHolder<? extends T> arg4,
                                           GetValueHolder<? extends T> arg5,
                                           GetValueHolder... otherArgs) {
        GetValueHolder[] args = new GetValueHolder[5 + (otherArgs == null ? 0 : otherArgs.length)];
        args[0] = arg1;
        args[1] = arg2;
        args[2] = arg3;
        args[3] = arg4;
        args[4] = arg5;
        if ((otherArgs != null) && (otherArgs.length > 0)) {
            System.arraycopy(otherArgs, 0, args, 5, otherArgs.length);
        }

        return new GetUnionValue<T>(this, args);
    }

    public default GetValue<Map<String, Object>> merge(GetValueHolder arg1, GetValueHolder... args) {
        return new GetMergeValue(this, arg1, args);
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
