package com.github.linfro.core.value;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.Getter;
import com.github.linfro.core.ValueChangeListener;
import com.github.linfro.core.dsl.GetValueHolder;

import java.util.function.BiFunction;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-06-12
 * @since 1.0.0
 */
public class GetJoin2Value<F1, F2, T> extends AbstractGetValue<T> {
    protected GetValue<? extends F1> arg0;
    protected GetValue<? extends F2> arg1;
    protected BiFunction<F1, F2, T> function;

    protected final ValueChangeListener<Object> argListener = new ValueChangeListener<Object>() {
        @Override
        public void valueChanged(Getter<?> getter) {
            lastGetter = getter;
            fireValueChanged();
        }
    };
    protected Getter<?> lastGetter;

    private T result;
    private boolean calculated = false;

    public GetJoin2Value(GetValueHolder<? extends F1> arg0,
                         GetValueHolder<? extends F2> arg1,
                         BiFunction<F1, F2, T> function) {
        this.arg0 = notNull(notNull(arg0).getContentValue());
        this.arg1 = notNull(notNull(arg1).getContentValue());
        this.function = notNull(function);

        this.lastGetter = this.arg0;
        this.arg0.addChangeListener(argListener);
        this.arg1.addChangeListener(argListener);
    }

    @Override
    public boolean isValueValid() {
        if (function == null) {
            throw new IllegalStateException("Value is disposed");
        }

        return arg0.isValueValid() && arg1.isValueValid();
    }

    @Override
    public Object getMetaInfo(String key) {
        if (function == null) {
            throw new IllegalStateException("Value is disposed");
        }

        return lastGetter == null ? null : lastGetter.getMetaInfo(key);
    }

    @Override
    public T getValue() {
        if (function == null) {
            throw new IllegalStateException("Value is disposed");
        }

        if (!calculated) {
            result = function.apply(arg0.getValue(), arg1.getValue());
            calculated = true;
        }

        return result;
    }

    @Override
    public void fireValueChanged() {
        result = null;
        calculated = false;
        if (function != null) {
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

        disposeArg(arg0);
        disposeArg(arg1);

        arg0 = null;
        arg1 = null;

        function = null;
        lastGetter = null;
        result = null;
        calculated = false;
    }

    private void disposeArg(GetValue<?> arg) {
        if (arg != null) {
            arg.removeChangeListener(argListener);
            if (arg.canDispose()) {
                arg.dispose();
            }
        }
    }
}
