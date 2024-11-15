package io.effi.rpc.common.util;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * Utility class for common collection operations.
 */
@SuppressWarnings("unchecked")
public final class CollectionUtil {

    private CollectionUtil() {
    }

    public static <E> void addUnique(List<E> list, E... elements) {
        Set<E> set = new HashSet<>(list);
        for (E element : elements) {
            if (set.add(element)) {
                list.add(element);
            }
        }
    }

    /**
     * Adds items to a collection based on the provided predicate.
     *
     * @param collection The collection to add items to.
     * @param predicate  The predicate used to determine if an item should be added.
     * @param items      The items to add to the collection.
     * @param <T>        The type of the items in the collection.
     */
    public static <T> void addToList(Collection<T> collection, BiPredicate<T, T> predicate, T... items) {
        addToList(collection, predicate, null, items);
    }

    /**
     * Adds items to a collection based on the provided predicate and invokes a callback on successful addition.
     *
     * @param collection      The collection to add items to.
     * @param predicate       The predicate used to determine if an item should be added.
     * @param successCallBack The callback function to be called on successful addition.
     * @param items           The items to add to the collection.
     * @param <T>             The type of the items in the collection.
     */
    public static <T> void addToList(Collection<T> collection, BiPredicate<T, T> predicate, Consumer<T> successCallBack, T... items) {
        if (items != null && items.length > 0) {
            loop:
            for (T item : items) {
                for (T collect : collection) {
                    if (predicate.test(collect, item)) {
                        continue loop;
                    }
                }
                collection.add(item);
                if (successCallBack != null) {
                    successCallBack.accept(item);
                }
            }
        }
    }

    /**
     * Checks if a collection is empty or null.
     *
     * @param value The collection to check.
     * @return True if the collection is null or empty, false otherwise.
     */
    public static boolean isEmpty(Collection<?> value) {
        return Objects.isNull(value) || value.isEmpty();
    }

    /**
     * Checks if a map is empty or null.
     *
     * @param value The map to check.
     * @return True if the map is null or empty, false otherwise.
     */
    public static boolean isEmpty(Map<?, ?> value) {
        return Objects.isNull(value) || value.isEmpty();
    }

    /**
     * Checks if an array is empty or null.
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(Object[] value) {
        return value == null || value.length == 0;
    }

    /**
     * Checks if a collection is not empty and not null.
     *
     * @param value The collection to check.
     * @return True if the collection is not null and not empty, false otherwise.
     */
    public static boolean isNotEmpty(Collection<?> value) {
        return !isEmpty(value);
    }

    /**
     * Checks if a map is not empty and not null.
     *
     * @param value The map to check.
     * @return True if the map is not null and not empty, false otherwise.
     */
    public static boolean isNotEmpty(Map<?, ?> value) {
        return !isEmpty(value);
    }

    /**
     * Checks if a array is not empty and not null.
     *
     * @param value
     * @return
     */
    public static boolean isNotEmpty(Object[] value) {
        return !isEmpty(value);
    }

}
