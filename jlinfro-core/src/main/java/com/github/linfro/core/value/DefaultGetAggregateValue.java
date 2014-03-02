package com.github.linfro.core.value;

import com.github.linfro.core.GetAggregateValue;
import com.github.linfro.core.HasValue;
import com.github.linfro.core.common.Aggregator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public class DefaultGetAggregateValue<T> extends AbstractGetValue<T> implements GetAggregateValue<T> {
    protected final Aggregator<T> aggregator;
    protected T initialValue;
    protected final List<HasValue<T>> arguments = new LinkedList<>();

    private T aggregation;
    private boolean aggregationCalculated = false;

    public DefaultGetAggregateValue(Aggregator<T> aggregator) {
        this(aggregator, null);
    }

    public DefaultGetAggregateValue(Aggregator<T> aggregator, T initialValue) {
        this.aggregator = notNull(aggregator);
        this.initialValue = initialValue;
    }

    @Override
    public T getValue() {
        if (!aggregationCalculated) {
            aggregation = aggregator.aggregate(this);
            aggregationCalculated = true;
        }
        return aggregation;
    }

    @Override
    public void fireValueChanged() {
        aggregationCalculated = false;
        super.fireValueChanged();
    }

    @Override
    public HasValue<T> newArgument() {
        DisposableArgument member = new DisposableArgument(getInitialValue());
        arguments.add(member);
        fireValueChanged();

        return member;
    }

    protected T getInitialValue() {
        return initialValue;
    }

    private class DisposableArgument extends AbstractHasValue<T> {
        private T value;
        private boolean disposed = false;

        private DisposableArgument(T value) {
            this.value = value;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public void setValue(T value) {
            this.value = value;
            if (!disposed) {
                DefaultGetAggregateValue.this.fireValueChanged();
            }
            super.fireValueChanged();
        }

        @Override
        public void dispose() {
            if (!disposed) {
                disposed = true;
                arguments.remove(this);
                DefaultGetAggregateValue.this.fireValueChanged();
            }
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new InternalValueIterator();
    }

    private class InternalValueIterator implements Iterator<T> {
        private final Iterator<HasValue<T>> iterator = arguments.iterator();

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            HasValue<T> member = iterator.next();
            return member == null ? null : member.getValue();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
