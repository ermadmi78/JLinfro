package com.github.linfro.core.common;

import java.util.Iterator;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public class IterableForArray<I> implements Iterable<I> {
    private I[] array;

    public IterableForArray(I[] array) {
        this.array = array;
    }

    @Override
    public Iterator<I> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<I> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return (array != null) && (index < array.length);
        }

        @Override
        public I next() {
            if (array == null) {
                throw new ArrayIndexOutOfBoundsException(index);
            }

            return array[index++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
