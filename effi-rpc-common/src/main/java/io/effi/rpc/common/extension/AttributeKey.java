package io.effi.rpc.common.extension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Key for storing and retrieving attributes in a context.
 * The key is used to associate a value of a specific type with a unique name,
 * allowing for type-safe retrieval of attributes from a map.
 *
 * @param <T> the type of the attribute value associated with this key
 */
public final class AttributeKey<T> {

    // A thread-safe map that holds the unique keys.
    private static final Map<CharSequence, AttributeKey<?>> KEY_POOL = new ConcurrentHashMap<>();

    // The name of the attribute key.
    private final CharSequence name;

    // Private constructor to prevent direct instantiation.
    private AttributeKey(CharSequence name) {
        this.name = name;
    }

    /**
     * Creates or retrieves an existing {@link AttributeKey} instance associated with the given name.
     * This method ensures that there is only one instance of an {@link AttributeKey} for each unique name,
     * which helps in managing keys efficiently.
     *
     * @param name the name of the attribute key
     * @param <T>  the type of the attribute value associated with this key
     * @return an {@link AttributeKey} for the specified name
     */
    @SuppressWarnings("unchecked")
    public static <T> AttributeKey<T> valueOf(CharSequence name) {
        return (AttributeKey<T>) KEY_POOL.computeIfAbsent(name, k -> new AttributeKey<T>(name));
    }

    /**
     * Returns the name of this attribute key.
     *
     * @return the name of the attribute key as a {@link CharSequence}
     */
    public CharSequence name() {
        return name;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
