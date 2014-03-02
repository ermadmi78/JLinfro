package com.github.linfro.core.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Dmitry Ermakov
 * @version 2014-03-02
 * @since 1.0.0
 */
public class TestUtil {
    public static void assertDisposed(Runnable runnable) {
        try {
            runnable.run();
            fail("Disposed wrapper must throw exception");
        } catch (IllegalStateException e) {
            assertEquals("Value is disposed", e.getMessage());
        }
    }
}
