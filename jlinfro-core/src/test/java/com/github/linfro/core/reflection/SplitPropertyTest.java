package com.github.linfro.core.reflection;

import org.junit.Test;

import static com.github.linfro.core.reflection.ReflectionUtil.splitProperty;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-18
 * @since 1.0.0
 */
public class SplitPropertyTest {
    @Test(expected = IllegalArgumentException.class)
    public void testNull() throws Exception {
        splitProperty(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmpty() throws Exception {
        splitProperty("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPoint() throws Exception {
        splitProperty(".");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartPoint() throws Exception {
        splitProperty(".my");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEndPoint() throws Exception {
        splitProperty("my.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBothPoint() throws Exception {
        splitProperty(".my.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerial2Point() throws Exception {
        splitProperty("first..second");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerial3Point() throws Exception {
        splitProperty("first...second");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpace() throws Exception {
        splitProperty("first name.second name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidName1() throws Exception {
        splitProperty("3name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidName2() throws Exception {
        splitProperty("for");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidName3() throws Exception {
        splitProperty("name.else");
    }

    @Test()
    public void testValid1() throws Exception {
        String[] res = splitProperty("a");
        assertArrayEquals(new String[]{"a"}, res);
    }

    @Test()
    public void testValid2() throws Exception {
        String[] res = splitProperty("prop");
        assertArrayEquals(new String[]{"prop"}, res);
    }

    @Test()
    public void testValid3() throws Exception {
        String[] res = splitProperty("a.b");
        assertArrayEquals(new String[]{"a", "b"}, res);
    }

    @Test()
    public void testValid4() throws Exception {
        String[] res = splitProperty("a.b.c");
        assertArrayEquals(new String[]{"a", "b", "c"}, res);
    }

    @Test()
    public void testValid5() throws Exception {
        String[] res = splitProperty("a.second");
        assertArrayEquals(new String[]{"a", "second"}, res);
    }

    @Test()
    public void testValid6() throws Exception {
        String[] res = splitProperty("first.b");
        assertArrayEquals(new String[]{"first", "b"}, res);
    }

    @Test()
    public void testValid7() throws Exception {
        String[] res = splitProperty("first.second");
        assertArrayEquals(new String[]{"first", "second"}, res);
    }

    @Test()
    public void testValid8() throws Exception {
        String[] res = splitProperty("First.Second.Third");
        assertArrayEquals(new String[]{"First", "Second", "Third"}, res);
    }
}
