package com.github.linfro.core.value;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.Getter;
import com.github.linfro.core.ValueChangeListener;

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

    protected boolean autoDispose = true;
    protected boolean disposed = false;

    private List<T> result;
    private Getter<? extends T> lastGetter;

    @SafeVarargs
    public GetUnionValue(GetValue<? extends T> firstArg, GetValue<? extends T>... otherArgs) {
        notNull(firstArg);

        this.args = new ArrayList<>(otherArgs == null ? 1 : otherArgs.length + 1);
        this.args.add(firstArg);
        firstArg.addChangeListener(argListener);
        this.lastGetter = firstArg;

        if (otherArgs != null) {
            for (GetValue<? extends T> arg : otherArgs) {
                notNull(arg);
                this.args.add(arg);
                arg.addChangeListener(argListener);
            }
        }
    }

    @Override
    public List<T> getValue() {
        if (disposed) {
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
    public boolean isValueValid() {
        if (disposed) {
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
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        return lastGetter == null ? null : lastGetter.getMetaInfo(key);
    }

    @Override
    public void fireValueChanged() {
        result = null;
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
        for (GetValue<? extends T> arg : args) {
            arg.removeChangeListener(argListener);
            if (arg.isAutoDispose()) {
                arg.dispose();
            }
        }
        args.clear();
        result = null;
        lastGetter = null;
    }
}
