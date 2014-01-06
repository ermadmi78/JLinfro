package com.github.linfro.core.dsl;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.value.GetValue;
import com.github.linfro.core.value.Getter;
import com.github.linfro.core.value.HasValue;
import com.github.linfro.core.value.ValueChangeListener;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-06
 * @since 1.0.0
 */
public class OneWayLink<A> implements Disposable {
    private GetValue<A> from;
    private HasValue<A> to;
    private Context context;

    public OneWayLink(GetValue<A> from, HasValue<A> to, Context context) {
        notNull(context);
        if (context.isSync()) {
            throw new IllegalArgumentException("Cannot create OneWayLink for sync context");
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
            A newValue = from.getValue();
            if (!context.isStrong() || !context.getEquality().areEquals(newValue, to.getValue())) {
                to.setValue(newValue);
            }
        }

        from.addChangeListener(fromListener);
    }

    private final ValueChangeListener<A> fromListener = new ValueChangeListener<A>() {
        @Override
        public void valueChanged(Getter<? extends A> getter) {
            A newValue = getter.getValue();
            if (!context.isStrong() || !context.getEquality().areEquals(newValue, to.getValue())) {
                to.setValue(newValue);
            }
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
