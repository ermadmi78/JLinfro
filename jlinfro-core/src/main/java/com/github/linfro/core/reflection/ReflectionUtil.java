package com.github.linfro.core.reflection;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static com.github.linfro.core.common.ObjectUtil.notNull;
import static com.github.linfro.core.common.ObjectUtil.nvl;

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

    public static boolean isUseDynamicInvoker() {
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

    public static SimplePropertyInvoker createSimplePropertyInvoker(Class<?> beanClass, String propertyName, InvokerFactory factory) {
        notNull(beanClass);
        notNull(propertyName);
        notNull(factory);

        if (!isValidPropertyName(propertyName)) {
            InvalidInvoker invoker = new InvalidInvoker("Invalid property name: " + propertyName);
            return new SimplePropertyInvoker(beanClass, propertyName, void.class, invoker, invoker);
        }

        Field field = null;
        try {
            field = beanClass.getField(propertyName);
        } catch (NoSuchFieldException e) {
            // Do nothing
        }

        final String camelizedName = camelize(propertyName);

        Method getter = null;
        try {
            getter = beanClass.getMethod("get" + camelizedName);
        } catch (NoSuchMethodException e) {
            // Do nothing
        }

        if (getter == null) {
            try {
                getter = beanClass.getMethod("is" + camelizedName);
            } catch (NoSuchMethodException e) {
                // Do nothing
            }
        }

        if (getter != null) {
            Class<?> ret = getter.getReturnType();
            if ((ret == null) || (ret == void.class) || ((field != null) && (ret != field.getType()))) {
                getter = null;
            }
        }

        if ((field == null) && (getter == null)) {
            InvalidInvoker invoker = new InvalidInvoker("Cannot find property '" + propertyName +
                    "' for bean: " + beanClass.getName());
            return new SimplePropertyInvoker(beanClass, propertyName, void.class, invoker, invoker);
        }

        final Class<?> type = getter != null ? getter.getReturnType() : field.getType();

        Method setter = null;
        try {
            setter = beanClass.getMethod("set" + camelizedName, type);
        } catch (NoSuchMethodException e) {
            // Do nothing
        }

        if (setter != null) {
            Class<?> ret = setter.getReturnType();
            if ((ret != null) && (ret != void.class)) {
                setter = null;
            }
        }

        final Invoker getterInvoker = notNull(
                getter != null ? factory.createMethodInvoker(getter) : factory.createGetterInvoker(field)
        );

        final Invoker setterInvoker = notNull(
                setter != null ? factory.createMethodInvoker(setter) : (
                        field != null ? factory.createSetterInvoker(field) :
                                new InvalidInvoker("Property '" + propertyName +
                                        "' is read only for bean: " + beanClass.getName())
                )

        );

        return new SimplePropertyInvoker(beanClass, propertyName, type, getterInvoker, setterInvoker);
    }

    public static void setAccessibleIfNeed(Field field) {
        notNull(field);
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
        }
    }

    public static void setAccessibleIfNeed(Method method) {
        notNull(method);
        if (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
        }
    }

    //******************************************************************************************************************

    public static final Set<String> RESERVED_KEYWORDS;

    static {
        Set<String> set = new HashSet<>();
        set.add("abstract");
        set.add("assert");
        set.add("boolean");
        set.add("break");
        set.add("byte");
        set.add("case");
        set.add("catch");
        set.add("char");
        set.add("class");
        set.add("const");
        set.add("continue");
        set.add("default");
        set.add("do");
        set.add("double");
        set.add("else");
        set.add("enum");
        set.add("extends");
        set.add("final");
        set.add("finally");
        set.add("float");
        set.add("for");
        set.add("goto");
        set.add("if");
        set.add("implements");
        set.add("import");
        set.add("instanceof");
        set.add("int");
        set.add("interface");
        set.add("long");
        set.add("native");
        set.add("new");
        set.add("package");
        set.add("private");
        set.add("protected");
        set.add("public");
        set.add("return");
        set.add("short");
        set.add("static");
        set.add("strictfp");
        set.add("super");
        set.add("switch");
        set.add("synchronized");
        set.add("this");
        set.add("throw");
        set.add("throws");
        set.add("transient");
        set.add("try");
        set.add("void");
        set.add("volatile");
        set.add("while");

        RESERVED_KEYWORDS = Collections.unmodifiableSet(set);
    }

    public static boolean isValidPropertyName(String s) {
        if ((s == null) || s.isEmpty()) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (i == 0) {
                if (!Character.isJavaIdentifierStart(c)) {
                    return false;
                }
            } else {
                if (!Character.isJavaIdentifierPart(c)) {
                    return false;
                }
            }
        }

        return !RESERVED_KEYWORDS.contains(s);
    }

    public static String camelize(String s) {
        if ((s == null) || s.isEmpty()) {
            return s;
        }

        if (s.length() == 1) {
            return s.toUpperCase();
        }

        if (Character.isUpperCase(s.charAt(1))) {
            // retain first letter in lower case (e.g. xRate will remain as xRate and corresponding getter is getxRate)
            return s;
        }

        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String[] splitProperty(String s) throws IllegalArgumentException {
        s = nvl(s).trim();

        if (s.isEmpty()) {
            throw new IllegalArgumentException("Invalid property name: property name must not be empty");
        }

        if ((s.charAt(0) == '.') || (s.charAt(s.length() - 1) == '.')) {
            throw new IllegalArgumentException("Invalid property name: " + s);
        }

        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.') {
                count++;
            }
        }

        String[] res = new String[count + 1];
        if (res.length == 1) {
            res[0] = s; // Small optimization
        } else {
            int index = 0;
            int lastPos = -1;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '.') {
                    res[index++] = s.substring(lastPos + 1, i);
                    lastPos = i;
                }
            }
            res[index] = s.substring(lastPos + 1);
        }

        for (String name : res) {
            if (!isValidPropertyName(name)) {
                throw new IllegalArgumentException(
                        "Invalid property name: " + s + (
                                ((res.length == 1) || nvl(name).isEmpty()) ? "" : (" (" + name + ")")
                        )
                );
            }
        }

        return res;
    }
}
