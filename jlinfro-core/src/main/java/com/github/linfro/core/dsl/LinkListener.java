package com.github.linfro.core.dsl;

import com.github.linfro.core.value.FilterException;
import com.github.linfro.core.value.Getter;
import com.github.linfro.core.value.HasValue;
import com.github.linfro.core.value.ValueChangeListener;

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
            A incoming = getter.getValue();

            boolean update = true;
            if (context.isStrong()) {
                try {
                    if (context.getEquality().areEquals(incoming, targetValue.getValue())) {
                        update = false;
                    }
                } catch (FilterException e) {
                    // Target value doesn't match some filter conditions -
                    // we don't know are incoming and target values equals or not.
                    // So we have to update target value.
                }
            }

            if (update) {
                targetValue.setValue(incoming);
            }
        } catch (FilterException e) {
            // Incoming value doesn't match some filter conditions - do nothing
        } finally {
            lock.unlock();
        }
    }
}
