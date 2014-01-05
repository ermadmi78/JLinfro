package com.github.linfro.core.value;

import com.github.linfro.core.common.RevertFunction;

import static com.github.linfro.core.common.ObjectUtil.nvl;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public final class ValueUtil {
    public static final RevertFunction<Boolean, Boolean> NOT_FUNCTION = new RevertFunction<Boolean, Boolean>() {
        @Override
        public Boolean apply(Boolean from) {
            return !nvl(from);
        }

        @Override
        public Boolean revert(Boolean to) {
            return !nvl(to);
        }
    };

    public static final Aggregator<Boolean> AGGREGATOR_AND = new Aggregator<Boolean>() {
        @Override
        public Boolean aggregate(Iterable<Boolean> members) {
            int counter = 0;
            if (members != null) {
                for (Boolean val : members) {
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
        public Boolean aggregate(Iterable<Boolean> members) {
            if (members != null) {
                for (Boolean val : members) {
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
