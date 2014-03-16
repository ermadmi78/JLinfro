package com.github.linfro.core.common;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-04
 * @since 1.0.0
 */
public final class ObjectUtil {
    public static final float DEFAULT_FLOAT_TOLERANCE = 1e-7F;
    public static final double DEFAULT_DOUBLE_TOLERANCE = 1e-7D;

    public static final EqualityNew<Object> DEFAULT_NEW_EQUALITY = ObjectUtil::eq;

    public static final EqualityNew<Object> IDENTITY_NEW_EQUALITY = (first, second) -> first == second;

    public static final Copier DEFAULT_COPIER = new Copier() {
        @Override
        public Object copy(Object obj) {
            if (obj == null) {
                return null;
            }

            if (!(obj instanceof Serializable)) {
                throw new IllegalArgumentException("Cannot copy not serializable object");
            }

            return deepCopy((Serializable) obj);
        }
    };

    private ObjectUtil() {
    }

    public static <T> T notNull(T value) {
        return notNull(value, "Argument must not be null");
    }

    public static <T> T notNull(T value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

    public static boolean eq(Object obj1, Object obj2) {
        if ((obj1 instanceof Number) && (obj2 instanceof Number)) {
            if ((obj1 instanceof Double) && (obj2 instanceof Double)) {
                return compareDouble((Double) obj1, (Double) obj2) == 0;
            }

            if ((obj1 instanceof Float) && (obj2 instanceof Float)) {
                return compareFloat((Float) obj1, (Float) obj2) == 0;
            }

            if ((obj1 instanceof BigDecimal) && (obj2 instanceof BigDecimal)) {
                return ((BigDecimal) obj1).compareTo((BigDecimal) obj2) == 0;
            }
        }

        return eqClassic(obj1, obj2);
    }

    public static boolean eqClassic(Object obj1, Object obj2) {
        return obj1 == null ? (obj2 == null) : obj1.equals(obj2);
    }

    public static String nvl(String value) {
        return nvl(value, "");
    }

    public static Boolean nvl(Boolean value) {
        return nvl(value, Boolean.FALSE);
    }

    public static Character nvl(Character value) {
        return nvl(value, '\u0000');
    }

    public static Byte nvl(Byte value) {
        return nvl(value, (byte) 0);
    }

    public static Short nvl(Short value) {
        return nvl(value, (short) 0);
    }

    public static Integer nvl(Integer value) {
        return nvl(value, 0);
    }

    public static Long nvl(Long value) {
        return nvl(value, 0L);
    }

    public static Float nvl(Float value) {
        return nvl(value, 0F);
    }

    public static Double nvl(Double value) {
        return nvl(value, 0D);
    }

    public static BigInteger nvl(BigInteger value) {
        return nvl(value, BigInteger.ZERO);
    }

    public static BigDecimal nvl(BigDecimal value) {
        return nvl(value, BigDecimal.ZERO);
    }

    public static <T> T nvl(T value, T nullValue) {
        return value == null ? nullValue : value;
    }

    public static byte[] objectToBytes(Serializable inData) throws IOException {
        if (inData == null) {
            throw new IllegalArgumentException("inData must not be null");
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(inData);
            return byteArrayOutputStream.toByteArray();
        }
    }

    public static Object bytesToObject(byte[] data) throws IOException, ClassNotFoundException {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return objectInputStream.readObject();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deepCopy(T obj) {
        if (obj == null) {
            return null;
        }

        try {
            byte[] bytes = objectToBytes(obj);
            return (T) bytesToObject(bytes);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    //***************************** Comparator support *****************************************************************

    private static final Integer EQUALS = 0;
    private static final Integer MINUS = -1;
    private static final Integer PLUS = 1;

    public static final Comparator<Object> OBJECTS_BY_STRING_COMPARATOR = new ObjectsByStringComparator();

    public static class ObjectsByStringComparator implements Comparator<Object>, Serializable {
        @Override
        public int compare(Object o1, Object o2) {
            return compareObjectByString(o1, o2);
        }
    }

    public static final Comparator<Comparable> UNSAFE_COMPARATOR = new UnsafeComparator();

    public static class UnsafeComparator implements Comparator<Comparable>, Serializable {
        @Override
        @SuppressWarnings("unchecked")
        public int compare(Comparable o1, Comparable o2) {
            Integer nullRes = compareByNull(o1, o2);
            if (nullRes != null) {
                return nullRes;
            }
            return o1.compareTo(o2);
        }
    }

    public static final Comparator<String> STRINGS_IGNORE_CASE_COMPARATOR = new StringsIgnoreCaseComparator();

    public static class StringsIgnoreCaseComparator implements Comparator<String>, Serializable {
        @Override
        public int compare(String o1, String o2) {
            return compareStringIgnoreCase(o1, o2);
        }
    }

    public static int compareChar(char first, char second) {
        return first - second;
    }

    public static int compareByte(byte first, byte second) {
        return first - second;
    }

    public static int compareShort(short first, short second) {
        return first - second;
    }

    public static int compareInt(int first, int second) {
        return first < second ? -1 : (first == second ? 0 : 1);
    }

    public static int compareLong(long first, long second) {
        return first < second ? -1 : (first == second ? 0 : 1);
    }

    public static int compareFloat(float first, float second) {
        return compareFloat(first, second, DEFAULT_FLOAT_TOLERANCE);
    }

    public static int compareFloat(float a, float b, float tolerance) {
        if (Float.isNaN(tolerance) || Float.isInfinite(tolerance)) {
            throw new IllegalArgumentException("tolerance must not be NaN or Infinite");
        }

        if ((a == Float.NEGATIVE_INFINITY) || (b == Float.NEGATIVE_INFINITY)) {
            if ((a == Float.NEGATIVE_INFINITY) && (b == Float.NEGATIVE_INFINITY)) {
                return 0;
            } else if (a == Float.NEGATIVE_INFINITY) {
                return -1;
            } else {
                return 1;
            }
        }

        if (Float.isNaN(a) || Float.isNaN(b)) {
            if (Float.isNaN(a) && Float.isNaN(b)) {
                return 0;
            } else if (Float.isNaN(a)) {
                return -1;
            } else {
                return 1;
            }
        }

        if ((a == Float.POSITIVE_INFINITY) || (b == Float.POSITIVE_INFINITY)) {
            if ((a == Float.POSITIVE_INFINITY) && (b == Float.POSITIVE_INFINITY)) {
                return 0;
            } else if (a == Float.POSITIVE_INFINITY) {
                return 1;
            } else {
                return -1;
            }
        }

        float diff = a - b;
        return diff >= tolerance ? 1 : diff <= -tolerance ? -1 : 0;
    }

    public static int compareDouble(double first, double second) {
        return compareDouble(first, second, DEFAULT_DOUBLE_TOLERANCE);
    }

    public static int compareDouble(double a, double b, double tolerance) {
        if (Double.isNaN(tolerance) || Double.isInfinite(tolerance)) {
            throw new IllegalArgumentException("tolerance must not be NaN or Infinite");
        }

        if ((a == Double.NEGATIVE_INFINITY) || (b == Double.NEGATIVE_INFINITY)) {
            if ((a == Double.NEGATIVE_INFINITY) && (b == Double.NEGATIVE_INFINITY)) {
                return 0;
            } else if (a == Double.NEGATIVE_INFINITY) {
                return -1;
            } else {
                return 1;
            }
        }

        if (Double.isNaN(a) || Double.isNaN(b)) {
            if (Double.isNaN(a) && Double.isNaN(b)) {
                return 0;
            } else if (Double.isNaN(a)) {
                return -1;
            } else {
                return 1;
            }
        }

        if ((a == Double.POSITIVE_INFINITY) || (b == Double.POSITIVE_INFINITY)) {
            if ((a == Double.POSITIVE_INFINITY) && (b == Double.POSITIVE_INFINITY)) {
                return 0;
            } else if (a == Double.POSITIVE_INFINITY) {
                return 1;
            } else {
                return -1;
            }
        }

        double diff = a - b;
        return diff >= tolerance ? 1 : diff <= -tolerance ? -1 : 0;
    }

    public static int compareBoolean(boolean first, boolean second) {
        return first == second ? 0 : (first ? 1 : -1);
    }

    public static int compareObjectByString(Object first, Object second) {
        return compareObj((first == null ? null : first.toString()), (second == null ? null : second.toString()));
    }

    public static int compareObjectByStringIgnoreCase(Object first, Object second) {
        return compareStringIgnoreCase((first == null ? null : first.toString()), (second == null ? null : second.toString()));
    }

    public static int compareStringIgnoreCase(String first, String second) {
        Integer nullRes = compareByNull(first, second);
        if (nullRes != null) {
            return nullRes;
        }
        return first.compareToIgnoreCase(second);
    }

    public static <T extends Comparable<T>> int compareObj(T first, T second) {
        Integer nullRes = compareByNull(first, second);
        if (nullRes != null) {
            return nullRes;
        }
        return first.compareTo(second);
    }

    public static Integer compareByNull(Object first, Object second) {
        if ((first == null) && (second == null)) {
            return EQUALS;
        }
        if (first == null) {
            return MINUS;
        }
        if (second == null) {
            return PLUS;
        }

        return null;
    }
}
