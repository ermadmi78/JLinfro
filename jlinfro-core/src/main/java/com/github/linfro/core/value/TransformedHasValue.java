package com.github.linfro.core.value;

import com.github.linfro.core.common.RevertFunction;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public class TransformedHasValue<F, T> extends AbstractHasValue<T> implements HasDisposableValue<T> {
    protected final HasValue<F> from;
    protected final RevertFunction<F, T> function;
    protected final ValueChangeListener<F> fromListener = new ValueChangeListener<F>() {
        @Override
        public void valueChanged(Getter<? extends F> getter) {
            fireValueChanged();
        }
    };

    protected boolean disposed = false;

    private T result;
    private boolean calculated = false;

    public TransformedHasValue(HasValue<F> from, RevertFunction<F, T> function) {
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
    public void setValue(T value) {
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        from.setValue(function.revert(value));
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
