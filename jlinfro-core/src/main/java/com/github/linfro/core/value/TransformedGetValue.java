package com.github.linfro.core.value;

import com.github.linfro.core.common.Function;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public class TransformedGetValue<F, T> extends AbstractGetValue<T> implements GetDisposableValue<T> {
    protected final GetValue<F> from;
    protected final Function<F, T> function;
    protected final ValueChangeListener<F> fromListener = new ValueChangeListener<F>() {
        @Override
        public void valueChanged(Getter<? extends F> getter) {
            fireValueChanged();
        }
    };

    protected boolean disposed = false;

    private T result;
    private boolean calculated = false;

    public TransformedGetValue(GetValue<F> from, Function<F, T> function) {
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
    public void dispose() {
        if (disposed) {
            return;
        }

        disposed = true;
        result = null;
        calculated = false;
        from.removeChangeListener(fromListener);
    }
}
