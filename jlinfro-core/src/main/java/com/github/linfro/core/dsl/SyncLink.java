package com.github.linfro.core.dsl;


import com.github.linfro.core.HasValue;
import com.github.linfro.core.ValueChangeListener;
import com.github.linfro.core.common.Disposable;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-06
 * @since 1.0.0
 */
public class SyncLink<A> implements Disposable {
    protected HasValue<A> from;
    protected HasValue<A> to;
    protected Context context;

    protected final UnsafeLock lock = new UnsafeLock();
    protected ValueChangeListener<A> fromListener;
    protected ValueChangeListener<A> toListener;

    public SyncLink(HasValue<A> from, HasValue<A> to, Context context) {
        this.from = notNull(from);
        this.to = notNull(to);
        this.context = notNull(context);

        this.fromListener = new LinkListener<>(this.to, this.context, this.lock);
        this.toListener = this.context.isSync() ?
                new LinkListener<>(this.from, this.context, this.lock) : new FakeListener<>();

        if (this.context.isForce()) {
            this.fromListener.valueChanged(this.from);
        }

        this.from.addChangeListener(this.fromListener);
        this.to.addChangeListener(this.toListener);
    }

    @Override
    public void dispose() {
        if (from != null) {
            if (fromListener != null) {
                from.removeChangeListener(fromListener);
            }

            if (from.canDispose()) {
                from.dispose();
            }
        }

        if (to != null) {
            if (toListener != null) {
                to.removeChangeListener(toListener);
            }

            if (to.canDispose()) {
                to.dispose();
            }
        }

        from = null;
        to = null;
        context = null;
        fromListener = null;
        toListener = null;
    }
}
