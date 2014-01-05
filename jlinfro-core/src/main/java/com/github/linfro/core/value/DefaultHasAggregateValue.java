package com.github.linfro.core.value;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public class DefaultHasAggregateValue<T> extends AbstractGetValue<T> implements HasAggregateValue<T> {
    protected final Aggregator<T> aggregator;
    protected T initialValue;
    protected final List<HasDisposableValue<T>> members = new LinkedList<>();

    private T aggregation;
    private boolean aggregationCalculated = false;

    public DefaultHasAggregateValue(Aggregator<T> aggregator) {
        this(aggregator, null);
    }

    public DefaultHasAggregateValue(Aggregator<T> aggregator, T initialValue) {
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
    public HasDisposableValue<T> newMemberValue() {
        DisposableMember member = new DisposableMember(getInitialValue());
        members.add(member);
        fireValueChanged();

        return member;
    }

    protected T getInitialValue() {
        return initialValue;
    }

    private class DisposableMember extends AbstractHasValue<T> implements HasDisposableValue<T> {
        private T value;
        private boolean disposed = false;

        private DisposableMember(T value) {
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
                DefaultHasAggregateValue.this.fireValueChanged();
            }
            fireValueChanged();
        }

        @Override
        public void dispose() {
            if (!disposed) {
                disposed = true;
                members.remove(this);
                DefaultHasAggregateValue.this.fireValueChanged();
            }
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new InternalValueIterator();
    }

    private class InternalValueIterator implements Iterator<T> {
        private final Iterator<HasDisposableValue<T>> iterator = members.iterator();

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
