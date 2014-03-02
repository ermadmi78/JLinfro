package com.github.linfro.core.value;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.dsl.GetValueHolder;

import java.util.function.Function;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public class GetTransformedValue<F, T> extends AbstractWrapperValue<F, T, GetValue<F>> implements GetValue<T> {
    protected Function<F, T> function;

    private T result;
    private boolean calculated = false;

    public GetTransformedValue(GetValueHolder<F> from, Function<F, T> function) {
        super(notNull(from).getContentValue());
        this.function = notNull(function);
    }

    @Override
    public T getValue() {
        if (from == null) {
            throw new IllegalStateException("Value is disposed");
        }

        if (!calculated) {
            result = function.apply(from.getValue());
            calculated = true;
        }

        return result;
    }

    @Override
    public void fireValueChanged() {
        result = null;
        calculated = false;
        super.fireValueChanged();
    }

    @Override
    public void dispose() {
        super.dispose();
        result = null;
        calculated = false;
        function = null;
    }
}
