package com.github.linfro.core.value;

import com.github.linfro.core.common.AutoDisposable;

import java.util.function.Predicate;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-06
 * @since 1.0.0
 */
public class GetFilteredValue<T> extends AbstractGetValue<T> implements GetDisposableValue<T>, AutoDisposable {
    protected final GetValue<T> from;
    protected final Predicate<? super T> predicate;
    protected final ValueChangeListener<T> fromListener = new ValueChangeListener<T>() {
        @Override
        public void valueChanged(Getter<? extends T> getter) {
            fireValueChanged();
        }
    };

    protected boolean autoDispose = true;
    protected boolean disposed = false;

    public GetFilteredValue(GetValue<T> from, Predicate<? super T> predicate) {
        this.from = notNull(from);
        this.predicate = notNull(predicate);

        this.from.addChangeListener(fromListener);
        fireValueChanged();
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

    @Override
    public void fireValueChanged() {
        if (!disposed) {
            super.fireValueChanged();
        }
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
        from.removeChangeListener(fromListener);
        from.autoDispose();
    }
}
