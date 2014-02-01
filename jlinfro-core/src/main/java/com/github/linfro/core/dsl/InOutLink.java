package com.github.linfro.core.dsl;


import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.value.Getter;
import com.github.linfro.core.value.HasValue;
import com.github.linfro.core.value.ValueChangeListener;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-06
 * @since 1.0.0
 */
public class InOutLink<A> implements Disposable {
    private HasValue<A> from;
    private HasValue<A> to;
    private Context context;

    private boolean flag = false;

    public InOutLink(HasValue<A> from, HasValue<A> to, Context context) {
        this.from = notNull(from);
        this.to = notNull(to);
        this.context = notNull(context);

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
        if (context.isSync()) {
            to.addChangeListener(toListener);
        }
    }

    private final ValueChangeListener<A> fromListener = new ValueChangeListener<A>() {
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

    private final ValueChangeListener<A> toListener = new ValueChangeListener<A>() {
        @Override
        public void valueChanged(Getter<? extends A> getter) {
            if (flag) {
                return;
            }

            flag = true;
            try {
                A newValue = getter.getValue();
                if (!context.isStrong() || !context.getEquality().areEquals(newValue, from.getValue())) {
                    from.setValue(newValue);
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
