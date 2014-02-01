package com.github.linfro.core;

import com.github.linfro.core.common.*;
import com.github.linfro.core.dsl.Context;
import com.github.linfro.core.dsl.InOutLink;
import com.github.linfro.core.dsl.OutLink;
import com.github.linfro.core.value.*;

import java.util.function.Function;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-06
 * @since 1.0.0
 */
public abstract class Flow<DSL, F, SRC extends GetValue<F>> {
    protected final SRC from;
    protected final Context context;

    private Flow(SRC from, Context context) {
        this.from = notNull(from);
        this.context = notNull(context);
        autoDispose(this.from);
    }

    protected final <A> A autoDispose(A obj) {
        if ((obj instanceof AutoDisposable) && ((AutoDisposable) obj).isAutoDispose()) {
            context.addToDispose(obj);
        }
        return obj;
    }

    protected abstract DSL nextDSL();

    protected abstract Disposable createLink(HasValue<F> to);

    //******************************************************************************************************************
    //  Transformation syntax
    //******************************************************************************************************************

    public static final class OutFlow<F> extends Flow<OutFlow<F>, F, GetValue<F>> {
        private OutFlow(GetValue<F> from, Context context) {
            super(from, context);
        }

        @Override
        protected OutFlow<F> nextDSL() {
            return this;
        }

        @Override
        protected Disposable createLink(HasValue<F> to) {
            return new OutLink<F>(from, to, context);
        }

        public <T> OutFlow<T> map(Function<F, T> function) {
            return new OutFlow<>(from.map(function), context);
        }
    }

    public static final class InOutFlow<F> extends Flow<InOutFlow<F>, F, HasValue<F>> {
        private InOutFlow(HasValue<F> from, Context context) {
            super(from, context);
        }

        @Override
        protected InOutFlow<F> nextDSL() {
            return this;
        }

        @Override
        protected Disposable createLink(HasValue<F> to) {
            return new InOutLink<F>(from, to, context);
        }

        public InOutFlow<F> sync() {
            context.setSync(true);
            return nextDSL();
        }

        public <T> InOutFlow<T> map(RevertFunction<F, T> function) {
            return new InOutFlow<T>(new TransformedHasValue<F, T>(from, function), context);
        }

        public <T> InOutFlow<T> map(Function<F, T> function, Function<T, F> revertFunction) {
            return new InOutFlow<T>(new TransformedHasValue<F, T>(
                    from, new DefaultRevertFunction<F, T>(function, revertFunction)), context
            );
        }
    }

    public static final class HybridFlow<F> extends Flow<HybridFlow<F>, F, HasValue<F>> {
        private HybridFlow(HasValue<F> from, Context context) {
            super(from, context);
        }

        @Override
        protected HybridFlow<F> nextDSL() {
            return this;
        }

        @Override
        protected Disposable createLink(HasValue<F> to) {
            return new InOutLink<F>(from, to, context);
        }

        public <T> HybridFlow<T> map(RevertFunction<F, T> function) {
            return new HybridFlow<T>(new TransformedHasValue<F, T>(from, function), context);
        }

        public <T> HybridFlow<T> map(Function<F, T> function, Function<T, F> revertFunction) {
            return new HybridFlow<T>(new TransformedHasValue<F, T>(
                    from, new DefaultRevertFunction<F, T>(function, revertFunction)), context
            );
        }

        // One way branch
        public <T> OutFlow<T> map(Function<F, T> function) {
            return new OutFlow<T>(new TransformedGetValue<F, T>(from, function), context);
        }

        // Both way branch
        public InOutFlow<F> sync() {
            context.setSync(true);
            return new InOutFlow<>(from, context);
        }
    }

    //******************************************************************************************************************
    //  Common syntax
    //******************************************************************************************************************

    public static <A> DefaultHasValue<A> newHasValue() {
        return new DefaultHasValue<>();
    }

    public static <A> DefaultHasValue<A> newHasValue(A value) {
        return new DefaultHasValue<>(value);
    }

    public static <F> OutFlow<F> from(GetValue<F> from) {
        return new OutFlow<>(from, new Context());
    }

    public static <F> HybridFlow<F> from(HasValue<F> from) {
        return new HybridFlow<>(from, new Context());
    }

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
        return createLink(autoDispose(to));
    }

    //******************************************************************************************************************
    //  Aggregation syntax
    //******************************************************************************************************************

    public Disposable to(GetAggregateValue<F> aggregateValue) {
        notNull(aggregateValue);
        return to(notNull(aggregateValue.newArgument()));
    }

    public static <A> DefaultGetAggregateValue<A> newAggregateValue(Aggregator<A> aggregator) {
        notNull(aggregator);
        return new DefaultGetAggregateValue<>(aggregator);
    }

    public static DefaultGetAggregateValue<Boolean> andValue() {
        return newAggregateValue(ValueUtil.AGGREGATOR_AND);
    }

    public static DefaultGetAggregateValue<Boolean> orValue() {
        return newAggregateValue(ValueUtil.AGGREGATOR_OR);
    }

    public static RevertFunction<Boolean, Boolean> not() {
        return ValueUtil.NOT_FUNCTION;
    }

    //******************************************************************************************************************
    //  Java Bean syntax
    //******************************************************************************************************************

    public static <F> HybridFlow<F> fromProperty(Object bean, String property) {
        return null; //todo
    }

    public Disposable toProperty(Object bean, String property) {
        return null; //todo;
    }

    public static <X, F> OutFlow<F> from(GetValue<X> beanValue, String property) {
        return null; //todo
    }

    public static <X, F> HybridFlow<F> from(HasValue<X> beanValue, String property) {
        return null; //todo
    }

    public <X> Disposable to(HasValue<X> beanValue, String property) {
        //getClass().getGenericInterfaces()
        return null; //todo
    }
}
