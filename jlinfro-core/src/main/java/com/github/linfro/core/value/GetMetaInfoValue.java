package com.github.linfro.core.value;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.dsl.GetValueHolder;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-13
 * @since 1.0.0
 */
public class GetMetaInfoValue<T> extends AbstractWrapperValue<T, T, GetValue<T>> implements GetValue<T> {
    protected final String metaInfoKey;
    protected final Object metaInfoValue;

    public GetMetaInfoValue(GetValueHolder<T> from, String metaInfoKey, Object metaInfoValue) {
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
    public Object getMetaInfo(String key) {
        return metaInfoKey.equals(key) ? metaInfoValue : super.getMetaInfo(key);
    }
}
