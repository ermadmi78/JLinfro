package com.github.linfro.core.dsl;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.common.Equality;

import java.util.LinkedList;
import java.util.List;

import static com.github.linfro.core.common.ObjectUtil.DEFAULT_EQUALITY;
import static com.github.linfro.core.common.ObjectUtil.nvl;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-06
 * @since 1.0.0
 */
public class Context implements Disposable {
    private boolean sync = false;
    private boolean strong = false;
    private Equality equality = DEFAULT_EQUALITY;
    private boolean force = false;

    private final List<Disposable> toDisposeList = new LinkedList<>();

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

    public void addToDispose(Object obj) {
        if (obj instanceof Disposable) {
            toDisposeList.add((Disposable) obj);
        }
    }

    @Override
    public void dispose() {
        if (toDisposeList.isEmpty()) {
            return;
        }

        for (Disposable disposable : toDisposeList) {
            disposable.dispose();
        }
        toDisposeList.clear();
    }
}
