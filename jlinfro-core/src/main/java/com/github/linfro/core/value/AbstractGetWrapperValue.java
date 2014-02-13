package com.github.linfro.core.value;

import com.github.linfro.core.common.AutoDisposable;
import com.github.linfro.core.common.MetaInfoHolder;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-13
 * @since 1.0.0
 */
public abstract class AbstractGetWrapperValue<F, T> extends AbstractGetValue<T>
        implements GetDisposableValue<T>, AutoDisposable, MetaInfoHolder {
    protected final GetValue<F> from;
    protected final ValueChangeListener<F> fromListener = new ValueChangeListener<F>() {
        @Override
        public void valueChanged(Getter<? extends F> getter) {
            fireValueChanged();
        }
    };

    protected boolean autoDispose = true;
    protected boolean disposed = false;

    protected AbstractGetWrapperValue(GetValue<F> from) {
        this.from = notNull(from);
        this.from.addChangeListener(fromListener);
    }

    @Override
    public Object getMetaInfo(String key) {
        return from.findMetaInfo(key);
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
