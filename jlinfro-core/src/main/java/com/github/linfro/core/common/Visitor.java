package com.github.linfro.core.common;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public interface Visitor<T> {
    public void visit(T target) throws VisitorBreakException;
}
