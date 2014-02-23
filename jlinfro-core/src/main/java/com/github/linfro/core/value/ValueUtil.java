package com.github.linfro.core.value;

import com.github.linfro.core.Aggregator;

import static com.github.linfro.core.common.ObjectUtil.nvl;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public final class ValueUtil {
    public static final Aggregator<Boolean> AGGREGATOR_AND = new Aggregator<Boolean>() {
        @Override
        public Boolean aggregate(Iterable<Boolean> args) {
            int counter = 0;
            if (args != null) {
                for (Boolean val : args) {
                    counter++;
                    if (!nvl(val)) {
                        return Boolean.FALSE;
                    }
                }
            }

            return counter == 0 ? Boolean.FALSE : Boolean.TRUE;
        }
    };

    public static final Aggregator<Boolean> AGGREGATOR_OR = new Aggregator<Boolean>() {
        @Override
        public Boolean aggregate(Iterable<Boolean> args) {
            if (args != null) {
                for (Boolean val : args) {
                    if (nvl(val)) {
                        return Boolean.TRUE;
                    }
                }
            }

            return Boolean.FALSE;
        }
    };

    private ValueUtil() {
    }
}
