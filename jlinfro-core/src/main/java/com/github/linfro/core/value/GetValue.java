package com.github.linfro.core.value;

import com.github.linfro.core.Flow;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-04
 * @since 1.0.0
 */
public interface GetValue<T> extends Getter<T> {
    @Override
    public T getValue();

    public void addChangeListener(ValueChangeListener<? super T> listener);

    public void removeChangeListener(ValueChangeListener<? super T> listener);

    public default Flow.OneWayFlow<T> directFlow() {
        return Flow.from(this);
    }
}
