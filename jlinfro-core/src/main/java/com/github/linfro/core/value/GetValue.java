package com.github.linfro.core.value;

import com.github.linfro.core.Flow;
import com.github.linfro.core.IOutFlow;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-04
 * @since 1.0.0
 */
public interface GetValue<T> extends Getter<T>, GetValueDSL<T> {
    @Override
    public T getValue();

    public void addChangeListener(ValueChangeListener<? super T> listener);

    public void removeChangeListener(ValueChangeListener<? super T> listener);

    @Override
    public default GetValue<T> getMainValue() {
        return this;
    }

    public default IOutFlow<T> flow() {
        return Flow.from(this);
    }
}
