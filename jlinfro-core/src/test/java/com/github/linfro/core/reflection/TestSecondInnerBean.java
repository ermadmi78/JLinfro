package com.github.linfro.core.reflection;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-25
 * @since 1.0.0
 */
public class TestSecondInnerBean {
    private int value = 1;

    public TestSecondInnerBean() {
    }

    public TestSecondInnerBean(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
