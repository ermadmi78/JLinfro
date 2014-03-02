package com.github.linfro.core.dsl;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.HasValue;
import com.github.linfro.core.ValueChangeListener;
import com.github.linfro.core.common.Disposable;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-06
 * @since 1.0.0
 */
public class SimpleLink<A> implements Disposable {
    protected GetValue<A> from;
    protected HasValue<A> to;
    protected Context context;

    protected final UnsafeLock lock = new UnsafeLock();
    protected ValueChangeListener<A> fromListener;

    /**
     * Fake listener for "to" value is used to prevent cascade "to" value dispose
     */
    protected final ValueChangeListener<A> toListener = new FakeListener<>();

    public SimpleLink(GetValue<A> from, HasValue<A> to, Context context) {
        notNull(context);
        if (context.isSync()) {
            throw new IllegalArgumentException("Cannot create SimpleLink for sync context");
        }

        this.from = notNull(from);
        this.to = notNull(to);
        this.context = context;

        this.fromListener = new LinkListener<>(this.to, this.context, this.lock);

        if (this.context.isForce()) {
            this.fromListener.valueChanged(this.from);
        }

        this.from.addChangeListener(this.fromListener);
        this.to.addChangeListener(toListener);
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
            to.removeChangeListener(toListener);
            if (to.canDispose()) {
                to.dispose();
            }
        }

        from = null;
        to = null;
        context = null;
        fromListener = null;
    }
}
