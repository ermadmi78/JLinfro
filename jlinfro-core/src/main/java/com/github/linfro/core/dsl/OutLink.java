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
public class OutLink<A> implements Disposable {
    protected final GetValue<A> from;
    protected final HasValue<A> to;
    protected final Context context;

    protected final UnsafeLock lock = new UnsafeLock();
    protected final ValueChangeListener<A> fromListener;

    protected boolean disposed = false;

    public OutLink(GetValue<A> from, HasValue<A> to, Context context) {
        notNull(context);
        if (context.isSync()) {
            throw new IllegalArgumentException("Cannot create OutLink for sync context");
        }

        this.from = notNull(from);
        this.to = notNull(to);
        this.context = context;

        this.fromListener = new LinkListener<>(this.to, this.context, this.lock);

        if (this.context.isForce()) {
            this.fromListener.valueChanged(this.from);
        }

        this.from.addChangeListener(this.fromListener);
    }

    @Override
    public void dispose() {
        if (disposed) {
            return;
        }

        disposed = true;

        from.removeChangeListener(fromListener);
        context.dispose();
    }
}
