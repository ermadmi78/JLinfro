package com.github.linfro.core.value;

import com.github.linfro.core.Flow;
import com.github.linfro.core.IHybridFlow;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-04
 * @since 1.0.0
 */
public interface HasValue<T> extends GetValue<T>, HasValueDSL<T> {
    @Override
    public T getValue();

    public void setValue(T value);

    @Override
    public void addChangeListener(ValueChangeListener<? super T> listener);

    @Override
    public void removeChangeListener(ValueChangeListener<? super T> listener);

    @Override
    public default HasValue<T> getMainValue() {
        return this;
    }

    @Override
    public default IHybridFlow<T> flow() {
        return Flow.from(this);
    }
}
