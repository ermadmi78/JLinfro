package com.github.linfro.core.reflection;

import org.junit.Test;

import static com.github.linfro.core.reflection.ReflectionUtil.isValidPropertyName;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-18
 * @since 1.0.0
 */
public class IsValidPropertyNameTest {
    @Test
    public void testInvalidName() throws Exception {
        assertFalse(isValidPropertyName(null));
        assertFalse(isValidPropertyName(""));
        assertFalse(isValidPropertyName(" "));
        assertFalse(isValidPropertyName("3ret"));
        assertFalse(isValidPropertyName("ret()"));
        assertFalse(isValidPropertyName("re~t"));
        assertFalse(isValidPropertyName("."));
        assertFalse(isValidPropertyName("first.second"));
        assertFalse(isValidPropertyName(".second"));
        assertFalse(isValidPropertyName("first."));
    }

    @Test
    public void testReservedWords() throws Exception {
        assertFalse(isValidPropertyName("abstract"));
        assertFalse(isValidPropertyName("assert"));
        assertFalse(isValidPropertyName("boolean"));
        assertFalse(isValidPropertyName("break"));
        assertFalse(isValidPropertyName("byte"));
        assertFalse(isValidPropertyName("case"));
        assertFalse(isValidPropertyName("catch"));
        assertFalse(isValidPropertyName("char"));
        assertFalse(isValidPropertyName("class"));
        assertFalse(isValidPropertyName("const"));
        assertFalse(isValidPropertyName("continue"));
        assertFalse(isValidPropertyName("default"));
        assertFalse(isValidPropertyName("do"));
        assertFalse(isValidPropertyName("double"));
        assertFalse(isValidPropertyName("else"));
        assertFalse(isValidPropertyName("enum"));
        assertFalse(isValidPropertyName("extends"));
        assertFalse(isValidPropertyName("final"));
        assertFalse(isValidPropertyName("finally"));
        assertFalse(isValidPropertyName("float"));
        assertFalse(isValidPropertyName("for"));
        assertFalse(isValidPropertyName("goto"));
        assertFalse(isValidPropertyName("if"));
        assertFalse(isValidPropertyName("implements"));
        assertFalse(isValidPropertyName("import"));
        assertFalse(isValidPropertyName("instanceof"));
        assertFalse(isValidPropertyName("int"));
        assertFalse(isValidPropertyName("interface"));
        assertFalse(isValidPropertyName("long"));
        assertFalse(isValidPropertyName("native"));
        assertFalse(isValidPropertyName("new"));
        assertFalse(isValidPropertyName("package"));
        assertFalse(isValidPropertyName("private"));
        assertFalse(isValidPropertyName("protected"));
        assertFalse(isValidPropertyName("public"));
        assertFalse(isValidPropertyName("return"));
        assertFalse(isValidPropertyName("short"));
        assertFalse(isValidPropertyName("static"));
        assertFalse(isValidPropertyName("strictfp"));
        assertFalse(isValidPropertyName("super"));
        assertFalse(isValidPropertyName("switch"));
        assertFalse(isValidPropertyName("synchronized"));
        assertFalse(isValidPropertyName("this"));
        assertFalse(isValidPropertyName("throw"));
        assertFalse(isValidPropertyName("throws"));
        assertFalse(isValidPropertyName("transient"));
        assertFalse(isValidPropertyName("try"));
        assertFalse(isValidPropertyName("void"));
        assertFalse(isValidPropertyName("volatile"));
        assertFalse(isValidPropertyName("while"));
    }

    @Test
    public void testValidWords() throws Exception {
        assertTrue(isValidPropertyName("_"));
        assertTrue(isValidPropertyName("$"));
        assertTrue(isValidPropertyName("a"));
        assertTrue(isValidPropertyName("_a"));
        assertTrue(isValidPropertyName("$a"));
        assertTrue(isValidPropertyName("name"));
        assertTrue(isValidPropertyName("name3"));
        assertTrue(isValidPropertyName("name3val"));
        assertTrue(isValidPropertyName("_pro5p"));
        assertTrue(isValidPropertyName("$pro4p"));
    }
}
