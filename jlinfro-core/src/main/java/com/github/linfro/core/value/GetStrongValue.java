package com.github.linfro.core.value;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.common.Equality;
import com.github.linfro.core.dsl.GetValueHolder;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-03-16
 * @since 1.0.0
 */
public class GetStrongValue<T> extends AbstractWrapperValue<T, T, GetValue<T>> implements GetValue<T> {
    protected Equality<? super T> equality;

    private boolean curValueValid = false;
    private T curValue;

    public GetStrongValue(GetValueHolder<T> from, Equality<? super T> equality) {
        super(notNull(from).getContentValue());
        this.equality = notNull(equality);

        this.curValueValid = this.from.isValueValid();
        this.curValue = this.curValueValid ? this.from.getValue() : null;
    }

    @Override
    public boolean isValueValid() {
        if (from == null) {
            throw new IllegalStateException("Value is disposed");
        }

        return curValueValid;
    }

    @Override
    public T getValue() {
        if (from == null) {
            throw new IllegalStateException("Value is disposed");
        }

        return curValue;
    }

    @Override
    public void fireValueChanged() {
        if ((from == null) || (equality == null)) {
            return;
        }

        boolean oldValueValid = curValueValid;
        T oldValue = curValue;

        curValueValid = from.isValueValid();
        curValue = curValueValid ? from.getValue() : null;

        if ((oldValueValid != curValueValid) || (curValueValid && !equality.areEquals(oldValue, curValue))) {
            super.fireValueChanged();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        equality = null;
        curValueValid = false;
        curValue = null;
    }
}
