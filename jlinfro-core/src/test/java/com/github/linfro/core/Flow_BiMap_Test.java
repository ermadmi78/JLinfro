package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-09
 * @since 1.0.0
 */
public class Flow_BiMap_Test {
    @Test
    public void testBiMap() throws Exception {
        HasValue<Boolean> a = Values.newHasValue();
        HasValue<Boolean> b = Values.newHasValue();

        assertNull(a.getValue());
        assertNull(b.getValue());

        Disposable link = a.biMap(
                (val) -> val == null ? null : !val
        ).flow().sync().force().to(b);

        assertNull(a.getValue());
        assertNull(b.getValue());

        a.setValue(true);

        assertTrue(a.getValue());
        assertFalse(b.getValue());

        b.setValue(true);

        assertFalse(a.getValue());
        assertTrue(b.getValue());

        a.setValue(null);

        assertNull(a.getValue());
        assertNull(b.getValue());

        a.setValue(false);

        assertFalse(a.getValue());
        assertTrue(b.getValue());

        b.setValue(null);

        assertNull(a.getValue());
        assertNull(b.getValue());

        link.dispose();
        a.setValue(false);
        b.setValue(false);

        assertFalse(a.getValue());
        assertFalse(b.getValue());
    }
}
