package com.github.linfro.core.value;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.dsl.GetValueHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-16
 * @since 1.0.0
 */
public class GetUnionValue<T> extends AbstractMultiWrapperValue<T, List<T>> {
    private List<T> result;

    @SafeVarargs
    public GetUnionValue(GetValueHolder<? extends T> firstArg, GetValueHolder<? extends T>... otherArgs) {
        super(firstArg, otherArgs);
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
        super.fireValueChanged();
    }

    @Override
    public void dispose() {
        super.dispose();
        result = null;
    }
}
