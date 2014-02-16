package com.github.linfro.core.value;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-15
 * @since 1.0.0
 */
public class TestListener implements ValueChangeListener<Object> {
    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    @Override
    public void valueChanged(Getter<?> getter) {
        counter++;
    }
}
