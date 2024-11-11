package io.effi.rpc.core.manager;

import io.effi.rpc.common.extension.resoruce.Cleanable;
import io.effi.rpc.core.PortalSource;

import java.util.Collection;

/**
 * Manage a collection of values of type {@link T} by specified key.
 *
 * @param <T> the type of values managed
 */
public interface Manager<T> extends PortalSource, Cleanable {

    /**
     * Registers a value associated with a specific key.
     *
     * @param key   the key to associate with the value
     * @param value the value to register
     */
    void register(String key, T value);

    /**
     * Removes the value associated with the specified key.
     *
     * @param key the key whose associated value is to be removed
     */
    void remove(String key);

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param key the key whose associated value is to be retrieved
     * @return the value associated with the specified key, or {@link null} if no value is associated
     */
    T get(String key);

    /**
     * Returns a collection of all values managed by this {@link Manager}.
     *
     * @return a collection of values
     */
    Collection<T> values();

    /**
     * Registers a value if it implements the {@link Key} interface.
     * The key is derived from the value itself, allowing for implicit key registration.
     *
     * @param value the value to register, which must be an instance of {@link Key}
     */
    default void register(T value) {
        if (value instanceof Manager.Key managerValue) {
            register(managerValue.managerKey(), value);
        }
    }

    /**
     * Key that can be associated with a value in the {@link Manager}.
     * Implementations of this interface should provide a method to return the associated key as a string.
     */
    interface Key {
        /**
         * Returns the key associated with the implementing instance.
         *
         * @return the key as a string
         */
        String managerKey();
    }
}

