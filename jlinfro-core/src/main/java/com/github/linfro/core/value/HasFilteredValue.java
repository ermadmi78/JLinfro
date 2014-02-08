package com.github.linfro.core.value;

import com.github.linfro.core.common.AutoDisposable;

import java.util.function.Predicate;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-08
 * @since 1.0.0
 */
public class HasFilteredValue<T> extends AbstractHasValue<T> implements HasDisposableValue<T>, AutoDisposable {
    protected final HasValue<T> from;
    protected final Predicate<? super T> outPredicate;
    protected final Predicate<? super T> inPredicate;
    protected final ValueChangeListener<T> fromListener = new ValueChangeListener<T>() {
        @Override
        public void valueChanged(Getter<? extends T> getter) {
            fireValueChanged();
        }
    };

    protected boolean autoDispose = true;
    protected boolean disposed = false;

    public HasFilteredValue(HasValue<T> from, Predicate<? super T> outPredicate, Predicate<? super T> inPredicate) {
        this.from = notNull(from);
        this.outPredicate = notNull(outPredicate);
        this.inPredicate = notNull(inPredicate);

        this.from.addChangeListener(fromListener);
        fireValueChanged();
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
        if (inPredicate.test(value)) {
            from.setValue(value);
        }
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
