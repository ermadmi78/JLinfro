package com.github.linfro.core;

import com.github.linfro.core.common.AutoDisposable;
import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.common.Equality;
import com.github.linfro.core.dsl.ConsumerLink;
import com.github.linfro.core.dsl.Context;
import com.github.linfro.core.dsl.InOutLink;
import com.github.linfro.core.dsl.OutLink;
import com.github.linfro.core.value.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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

    protected static final class OutFlow<F> extends Flow<IOutFlow<F>, F, GetValue<F>> implements IOutFlow<F> {
        private OutFlow(GetValue<F> from, Context context) {
            super(from, context);
        }

        @Override
        protected IOutFlow<F> nextDSL() {
            return this;
        }

        @Override
        protected Disposable createLink(HasValue<F> to) {
            return new OutLink<>(from, to, context);
        }

        @Override
        public Disposable to(Consumer<? super F> consumer) {
            return new ConsumerLink<F>(from, autoDispose(consumer), context);
        }

        @Override
        public <T> IOutFlow<T> map(Function<F, T> function) {
            return new OutFlow<>(from.map(function), context);
        }

        @Override
        public <T> IOutFlow<T> mapNotNull(Function<F, T> function) {
            return new OutFlow<>(from.mapNotNull(function), context);
        }

        @Override
        public IOutFlow<F> nvl(F nullValue) {
            return new OutFlow<>(from.nvl(nullValue), context);
        }

        @Override
        public IOutFlow<F> filter(Predicate<? super F> predicate) {
            return new OutFlow<>(from.filter(predicate), context);
        }

        @Override
        public IOutFlow<F> putMetaInfo(String metaInfoKey, Object metaInfoValue) {
            return new OutFlow<>(from.putMetaInfo(metaInfoKey, metaInfoValue), context);
        }

        @Override
        public IOutFlow<F> named(String name) {
            return new OutFlow<>(from.named(name), context);
        }
    }

    protected static final class InOutFlow<F> extends Flow<IInOutFlow<F>, F, HasValue<F>> implements IInOutFlow<F> {
        private InOutFlow(HasValue<F> from, Context context) {
            super(from, context);
        }

        @Override
        protected IInOutFlow<F> nextDSL() {
            return this;
        }

        @Override
        protected Disposable createLink(HasValue<F> to) {
            return new InOutLink<>(from, to, context);
        }

        @Override
        public IInOutFlow<F> sync() {
            context.setSync(true);
            return nextDSL();
        }

        @Override
        public <T> IInOutFlow<T> map(Function<F, T> outFunc, Function<T, F> inFunc) {
            return new InOutFlow<>(from.map(outFunc, inFunc), context);
        }

        @Override
        public <T> IInOutFlow<T> mapNotNull(Function<F, T> outFunc, Function<T, F> inFunc) {
            return new InOutFlow<>(from.mapNotNull(outFunc, inFunc), context);
        }

        @Override
        public IInOutFlow<F> nvl(F outNullValue, F inNullValue) {
            return new InOutFlow<>(from.nvl(outNullValue, inNullValue), context);
        }

        @Override
        public IInOutFlow<F> filter(Predicate<? super F> outPredicate, Predicate<? super F> inPredicate) {
            return new InOutFlow<>(from.filter(outPredicate, inPredicate), context);
        }

        @Override
        public IInOutFlow<F> biMap(Function<F, F> inOutFunction) {
            return new InOutFlow<>(from.biMap(inOutFunction), context);
        }

        @Override
        public IInOutFlow<F> biMapNotNull(Function<F, F> inOutFunction) {
            return new InOutFlow<>(from.biMapNotNull(inOutFunction), context);
        }

        @Override
        public IInOutFlow<F> biNvl(F inOutNullValue) {
            return new InOutFlow<>(from.biNvl(inOutNullValue), context);
        }

        @Override
        public IInOutFlow<F> putMetaInfo(String metaInfoKey, Object metaInfoValue) {
            return new InOutFlow<>(from.putMetaInfo(metaInfoKey, metaInfoValue), context);
        }

        @Override
        public IInOutFlow<F> named(String name) {
            return new InOutFlow<>(from.named(name), context);
        }
    }

    protected static final class HybridFlow<F> extends Flow<IHybridFlow<F>, F, HasValue<F>> implements IHybridFlow<F> {
        private HybridFlow(HasValue<F> from, Context context) {
            super(from, context);
        }

        @Override
        protected IHybridFlow<F> nextDSL() {
            return this;
        }

        @Override
        protected Disposable createLink(HasValue<F> to) {
            return new InOutLink<>(from, to, context);
        }

        @Override
        public Disposable to(Consumer<? super F> consumer) {
            return new ConsumerLink<F>(from, autoDispose(consumer), context);
        }

        @Override
        public <T> IHybridFlow<T> map(Function<F, T> inFunc, Function<T, F> outFunc) {
            return new HybridFlow<>(from.map(inFunc, outFunc), context);
        }

        @Override
        public <T> IHybridFlow<T> mapNotNull(Function<F, T> inFunc, Function<T, F> outFunc) {
            return new HybridFlow<>(from.mapNotNull(inFunc, outFunc), context);
        }

        @Override
        public IHybridFlow<F> nvl(F outNullValue, F inNullValue) {
            return new HybridFlow<>(from.nvl(outNullValue, inNullValue), context);
        }

        @Override
        public IHybridFlow<F> filter(Predicate<? super F> outPredicate, Predicate<? super F> inPredicate) {
            return new HybridFlow<>(from.filter(outPredicate, inPredicate), context);
        }

        @Override
        public IHybridFlow<F> biMap(Function<F, F> inOutFunction) {
            return new HybridFlow<>(from.biMap(inOutFunction), context);
        }

        @Override
        public IHybridFlow<F> biMapNotNull(Function<F, F> inOutFunction) {
            return new HybridFlow<>(from.biMapNotNull(inOutFunction), context);
        }

        @Override
        public IHybridFlow<F> biNvl(F inOutNullValue) {
            return new HybridFlow<>(from.biNvl(inOutNullValue), context);
        }

        @Override
        public IHybridFlow<F> putMetaInfo(String metaInfoKey, Object metaInfoValue) {
            return new HybridFlow<>(from.putMetaInfo(metaInfoKey, metaInfoValue), context);
        }

        @Override
        public IHybridFlow<F> named(String name) {
            return new HybridFlow<>(from.named(name), context);
        }

        // In-out branch
        @Override
        public IInOutFlow<F> sync() {
            context.setSync(true);
            return new InOutFlow<>(from, context);
        }

        // Out branch
        @Override
        public <T> IOutFlow<T> map(Function<F, T> function) {
            return new OutFlow<>(from.map(function), context);
        }

        @Override
        public <T> IOutFlow<T> mapNotNull(Function<F, T> function) {
            return new OutFlow<>(from.mapNotNull(function), context);
        }

        @Override
        public IOutFlow<F> nvl(F nullValue) {
            return new OutFlow<>(from.nvl(nullValue), context);
        }

        @Override
        public IOutFlow<F> filter(Predicate<? super F> predicate) {
            return new OutFlow<>(from.filter(predicate), context);
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

    public static <F> IOutFlow<F> from(GetValue<F> from) {
        return new OutFlow<>(from, new Context());
    }

    public static <F> IHybridFlow<F> from(HasValue<F> from) {
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

    //******************************************************************************************************************
    //  Java Bean syntax
    //******************************************************************************************************************

    public static <F> IHybridFlow<F> fromProperty(Object bean, String property) {
        return null; //todo
    }

    public Disposable toProperty(Object bean, String property) {
        return null; //todo;
    }

    public static <X, F> IOutFlow<F> from(GetValue<X> beanValue, String property) {
        return null; //todo
    }

    public static <X, F> IHybridFlow<F> from(HasValue<X> beanValue, String property) {
        return null; //todo
    }

    public <X> Disposable to(HasValue<X> beanValue, String property) {
        //getClass().getGenericInterfaces()
        return null; //todo
    }
}
