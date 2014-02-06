package com.github.linfro.core.value;

import com.github.linfro.core.common.AutoDisposable;

import java.util.function.Function;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public class GetTransformedValue<F, T> extends AbstractGetValue<T> implements GetDisposableValue<T>, AutoDisposable {
    protected final GetValue<F> from;
    protected final Function<F, T> function;
    protected final ValueChangeListener<F> fromListener = (getter) -> fireValueChanged();

    protected boolean autoDispose = true;
    protected boolean disposed = false;

    private T result;
    private boolean calculated = false;

    public GetTransformedValue(GetValue<F> from, Function<F, T> function) {
        this.from = notNull(from);
        this.function = notNull(function);

        this.from.addChangeListener(fromListener);
    }

    @Override
    public T getValue() {
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        if (!calculated) {
            calculated = true;
            result = function.apply(from.getValue());
        }

        return result;
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
