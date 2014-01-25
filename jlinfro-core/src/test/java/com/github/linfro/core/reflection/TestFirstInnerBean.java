package com.github.linfro.core.reflection;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-25
 * @since 1.0.0
 */
public class TestFirstInnerBean {
    private TestSecondInnerBean second = new TestSecondInnerBean();
    private TestSecondInnerBean readOnly = new TestSecondInnerBean(25);

    public TestSecondInnerBean getSecond() {
        return second;
    }

    public void setSecond(TestSecondInnerBean second) {
        this.second = second;
    }

    public TestSecondInnerBean getReadOnly() {
        return readOnly;
    }
}
