package com.github.linfro.core.reflection;

import org.junit.Test;

import static com.github.linfro.core.reflection.ReflectionUtil.*;
import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-20
 * @since 1.0.0
 */
public class ReflectionUtilTest {
    @Test
    public void testPrimitiveToWrapMap() throws Exception {
        assertNotNull(PRIMITIVE_TO_WRAP_MAP);
        assertEquals(8, PRIMITIVE_TO_WRAP_MAP.size());

        assertEquals(Boolean.class, PRIMITIVE_TO_WRAP_MAP.get(Boolean.TYPE));
        assertEquals(Character.class, PRIMITIVE_TO_WRAP_MAP.get(Character.TYPE));
        assertEquals(Byte.class, PRIMITIVE_TO_WRAP_MAP.get(Byte.TYPE));
        assertEquals(Short.class, PRIMITIVE_TO_WRAP_MAP.get(Short.TYPE));
        assertEquals(Integer.class, PRIMITIVE_TO_WRAP_MAP.get(Integer.TYPE));
        assertEquals(Long.class, PRIMITIVE_TO_WRAP_MAP.get(Long.TYPE));
        assertEquals(Float.class, PRIMITIVE_TO_WRAP_MAP.get(Float.TYPE));
        assertEquals(Double.class, PRIMITIVE_TO_WRAP_MAP.get(Double.TYPE));

        assertNull(PRIMITIVE_TO_WRAP_MAP.get(String.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPrimitiveToWrapMapIsImmutable() throws Exception {
        PRIMITIVE_TO_WRAP_MAP.put(String.class, void.class);
    }

    @Test
    public void testWrapToPrimitiveMap() throws Exception {
        assertNotNull(WRAP_TO_PRIMITIVE_MAP);
        assertEquals(8, WRAP_TO_PRIMITIVE_MAP.size());

        assertEquals(Boolean.TYPE, WRAP_TO_PRIMITIVE_MAP.get(Boolean.class));
        assertEquals(Character.TYPE, WRAP_TO_PRIMITIVE_MAP.get(Character.class));
        assertEquals(Byte.TYPE, WRAP_TO_PRIMITIVE_MAP.get(Byte.class));
        assertEquals(Short.TYPE, WRAP_TO_PRIMITIVE_MAP.get(Short.class));
        assertEquals(Integer.TYPE, WRAP_TO_PRIMITIVE_MAP.get(Integer.class));
        assertEquals(Long.TYPE, WRAP_TO_PRIMITIVE_MAP.get(Long.class));
        assertEquals(Float.TYPE, WRAP_TO_PRIMITIVE_MAP.get(Float.class));
        assertEquals(Double.TYPE, WRAP_TO_PRIMITIVE_MAP.get(Double.class));

        assertNull(WRAP_TO_PRIMITIVE_MAP.get(String.class));
    }

    @Test
    public void testIsPrimitive() throws Exception {
        assertTrue(isPrimitive(Boolean.TYPE));
        assertTrue(isPrimitive(Character.TYPE));
        assertTrue(isPrimitive(Byte.TYPE));
        assertTrue(isPrimitive(Short.TYPE));
        assertTrue(isPrimitive(Integer.TYPE));
        assertTrue(isPrimitive(Long.TYPE));
        assertTrue(isPrimitive(Float.TYPE));
        assertTrue(isPrimitive(Double.TYPE));

        assertFalse(isPrimitive(Boolean.class));
        assertFalse(isPrimitive(Character.class));
        assertFalse(isPrimitive(Byte.class));
        assertFalse(isPrimitive(Short.class));
        assertFalse(isPrimitive(Integer.class));
        assertFalse(isPrimitive(Long.class));
        assertFalse(isPrimitive(Float.class));
        assertFalse(isPrimitive(Double.class));

        assertFalse(isPrimitive(String.class));
    }

    @Test
    public void testPrimitiveToWrap() throws Exception {
        assertEquals(Boolean.class, primitiveToWrap(Boolean.TYPE));
        assertEquals(Character.class, primitiveToWrap(Character.TYPE));
        assertEquals(Byte.class, primitiveToWrap(Byte.TYPE));
        assertEquals(Short.class, primitiveToWrap(Short.TYPE));
        assertEquals(Integer.class, primitiveToWrap(Integer.TYPE));
        assertEquals(Long.class, primitiveToWrap(Long.TYPE));
        assertEquals(Float.class, primitiveToWrap(Float.TYPE));
        assertEquals(Double.class, primitiveToWrap(Double.TYPE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrimitiveToWrapException() throws Exception {
        primitiveToWrap(String.class);
    }

    @Test
    public void testIsWrap() throws Exception {
        assertTrue(isWrap(Boolean.class));
        assertTrue(isWrap(Character.class));
        assertTrue(isWrap(Byte.class));
        assertTrue(isWrap(Short.class));
        assertTrue(isWrap(Integer.class));
        assertTrue(isWrap(Long.class));
        assertTrue(isWrap(Float.class));
        assertTrue(isWrap(Double.class));

        assertFalse(isWrap(Boolean.TYPE));
        assertFalse(isWrap(Character.TYPE));
        assertFalse(isWrap(Byte.TYPE));
        assertFalse(isWrap(Short.TYPE));
        assertFalse(isWrap(Integer.TYPE));
        assertFalse(isWrap(Long.TYPE));
        assertFalse(isWrap(Float.TYPE));
        assertFalse(isWrap(Double.TYPE));

        assertFalse(isWrap(String.class));
    }

    @Test
    public void testWrapToPrimitive() throws Exception {
        assertEquals(Boolean.TYPE, wrapToPrimitive(Boolean.class));
        assertEquals(Character.TYPE, wrapToPrimitive(Character.class));
        assertEquals(Byte.TYPE, wrapToPrimitive(Byte.class));
        assertEquals(Short.TYPE, wrapToPrimitive(Short.class));
        assertEquals(Integer.TYPE, wrapToPrimitive(Integer.class));
        assertEquals(Long.TYPE, wrapToPrimitive(Long.class));
        assertEquals(Float.TYPE, wrapToPrimitive(Float.class));
        assertEquals(Double.TYPE, wrapToPrimitive(Double.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapToPrimitiveException() throws Exception {
        wrapToPrimitive(String.class);
    }
}
