package com.github.linfro.core.value;

import com.github.linfro.core.common.AutoDisposable;

import java.util.function.Function;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public class HasTransformedValue<F, T> extends AbstractHasWrapperValue<F, T> implements HasDisposableValue<T>, AutoDisposable {
    protected final Function<F, T> outFunc;
    protected final Function<T, F> inFunc;

    private T result;
    private boolean calculated = false;

    public HasTransformedValue(HasValue<F> from, Function<F, T> outFunc, Function<T, F> inFunc) {
        super(from);
        this.outFunc = notNull(outFunc);
        this.inFunc = notNull(inFunc);
    }

    @Override
    public T getValue() {
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        if (!calculated) {
            result = outFunc.apply(from.getValue());
            calculated = true;
        }

        return result;
    }

    @Override
    public void setValue(T value) {
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        from.setValue(inFunc.apply(value));
    }

    @Override
    public void fireValueChanged() {
        result = null;
        calculated = false;
        super.fireValueChanged();
    }

    @Override
    public void dispose() {
        super.dispose();
        result = null;
        calculated = false;
    }
}
