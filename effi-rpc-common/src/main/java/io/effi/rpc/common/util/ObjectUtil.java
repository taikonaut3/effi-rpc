package io.effi.rpc.common.util;

import java.util.Objects;

/**
 * Utility class for common object operations.
 *
 */
public final class ObjectUtil {

    /**
     * Get the simple class name of the object.
     * <p>if the object is null, return "null object",
     * otherwise return the abbreviation of the object class name.</p>
     *
     * @param o
     * @return
     */
    public static String simpleClassName(Object o) {
        if (o == null) {
            return "null_object";
        } else {
            return simpleClassName(o.getClass());
        }
    }

    /**
     * Get the short name of the class.
     *
     * @param type
     * @return
     */
    public static String simpleClassName(Class<?> type) {
        Objects.requireNonNull(type);
        return type.getSimpleName();
    }

}
