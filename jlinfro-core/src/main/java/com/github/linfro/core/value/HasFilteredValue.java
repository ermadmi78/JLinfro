package com.github.linfro.core.value;

import java.util.function.Predicate;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-08
 * @since 1.0.0
 */
public class HasFilteredValue<T> extends AbstractHasWrapperValue<T, T> {
    protected final Predicate<? super T> outPredicate;
    protected final Predicate<? super T> inPredicate;

    public HasFilteredValue(HasValue<T> from, Predicate<? super T> outPredicate, Predicate<? super T> inPredicate) {
        super(from);
        this.outPredicate = notNull(outPredicate);
        this.inPredicate = notNull(inPredicate);
    }

    @Override
    public T getValue() throws FilterException {
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        T result = from.getValue();
        if (!outPredicate.test(result)) {
            throw new FilterException();
        }

        return result;
    }

    @Override
    public void setValue(T value) {
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        if (inPredicate.test(value)) {
            from.setValue(value);
        }
    }
}
