package com.github.linfro.core.value;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.dsl.GetValueHolder;

import java.util.function.Predicate;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-06
 * @since 1.0.0
 */
public class GetFilteredValue<T> extends AbstractWrapperValue<T, T, GetValue<T>> implements GetValue<T> {
    protected Predicate<? super T> predicate;

    private boolean result = false;
    private boolean calculated = false;

    public GetFilteredValue(GetValueHolder<T> from, Predicate<? super T> predicate) {
        super(notNull(from).getContentValue());
        this.predicate = notNull(predicate);
    }

    @Override
    public T getValue() {
        if (from == null) {
            throw new IllegalStateException("Value is disposed");
        }

        return from.getValue();
    }

    @Override
    public boolean isValueValid() {
        if (from == null) {
            throw new IllegalStateException("Value is disposed");
        }

        if (!calculated) {
            result = false;
            if (super.isValueValid()) {
                result = predicate.test(getValue());
            }
            calculated = true;
        }

        return result;
    }

    @Override
    public void fireValueChanged() {
        result = false;
        calculated = false;
        super.fireValueChanged();
    }

    @Override
    public void dispose() {
        super.dispose();
        result = false;
        calculated = false;
        predicate = null;
    }
}
