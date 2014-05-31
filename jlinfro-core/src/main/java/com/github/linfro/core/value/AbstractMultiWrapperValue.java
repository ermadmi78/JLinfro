package com.github.linfro.core.value;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.Getter;
import com.github.linfro.core.ValueChangeListener;
import com.github.linfro.core.dsl.GetValueHolder;

import java.util.ArrayList;
import java.util.List;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-05-31
 * @since 1.0.0
 */
public abstract class AbstractMultiWrapperValue<F, T> extends AbstractGetValue<T> {
    protected final List<GetValue<? extends F>> args;
    protected final ValueChangeListener<F> argListener = new ValueChangeListener<F>() {
        @Override
        public void valueChanged(Getter<? extends F> getter) {
            updateLastGetter(getter);
            fireValueChanged();
        }
    };

    protected Getter<? extends F> lastGetter;

    @SafeVarargs
    public AbstractMultiWrapperValue(GetValueHolder<? extends F> firstArg, GetValueHolder<? extends F>... otherArgs) {
        GetValue<? extends F> firstValue = notNull(notNull(firstArg).getContentValue());

        this.args = new ArrayList<>(otherArgs == null ? 1 : otherArgs.length + 1);
        this.args.add(firstValue);
        firstValue.addChangeListener(argListener);
        this.lastGetter = firstValue;

        if (otherArgs != null) {
            for (GetValueHolder<? extends F> arg : otherArgs) {
                GetValue<? extends F> nextValue = notNull(notNull(arg).getContentValue());
                this.args.add(nextValue);
                nextValue.addChangeListener(argListener);
            }
        }
    }

    @Override
    public boolean isValueValid() {
        if (args.isEmpty()) {
            throw new IllegalStateException("Value is disposed");
        }

        for (GetValue<? extends F> arg : args) {
            if (!arg.isValueValid()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Object getMetaInfo(String key) {
        if (args.isEmpty()) {
            throw new IllegalStateException("Value is disposed");
        }

        return lastGetter == null ? null : lastGetter.getMetaInfo(key);
    }

    protected void updateLastGetter(Getter<? extends F> getter) {
        lastGetter = getter;
    }

    @Override
    public void fireValueChanged() {
        if (!args.isEmpty()) {
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

        for (GetValue<? extends F> arg : args) {
            arg.removeChangeListener(argListener);
            if (arg.canDispose()) {
                arg.dispose();
            }
        }
        args.clear();
        lastGetter = null;
    }
}
