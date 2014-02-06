package com.github.linfro.core.value;

import com.github.linfro.core.common.AutoDisposable;

import java.util.function.Function;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public class HasTransformedValue<F, T> extends AbstractHasValue<T> implements HasDisposableValue<T>, AutoDisposable {
    protected final HasValue<F> from;
    protected final Function<F, T> outFunc;
    protected final Function<T, F> inFunc;
    protected final ValueChangeListener<F> fromListener = (getter) -> fireValueChanged();

    protected boolean autoDispose = true;
    protected boolean disposed = false;

    private T result;
    private boolean calculated = false;

    public HasTransformedValue(HasValue<F> from, Function<F, T> outFunc, Function<T, F> inFunc) {
        this.from = notNull(from);
        this.outFunc = notNull(outFunc);
        this.inFunc = notNull(inFunc);

        this.from.addChangeListener(fromListener);
    }

    @Override
    public T getValue() {
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        if (!calculated) {
            calculated = true;
            result = outFunc.apply(from.getValue());
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
        if (disposed) {
            return;
        }

        result = null;
        calculated = false;
        super.fireValueChanged();
    }

    @Override
    public boolean isAutoDispose() {
        return autoDispose;
    }

    public void setAutoDispose(boolean autoDispose) {
        this.autoDispose = autoDispose;
    }

    @Override
    public void dispose() {
        if (disposed) {
            return;
        }

        disposed = true;
        result = null;
        calculated = false;
        from.removeChangeListener(fromListener);
        from.autoDispose();
    }
}
