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
public class Link<A> implements Disposable {
    private GetValue<A> from;
    private HasValue<A> to;
    private Context context;

    private boolean flag = false;

    public Link(GetValue<A> from, HasValue<A> to, Context context) {
        this.from = notNull(from);
        this.to = notNull(to);
        this.context = notNull(context);

        link();
    }

    private void link() {
        if (context.isSync() && !(from instanceof HasValue)) {
            throw new IllegalArgumentException("Cannot create bidirectional link for source of GetValue type");
        }


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
        if (context.isSync()) {
            to.addChangeListener(toListener);
        }
    }

    private ValueChangeListener<A> fromListener = new ValueChangeListener<A>() {
        @Override
        public void valueChanged(Getter<? extends A> getter) {
            if (flag) {
                return;
            }

            flag = true;
            try {
                A newValue = getter.getValue();
                if (!context.isStrong() || !context.getEquality().areEquals(newValue, to.getValue())) {
                    to.setValue(newValue);
                }
            } finally {
                flag = false;
            }
        }
    };

    private ValueChangeListener<A> toListener = new ValueChangeListener<A>() {
        @Override
        public void valueChanged(Getter<? extends A> getter) {
            if (flag) {
                return;
            }

            flag = true;
            try {
                A newValue = getter.getValue();
                if (!context.isStrong() || !context.getEquality().areEquals(newValue, from.getValue())) {
                    ((HasValue<A>) from).setValue(newValue);
                }
            } finally {
                flag = false;
            }
        }
    };

    @Override
    public void dispose() {
        if ((from == null) || (to == null) || (context == null)) {
            return;
        }

        from.removeChangeListener(fromListener);
        if (context.isSync()) {
            to.removeChangeListener(toListener);
        }
        context.dispose();

        from = null;
        to = null;
        context = null;
    }
}
