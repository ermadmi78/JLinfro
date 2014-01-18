package com.github.linfro.core.reflection;

import org.junit.Test;

import static com.github.linfro.core.reflection.ReflectionUtil.camelize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-18
 * @since 1.0.0
 */
public class TestCamelize {
    @Test
    public void testCamelize() throws Exception {
        assertNull(camelize(null));
        assertEquals("", camelize(""));
        assertEquals("A", camelize("a"));
        assertEquals("A", camelize("A"));
        assertEquals("Name", camelize("name"));
        assertEquals("Name", camelize("Name"));
        assertEquals("xRate", camelize("xRate")); // getter for 'xRate' is 'getxRate'
        assertEquals("Xrate", camelize("xrate"));
        assertEquals("NAME", camelize("NAME"));
    }
}
