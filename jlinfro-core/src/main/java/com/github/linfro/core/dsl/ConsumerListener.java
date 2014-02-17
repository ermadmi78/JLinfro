package com.github.linfro.core.dsl;

import com.github.linfro.core.value.Getter;
import com.github.linfro.core.value.ValueChangeListener;

import java.util.function.Consumer;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-08
 * @since 1.0.0
 */
public class ConsumerListener<A> implements ValueChangeListener<A> {
    protected final Consumer<? super A> target;

    public ConsumerListener(Consumer<? super A> target) {
        this.target = notNull(target);
    }

    @Override
    public void valueChanged(Getter<? extends A> getter) {
        if (getter.isValueValid()) {
            target.accept(getter.getValue());
        }
    }
}
