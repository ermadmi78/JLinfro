package com.github.linfro.core.value;

import java.util.function.Predicate;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-06
 * @since 1.0.0
 */
public class GetFilteredValue<T> extends AbstractGetWrapperValue<T, T> {
    protected final Predicate<? super T> predicate;

    public GetFilteredValue(GetValue<T> from, Predicate<? super T> predicate) {
        super(from);
        this.predicate = notNull(predicate);
    }

    @Override
    public T getValue() throws FilterException {
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        T result = from.getValue();
        if (!predicate.test(result)) {
            throw new FilterException();
        }

        return result;
    }
}
