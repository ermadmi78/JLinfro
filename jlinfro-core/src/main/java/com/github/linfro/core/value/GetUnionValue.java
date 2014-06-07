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
 * @version 2014-02-16
 * @since 1.0.0
 */
public class GetUnionValue<T> extends AbstractGetValue<List<T>> {
    protected final List<GetValue<? extends T>> args;
    protected final ValueChangeListener<T> argListener = new ValueChangeListener<T>() {
        @Override
        public void valueChanged(Getter<? extends T> getter) {
            lastGetter = getter;
            fireValueChanged();
        }
    };

    protected Getter<? extends T> lastGetter;
    private List<T> result;

    @SafeVarargs
    public GetUnionValue(GetValueHolder<? extends T> firstArg, GetValueHolder<? extends T>... otherArgs) {
        GetValue<? extends T> firstValue = notNull(notNull(firstArg).getContentValue());

        this.args = new ArrayList<>(otherArgs == null ? 1 : otherArgs.length + 1);
        this.args.add(firstValue);
        firstValue.addChangeListener(argListener);
        this.lastGetter = firstValue;

        if (otherArgs != null) {
            for (GetValueHolder<? extends T> arg : otherArgs) {
                GetValue<? extends T> nextValue = notNull(notNull(arg).getContentValue());
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

        for (GetValue<? extends T> arg : args) {
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

    @Override
    public List<T> getValue() {
        if (args.isEmpty()) {
            throw new IllegalStateException("Value is disposed");
        }

        if (result == null) {
            List<T> res = new ArrayList<>(args.size());
            for (GetValue<? extends T> arg : args) {
                res.add(arg.getValue());
            }

            result = res;
        }

        return result;
    }

    @Override
    public void fireValueChanged() {
        result = null;
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

        for (GetValue<? extends T> arg : args) {
            arg.removeChangeListener(argListener);
            if (arg.canDispose()) {
                arg.dispose();
            }
        }
        args.clear();
        lastGetter = null;
        result = null;
    }
}
