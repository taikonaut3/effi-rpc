package io.effi.rpc.common.extension;

import io.effi.rpc.common.util.CollectionUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Object that has an order value for determining its precedence
 * in a sequence, typically used in contexts such as filters or handlers.
 * <p>
 * Objects with lower order values have higher priority. Consequently,
 * an object with the highest precedence will have the lowest order value,
 * If multiple objects have the same order value, their relative order is
 * considered arbitrary.
 */
public interface Ordered {

    /**
     * Constant representing the highest precedence value.
     * <p>
     * This value can be used to assign the highest priority to an object,
     * ensuring it is executed before others with higher order values.
     *
     * @see java.lang.Integer#MIN_VALUE
     */
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * Constant representing the lowest precedence value.
     * <p>
     * This value can be used to assign the lowest priority to an object,
     * ensuring it is executed after others with lower order values.
     *
     * @see java.lang.Integer#MAX_VALUE
     */
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * Returns the order value of this object.
     * <p>
     * A higher return value indicates a lower priority for execution.
     * Objects with the lowest return values are prioritized highest in
     * any ordering mechanism.
     *
     * @return the order value, with default being 0 if not specified.
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    default int getOrder() {
        return 0;
    }

    /**
     * Sorts a list of objects that implement the {@link Ordered} interface
     * based on their order values. The method returns a new list containing
     * the elements in ascending order according to their order values.
     *
     * @param <T>    the type of objects in the list, which must extend {@link Ordered}
     * @param values a list of {@link Ordered} objects to be sorted
     * @return a new list containing the sorted objects; if the input list is
     * empty or null, returns the original list.
     */
    static <T extends Ordered> List<T> order(List<T> values) {
        if (CollectionUtil.isEmpty(values)) {
            return values;
        }
        return values.stream()
                .sorted(Comparator.comparing(Ordered::getOrder))
                .collect(Collectors.toList());
    }

}
