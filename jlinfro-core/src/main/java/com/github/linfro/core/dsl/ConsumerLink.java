package com.github.linfro.core.dsl;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.value.GetValue;
import com.github.linfro.core.value.Getter;
import com.github.linfro.core.value.ValueChangeListener;

import java.util.function.Consumer;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-06
 * @since 1.0.0
 */
public class ConsumerLink<A> implements Disposable {
    private GetValue<A> from;
    private Consumer<? super A> to;
    private Context context;

    public ConsumerLink(GetValue<A> from, Consumer<? super A> to, Context context) {
        notNull(context);
        if (context.isSync()) {
            throw new IllegalArgumentException("Cannot create ConsumerLink for sync context");
        }

        this.from = notNull(from);
        this.to = notNull(to);
        this.context = context;

        link();
    }

    private void link() {
        if ((context.getSourceApplyDelay() != null) && (context.getDestinationApplyDelay() != null)) {
            context.getDestinationApplyDelay().setApplyDelay(context.getSourceApplyDelay().getApplyDelay());
        }

        if (context.isForce()) {
            to.accept(from.getValue());
        }

        from.addChangeListener(fromListener);
    }

    private final ValueChangeListener<A> fromListener = new ValueChangeListener<A>() {
        @Override
        public void valueChanged(Getter<? extends A> getter) {
            to.accept(getter.getValue());
        }
    };

    @Override
    public void dispose() {
        if ((from == null) || (to == null) || (context == null)) {
            return;
        }

        from.removeChangeListener(fromListener);
        context.dispose();

        from = null;
        to = null;
        context = null;
    }
}
