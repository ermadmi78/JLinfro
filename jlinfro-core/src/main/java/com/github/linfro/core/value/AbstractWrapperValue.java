package com.github.linfro.core.value;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.Getter;
import com.github.linfro.core.ValueChangeListener;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-03-02
 * @since 1.0.0
 */
public abstract class AbstractWrapperValue<F, T, VAL extends GetValue<F>> extends AbstractGetValue<T> {
    protected VAL from;
    protected final ValueChangeListener<F> fromListener = new ValueChangeListener<F>() {
        @Override
        public void valueChanged(Getter<? extends F> getter) {
            fireValueChanged();
        }
    };

    protected AbstractWrapperValue(VAL from) {
        this.from = notNull(from);
        this.from.addChangeListener(fromListener);
    }

    @Override
    public boolean isValueValid() {
        if (from == null) {
            throw new IllegalStateException("Value is disposed");
        }

        return from.isValueValid();
    }

    @Override
    public Object getMetaInfo(String key) {
        if (from == null) {
            throw new IllegalStateException("Value is disposed");
        }

        return from.getMetaInfo(key);
    }

    @Override
    public void fireValueChanged() {
        if (from != null) {
            super.fireValueChanged();
        }
    }

    @Override
    public boolean canDispose() {
        return listeners.isEmpty();
    }

    @Override
    public void dispose() {
        listeners.clear();

        if (from == null) {
            return;
        }

        from.removeChangeListener(fromListener);
        if (from.canDispose()) {
            from.dispose();
        }
        from = null;
    }
}
