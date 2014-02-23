package com.github.linfro.core.value;

import com.github.linfro.core.HasValue;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-13
 * @since 1.0.0
 */
public class HasMetaInfoValue<T> extends AbstractHasWrapperValue<T, T> {
    protected final String metaInfoKey;
    protected final Object metaInfoValue;

    public HasMetaInfoValue(HasValue<T> from, String metaInfoKey, Object metaInfoValue) {
        super(from);
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
