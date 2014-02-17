package com.github.linfro.core.value;

import java.util.function.Predicate;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-06
 * @since 1.0.0
 */
public class GetFilteredValue<T> extends AbstractGetWrapperValue<T, T> {
    protected final Predicate<? super T> predicate;

    private boolean result = false;
    private boolean calculated = false;

    public GetFilteredValue(GetValue<T> from, Predicate<? super T> predicate) {
        super(from);
        this.predicate = notNull(predicate);
    }

    @Override
    public T getValue() {
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        return from.getValue();
    }

    @Override
    public boolean isValueValid() {
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
    }
}
