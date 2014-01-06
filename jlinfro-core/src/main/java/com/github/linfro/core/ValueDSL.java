package com.github.linfro.core;

import com.github.linfro.core.common.*;
import com.github.linfro.core.dsl.Context;
import com.github.linfro.core.dsl.Link;
import com.github.linfro.core.value.GetValue;
import com.github.linfro.core.value.HasValue;
import com.github.linfro.core.value.TransformedGetValue;
import com.github.linfro.core.value.TransformedHasValue;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-04
 * @since 1.0.0
 */
public abstract class ValueDSL<DSL, F, SRC extends GetValue<F>> {
    protected final SRC from;
    protected final Context context;

    protected ValueDSL(SRC from, Context context) {
        this.from = from;
        this.context = context;
    }

    protected DSL addFromToDispose() {
        context.addToDispose(from);
        return nextDSL();
    }

    protected <A> A addToDispose(A obj) {
        context.addToDispose(obj);
        return obj;
    }

    protected abstract DSL nextDSL();

    public DSL strong() {
        context.setStrong(true);
        return nextDSL();
    }

    public DSL strong(Equality equality) {
        context.setStrong(true);
        context.setEquality(equality);
        return nextDSL();
    }

    public DSL force() {
        context.setForce(true);
        return nextDSL();
    }

    public Disposable to(HasValue<F> to) {
        return new Link<>(from, notNull(to), context);
    }

    //***************************************************************************************************

    public static final class OneWayDSL<F> extends ValueDSL<OneWayDSL<F>, F, GetValue<F>> {
        private OneWayDSL(GetValue<F> from, Context context) {
            super(from, context);
        }

        @Override
        protected OneWayDSL<F> nextDSL() {
            return this;
        }

        public <T> OneWayDSL<T> transform(Function<F, T> function) {
            return new OneWayDSL<>(new TransformedGetValue<>(from, function), context).addFromToDispose();
        }
    }

    public static final class BothWayDSL<F> extends ValueDSL<BothWayDSL<F>, F, HasValue<F>> {
        public BothWayDSL(HasValue<F> from, Context context) {
            super(from, context);
        }

        @Override
        protected BothWayDSL<F> nextDSL() {
            return this;
        }

        public BothWayDSL<F> sync() {
            context.setSync(true);
            return nextDSL();
        }

        public <T> BothWayDSL<T> transform(RevertFunction<F, T> function) {
            return new BothWayDSL<>(new TransformedHasValue<>(from, function), context).addFromToDispose();
        }

        public <T> BothWayDSL<T> transform(Function<F, T> function, Function<T, F> revertFunction) {
            return new BothWayDSL<>(new TransformedHasValue<>(
                    from, new DefaultRevertFunction<>(function, revertFunction)), context
            ).addFromToDispose();
        }
    }

    public static final class HybridDSL<F> extends ValueDSL<HybridDSL<F>, F, HasValue<F>> {
        public HybridDSL(HasValue<F> from, Context context) {
            super(from, context);
        }

        @Override
        protected HybridDSL<F> nextDSL() {
            return this;
        }

        public <T> HybridDSL<T> transform(RevertFunction<F, T> function) {
            return new HybridDSL<>(new TransformedHasValue<>(from, function), context).addFromToDispose();
        }

        public <T> HybridDSL<T> transform(Function<F, T> function, Function<T, F> revertFunction) {
            return new HybridDSL<>(new TransformedHasValue<>(
                    from, new DefaultRevertFunction<>(function, revertFunction)), context
            ).addFromToDispose();
        }

        // One way branch
        public <T> OneWayDSL<T> transform(Function<F, T> function) {
            return new OneWayDSL<>(new TransformedGetValue<>(from, function), context).addFromToDispose();
        }

        // Both way branch
        public BothWayDSL<F> sync() {
            context.setSync(true);
            return new BothWayDSL<>(from, context);
        }
    }

}
