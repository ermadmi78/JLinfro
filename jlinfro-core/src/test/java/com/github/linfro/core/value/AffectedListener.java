package com.github.linfro.core.value;

import com.github.linfro.core.Getter;
import com.github.linfro.core.ValueChangeListener;

/**
 * @author Dmitry Ermakov
 * @version 2014-03-16
 * @since 1.0.0
 */
public class AffectedListener implements ValueChangeListener<Object> {
    private boolean affected = false;

    public boolean testAffectedAndReset() {
        boolean res = affected;
        affected = false;
        return res;
    }

    @Override
    public void valueChanged(Getter<?> getter) {
        affected = true;
    }
}
