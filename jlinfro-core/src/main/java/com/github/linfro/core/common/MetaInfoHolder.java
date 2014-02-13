package com.github.linfro.core.common;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-13
 * @since 1.0.0
 */
public interface MetaInfoHolder {
    public static final String META_NAME = "JLinfro.meta.name";

    public Object getMetaInfo(String key);
}
