package io.effi.rpc.common.extension;

import io.effi.rpc.common.extension.resoruce.Cleanable;

import java.util.function.Supplier;

/**
 * Manage a collection of attributes.
 * <p>Allows for storing, retrieving, and cleaning up attributes
 * associated with specific keys.</p>
 */
public interface Attributes extends Cleanable {

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param key the key of the attribute to retrieve
     * @param <T> the type of the attribute value
     * @return the value associated with the key, or null if it doesn't exist
     */
    <T> T get(AttributeKey<T> key);

    /**
     * Computes a value if the key is absent and stores it in the attributes.
     * If the key is already present, the existing value is returned.
     *
     * @param key     the key of the attribute to compute
     * @param creator a supplier that provides the value if the key is absent
     * @param <T>     the type of the attribute value
     * @return the value associated with the key
     */
    <T> T computeIfAbsent(AttributeKey<T> key, Supplier<T> creator);

    /**
     * Sets the value associated with the specified key.
     *
     * @param key   the key of the attribute to set
     * @param value the value to associate with the key
     * @param <T>   the type of the attribute value
     * @return the value
     */
    <T> T set(AttributeKey<T> key, T value);

    /**
     * Removes the attribute associated with the specified key.
     *
     * @param key the key of the attribute to remove
     * @return the updated attributes collection
     */
    Attributes remove(AttributeKey<?> key);
}

