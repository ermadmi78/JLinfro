package com.github.linfro.core.dsl;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.common.Equality;
import com.github.linfro.core.value.HasValue;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public interface ICoreDSL<NEXT, F> {
    public NEXT strong();

    public NEXT strong(Equality equality);

    public NEXT force();

    public Disposable to(HasValue<F> to);
}
