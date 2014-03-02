package com.github.linfro.core.value;

import com.github.linfro.core.HasValue;
import com.github.linfro.core.dsl.HasValueHolder;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-13
 * @since 1.0.0
 */
public class HasMetaInfoValue<T> extends AbstractWrapperValue<T, T, HasValue<T>> implements HasValue<T> {
    protected final String metaInfoKey;
    protected final Object metaInfoValue;

    public HasMetaInfoValue(HasValueHolder<T> from, String metaInfoKey, Object metaInfoValue) {
        super(notNull(from).getContentValue());
        this.metaInfoKey = notNull(metaInfoKey);
        this.metaInfoValue = metaInfoValue;
    }

    @Override
    public T getValue() {
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        return from.getValue();
    }

    @Override
    public void setValue(T value) {
        if (disposed) {
            throw new IllegalStateException("Value is disposed");
        }

        from.setValue(value);
    }

    @Override
    public Object getMetaInfo(String key) {
        return metaInfoKey.equals(key) ? metaInfoValue : super.getMetaInfo(key);
    }
}
