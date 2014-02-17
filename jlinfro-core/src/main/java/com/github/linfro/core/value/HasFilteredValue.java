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

    private boolean result = false;
    private boolean calculated = false;

    public HasFilteredValue(HasValue<T> from, Predicate<? super T> outPredicate, Predicate<? super T> inPredicate) {
        super(from);
        this.outPredicate = notNull(outPredicate);
        this.inPredicate = notNull(inPredicate);
    }

    @Override
    public T getValue() {
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        return from.getValue();
    }

    @Override
    public boolean isValueValid() {
        if (!calculated) {
            result = false;
            if (super.isValueValid()) {
                result = outPredicate.test(getValue());
            }
            calculated = true;
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

    @Override
    public void fireValueChanged() {
        result = false;
        calculated = false;
        super.fireValueChanged();
    }

    @Override
    public void dispose() {
        super.dispose();
        result = false;
        calculated = false;
    }
}
