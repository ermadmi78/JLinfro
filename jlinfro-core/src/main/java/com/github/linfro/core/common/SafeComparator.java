package com.github.linfro.core.common;

import java.util.Comparator;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public class SafeComparator<T extends Comparable<T>> implements Comparator<T> {
    boolean reverse;

    public SafeComparator() {
        this(false);
    }

    public SafeComparator(boolean reverse) {
        this.reverse = reverse;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public int compare(T first, T second) {
        int res = ObjectUtil.compareObj(first, second);
        return reverse ? -res : res;
    }
}
