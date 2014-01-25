package com.github.linfro.core.reflection;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-25
 * @since 1.0.0
 */
public class TestRootBean {
    private TestFirstInnerBean first = new TestFirstInnerBean();

    public TestFirstInnerBean getFirst() {
        return first;
    }

    public void setFirst(TestFirstInnerBean first) {
        this.first = first;
    }
}
