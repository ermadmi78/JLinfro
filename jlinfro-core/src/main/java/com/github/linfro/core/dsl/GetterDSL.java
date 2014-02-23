package com.github.linfro.core.dsl;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-22
 * @since 1.0.0
 */
public interface GetterDSL {
    public static final String META_NAME = "JLinfro.meta.name";

    public default boolean isValueValid() {
        return true;
    }

    public default Object getMetaInfo(String key) {
        return null;
    }

    public default String getMetaName() {
        return (String) getMetaInfo(META_NAME);
    }
}
