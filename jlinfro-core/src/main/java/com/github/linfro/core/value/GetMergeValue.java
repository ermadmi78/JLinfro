package com.github.linfro.core.value;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.Getter;
import com.github.linfro.core.ValueChangeListener;
import com.github.linfro.core.dsl.GetValueHolder;

import java.util.HashMap;
import java.util.Map;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-06-07
 * @since 1.0.0
 */
public class GetMergeValue extends AbstractGetValue<Map<String, Object>> {
    protected final Map<String, GetValue<?>> args = new HashMap<>();
    protected final ValueChangeListener<Object> argListener = new ValueChangeListener<Object>() {
        @Override
        public void valueChanged(Getter<?> getter) {
            lastGetter = getter;
            fireValueChanged();
        }
    };

    protected Getter<?> lastGetter;
    private Map<String, Object> result;

    public GetMergeValue(GetValueHolder firstArg, GetValueHolder secondArg, GetValueHolder... otherArgs) {
        this.lastGetter = addArg(firstArg, 0);
        addArg(secondArg, 1);
        if (otherArgs != null) {
            for (int i = 0; i < otherArgs.length; i++) {
                addArg(otherArgs[i], i + 2);
            }
        }
    }

    private GetValue<?> addArg(GetValueHolder arg, int index) {
        GetValue<?> value = notNull(notNull(arg).getContentValue());

        String argKey = notNull(getArgKey(value, index));
        if (args.containsKey(argKey)) {
            throw new IllegalArgumentException("Argument key duplicate found: " + argKey);
        }

        args.put(argKey, value);
        value.addChangeListener(argListener);
        return value;
    }

    protected String getArgKey(Getter<?> getter, int index) {
        String name = notNull(getter).getMetaName();
        return name == null ? "arg" + index : name;
    }

    @Override
    public boolean isValueValid() {
        if (args.isEmpty()) {
            throw new IllegalStateException("Value is disposed");
        }

        for (GetValue<?> arg : args.values()) {
            if (!arg.isValueValid()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Object getMetaInfo(String key) {
        if (args.isEmpty()) {
            throw new IllegalStateException("Value is disposed");
        }

        return lastGetter == null ? null : lastGetter.getMetaInfo(key);
    }

    @Override
    public Map<String, Object> getValue() {
        if (args.isEmpty()) {
            throw new IllegalStateException("Value is disposed");
        }

        if (result == null) {
            Map<String, Object> res = new HashMap<>();
            for (Map.Entry<String, GetValue<?>> entry : args.entrySet()) {
                String key = entry.getKey();
                GetValue<?> arg = entry.getValue();

                res.put(key, arg.getValue());
            }

            result = res;
        }

        return result;
    }

    @Override
    public void fireValueChanged() {
        result = null;
        if (!args.isEmpty()) {
            super.fireValueChanged();
        }
    }

    @Override
    public boolean canDispose() {
        return listeners.isEmpty();
    }

    @Override
    public void dispose() {
        listeners.clear();

        for (GetValue<?> arg : args.values()) {
            arg.removeChangeListener(argListener);
            if (arg.canDispose()) {
                arg.dispose();
            }
        }
        args.clear();
        lastGetter = null;
        result = null;
    }
}
