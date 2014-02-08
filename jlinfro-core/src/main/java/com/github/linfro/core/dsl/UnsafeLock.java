package com.github.linfro.core.dsl;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-08
 * @since 1.0.0
 */
public class UnsafeLock {
    private boolean locked = false;

    public boolean lock() {
        if (!locked) {
            locked = true;
            return true;
        } else {
            return false;
        }
    }

    public void unlock() {
        locked = false;
    }
}
