package com.github.linfro.core.value;

import com.github.linfro.core.common.AutoDisposable;

import java.util.ArrayList;
import java.util.List;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-16
 * @since 1.0.0
 */
public class GetUnionValue<T> extends AbstractGetValue<List<T>> implements GetDisposableValue<List<T>>, AutoDisposable {
    protected final List<GetValue<? extends T>> args;
    protected final ValueChangeListener<T> argListener = new ValueChangeListener<T>() {
        @Override
        public void valueChanged(Getter<? extends T> getter) {
            fireValueChanged();
        }
    };

    protected boolean autoDispose = true;
    protected boolean disposed = false;

    private List<T> result;

    @SuppressWarnings("unchecked")
    public GetUnionValue(GetValue<? extends T> firstArg, GetValue<? extends T>... otherArgs) {
        notNull(firstArg);
        if (otherArgs == null) {
            otherArgs = new GetValue[0];
        }

        this.args = new ArrayList<>(otherArgs.length + 1);
        this.args.add(firstArg);
        firstArg.addChangeListener(argListener);

        for (GetValue<? extends T> arg : otherArgs) {
            notNull(arg);
            this.args.add(arg);
            arg.addChangeListener(argListener);
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
            arg.autoDispose();
        }
        args.clear();
        result = null;
    }
}
