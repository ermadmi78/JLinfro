package com.github.linfro.core;

import com.github.linfro.core.common.AutoDisposable;
import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.common.Equality;
import com.github.linfro.core.dsl.ConsumerLink;
import com.github.linfro.core.dsl.Context;
import com.github.linfro.core.dsl.InOutLink;
import com.github.linfro.core.dsl.OutLink;
import com.github.linfro.core.value.DefaultGetAggregateValue;
import com.github.linfro.core.value.DefaultHasValue;
import com.github.linfro.core.value.ValueUtil;

import java.util.function.Consumer;

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

    protected static final class GetValueFlowImpl<F> extends Flow<GetValueFlow<F>, F, GetValue<F>> implements GetValueFlow<F> {
        public GetValueFlowImpl(GetValue<F> from, Context context) {
            super(from, context);
        }

        @Override
        protected GetValueFlow<F> nextDSL() {
            return this;
        }

        @Override
        protected Disposable createLink(HasValue<F> to) {
            return new OutLink<>(from, to, context);
        }
    }

    protected static final class HasValueFlowImpl<F> extends Flow<HasValueFlow<F>, F, HasValue<F>> implements HasValueFlow<F> {
        public HasValueFlowImpl(HasValue<F> from, Context context) {
            super(from, context);
        }

        @Override
        protected HasValueFlow<F> nextDSL() {
            return this;
        }

        @Override
        protected Disposable createLink(HasValue<F> to) {
            return new InOutLink<>(from, to, context);
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

    public static <F> GetValueFlow<F> from(GetValueHolder<F> from) {
        return new GetValueFlowImpl<>(notNull(from).getContentValue(), new Context());
    }

    public static <F> HasValueFlow<F> from(HasValueHolder<F> from) {
        return new HasValueFlowImpl<>(notNull(from).getContentValue(), new Context());
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

    public DSL sync() {
        context.setSync(true);
        return nextDSL();
    }

    public Disposable to(HasValueHolder<F> to) {
        return createLink(autoDispose(notNull(to).getContentValue()));
    }

    public Disposable to(Consumer<? super F> consumer) {
        return new ConsumerLink<>(from, autoDispose(consumer), context);
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

    //******************************************************************************************************************
    //  Java Bean syntax
    //******************************************************************************************************************

    public static <F> HasValueFlow<F> fromProperty(Object bean, String property) {
        return null; //todo
    }

    public Disposable toProperty(Object bean, String property) {
        return null; //todo;
    }

    public static <X, F> GetValueFlow<F> from(GetValueHolder<X> beanValue, String property) {
        return null; //todo
    }

    public static <X, F> HasValueFlow<F> from(HasValueHolder<X> beanValue, String property) {
        return null; //todo
    }

    public <X> Disposable to(HasValueHolder<X> beanValue, String property) {
        //getClass().getGenericInterfaces()
        return null; //todo
    }
}
