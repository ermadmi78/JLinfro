package com.github.linfro.core.dsl;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.ValueChangeListener;
import com.github.linfro.core.common.Disposable;

import java.util.function.Consumer;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-06
 * @since 1.0.0
 */
public class ConsumerLink<A> implements Disposable {
    protected GetValue<A> from;
    protected Consumer<? super A> to;
    protected Context context;

    protected ValueChangeListener<A> fromListener;

    public ConsumerLink(GetValue<A> from, Consumer<? super A> to, Context context) {
        notNull(context);
        if (context.isSync()) {
            throw new IllegalArgumentException("Cannot create ConsumerLink for sync context");
        }

        this.from = notNull(from);
        this.to = notNull(to);
        this.context = context;

        this.fromListener = new ConsumerListener<>(this.to);

        if (this.context.isForce()) {
            this.fromListener.valueChanged(this.from);
        }

        this.from.addChangeListener(fromListener);
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

        from = null;
        to = null;
        context = null;
        fromListener = null;
    }
}
