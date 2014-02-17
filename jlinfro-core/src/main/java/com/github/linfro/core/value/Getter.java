package com.github.linfro.core.value;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-04
 * @since 1.0.0
 */
public interface Getter<T> {
    public static final String META_NAME = "JLinfro.meta.name";

    public T getValue();

    public default Object getMetaInfo(String key) {
        return null;
    }

    public default String getMetaName() {
        return (String) getMetaInfo(META_NAME);
    }
}
