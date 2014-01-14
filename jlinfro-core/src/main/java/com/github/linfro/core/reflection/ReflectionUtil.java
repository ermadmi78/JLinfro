package com.github.linfro.core.reflection;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-12
 * @since 1.0.0
 */
public final class ReflectionUtil {
    private ReflectionUtil() {
    }

    private static volatile InvokerFactory invokerFactory;

    public static final String KEY_USE_DYNAMIC_INVOKER = "JLinfro.UseDynamicInvoker";

    public static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAP_MAP;
    public static final Map<Class<?>, Class<?>> WRAP_TO_PRIMITIVE_MAP;

    private static final Map<Class<?>, Object> DEFAULT_VALUES_OF_PRIMITIVES;

    static {
        Map<Class<?>, Class<?>> map = new HashMap<>();
        map.put(Boolean.TYPE, Boolean.class);
        map.put(Character.TYPE, Character.class);
        map.put(Byte.TYPE, Byte.class);
        map.put(Short.TYPE, Short.class);
        map.put(Integer.TYPE, Integer.class);
        map.put(Long.TYPE, Long.class);
        map.put(Float.TYPE, Float.class);
        map.put(Double.TYPE, Double.class);
        PRIMITIVE_TO_WRAP_MAP = Collections.unmodifiableMap(map);

        map = new HashMap<>();
        map.put(Boolean.class, Boolean.TYPE);
        map.put(Character.class, Character.TYPE);
        map.put(Byte.class, Byte.TYPE);
        map.put(Short.class, Short.TYPE);
        map.put(Integer.class, Integer.TYPE);
        map.put(Long.class, Long.TYPE);
        map.put(Float.class, Float.TYPE);
        map.put(Double.class, Double.TYPE);
        WRAP_TO_PRIMITIVE_MAP = Collections.unmodifiableMap(map);

        Map<Class<?>, Object> defMap = new HashMap<>();
        defMap.put(Boolean.TYPE, false);
        defMap.put(Character.TYPE, '\u0000');
        defMap.put(Byte.TYPE, (byte) 0);
        defMap.put(Short.TYPE, (short) 0);
        defMap.put(Integer.TYPE, 0);
        defMap.put(Long.TYPE, 0L);
        defMap.put(Float.TYPE, 0F);
        defMap.put(Double.TYPE, 0D);
        DEFAULT_VALUES_OF_PRIMITIVES = Collections.synchronizedMap(defMap);

        invokerFactory = isUseDynamicInvoker() ? new DynamicInvokerFactory() : new ReflectiveInvokerFactory();
    }

    private static boolean isUseDynamicInvoker() {
        return "true".equals(System.getProperty(KEY_USE_DYNAMIC_INVOKER, "false"));
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return PRIMITIVE_TO_WRAP_MAP.containsKey(notNull(clazz));
    }

    public static Class<?> primitiveToWrap(Class<?> primitive) {
        Class<?> wrap = PRIMITIVE_TO_WRAP_MAP.get(notNull(primitive, "Argument 'primitive' must not be null"));
        return notNull(wrap, "Argument 'primitive' must be primitive java type");
    }

    public static boolean isWrap(Class<?> clazz) {
        return WRAP_TO_PRIMITIVE_MAP.containsKey(notNull(clazz));
    }

    public static Class<?> wrapToPrimitive(Class<?> wrap) {
        Class<?> primitive = WRAP_TO_PRIMITIVE_MAP.get(notNull(wrap, "Argument 'wrap' must not be null"));
        return notNull(primitive, "Argument 'wrap' must be wrapper of primitive java type");
    }

    public static Object getDefaultPrimitiveValue(Class<?> primitive) {
        notNull(primitive, "Argument 'primitive' must not be null");
        return notNull(DEFAULT_VALUES_OF_PRIMITIVES.get(primitive), "Argument 'primitive' must be primitive java type");
    }

    public static <T> void setDefaultPrimitiveValue(Class<T> primitive, T value) {
        notNull(primitive, "Argument 'primitive' must not be null");
        notNull(value, "Argument 'value' must not be null");
        if (!isPrimitive(primitive)) {
            throw new IllegalArgumentException("Argument 'primitive' must be primitive java type");
        }
        DEFAULT_VALUES_OF_PRIMITIVES.put(primitive, value);
    }

    public static InvokerFactory getInvokerFactory() {
        return invokerFactory;
    }

    public static void setInvokerFactory(InvokerFactory invokerFactory) {
        ReflectionUtil.invokerFactory = notNull(invokerFactory);
    }
}
