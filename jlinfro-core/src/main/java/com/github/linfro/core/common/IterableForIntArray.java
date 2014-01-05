package com.github.linfro.core.common;

import java.util.Iterator;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public class IterableForIntArray implements Iterable<Integer> {
    private int[] array;

    public IterableForIntArray(int[] array) {
        this.array = array;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Integer> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return (array != null) && (index < array.length);
        }

        @Override
        public Integer next() {
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
