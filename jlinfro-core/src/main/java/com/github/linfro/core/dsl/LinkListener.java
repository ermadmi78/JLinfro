package com.github.linfro.core.dsl;

import com.github.linfro.core.Getter;
import com.github.linfro.core.HasValue;
import com.github.linfro.core.ValueChangeListener;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-08
 * @since 1.0.0
 */
public class LinkListener<A> implements ValueChangeListener<A> {
    protected final HasValue<A> targetValue;
    protected final Context context;
    protected final UnsafeLock lock;

    public LinkListener(HasValue<A> targetValue, Context context, UnsafeLock lock) {
        this.targetValue = notNull(targetValue);
        this.context = notNull(context);
        this.lock = notNull(lock);
    }

    @Override
    public void valueChanged(Getter<? extends A> getter) {
        if (!lock.lock()) {
            return;
        }

        try {
            if (getter.isValueValid()) {
                targetValue.setValue(getter.getValue());
            }
        } finally {
            lock.unlock();
        }
    }
}
