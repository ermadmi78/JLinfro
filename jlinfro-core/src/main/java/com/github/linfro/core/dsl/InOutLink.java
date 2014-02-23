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
public class InOutLink<A> implements Disposable {
    protected final HasValue<A> from;
    protected final HasValue<A> to;
    protected final Context context;

    protected final UnsafeLock lock = new UnsafeLock();
    protected final ValueChangeListener<A> fromListener;
    protected final ValueChangeListener<A> toListener;

    protected boolean disposed = false;

    public InOutLink(HasValue<A> from, HasValue<A> to, Context context) {
        this.from = notNull(from);
        this.to = notNull(to);
        this.context = notNull(context);

        this.fromListener = new LinkListener<>(this.to, this.context, this.lock);
        this.toListener = new LinkListener<>(this.from, this.context, this.lock);

        if (this.context.isForce()) {
            this.fromListener.valueChanged(this.from);
        }

        this.from.addChangeListener(this.fromListener);
        if (this.context.isSync()) {
            this.to.addChangeListener(this.toListener);
        }
    }

    @Override
    public void dispose() {
        if (disposed) {
            return;
        }

        from.removeChangeListener(fromListener);
        if (context.isSync()) {
            to.removeChangeListener(toListener);
        }

        if (from.isAutoDispose()) {
            from.dispose();
        }
        if (to.isAutoDispose()) {
            to.dispose();
        }

        disposed = true;
    }
}
