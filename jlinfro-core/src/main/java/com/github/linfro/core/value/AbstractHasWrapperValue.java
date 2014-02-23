package com.github.linfro.core.value;

import com.github.linfro.core.Getter;
import com.github.linfro.core.HasValue;
import com.github.linfro.core.ValueChangeListener;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-13
 * @since 1.0.0
 */
public abstract class AbstractHasWrapperValue<F, T> extends AbstractHasValue<T> {
    protected final HasValue<F> from;
    protected final ValueChangeListener<F> fromListener = new ValueChangeListener<F>() {
        @Override
        public void valueChanged(Getter<? extends F> getter) {
            fireValueChanged();
        }
    };

    protected boolean autoDispose = true;
    protected boolean disposed = false;

    protected AbstractHasWrapperValue(HasValue<F> from) {
        this.from = notNull(from);
        this.from.addChangeListener(fromListener);
    }

    @Override
    public boolean isValueValid() {
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        return from.isValueValid();
    }

    @Override
    public Object getMetaInfo(String key) {
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        return from.getMetaInfo(key);
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
        if (from.isAutoDispose()) {
            from.dispose();
        }
    }
}
