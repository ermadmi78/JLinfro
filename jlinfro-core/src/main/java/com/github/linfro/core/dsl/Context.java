package com.github.linfro.core.dsl;

import com.github.linfro.core.common.Equality;

import static com.github.linfro.core.common.ObjectUtil.DEFAULT_EQUALITY;
import static com.github.linfro.core.common.ObjectUtil.nvl;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-06
 * @since 1.0.0
 */
public class Context {
    private boolean sync = false;
    private boolean strong = false;
    private Equality equality = DEFAULT_EQUALITY;
    private boolean force = false;

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public boolean isStrong() {
        return strong;
    }

    public void setStrong(boolean strong) {
        this.strong = strong;
    }

    public Equality getEquality() {
        return equality;
    }

    public void setEquality(Equality equality) {
        this.equality = nvl(equality, DEFAULT_EQUALITY);
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }
}
