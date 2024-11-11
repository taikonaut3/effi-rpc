package io.effi.rpc.common.extension.spi;

/**
 * For creating new instances of a specified type.
 * <p>
 * This factory provides a way to abstract the instantiation logic for various types of objects,
 * allowing different implementations of this interface to manage object creation in different contexts.
 * For example, an implementation could create instances via reflection, dependency injection frameworks like Spring,
 * or other mechanisms.
 */
public interface ExtensionFactory {

    /**
     * Creates a new instance of the specified type.
     * <p>
     * Implementations of this method should provide the necessary logic to create and return a new instance
     * of the given class type. The creation mechanism may vary depending on the implementation.
     * If the type cannot be instantiated (e.g., due to a lack of a default constructor or some other reason),
     * the implementation may return {@link null} or throw an exception.
     *
     * @param type the type of the extension.
     * @param <T>  The type of the object to be created.
     * @return A new instance of the specified type, or {@link null} if the instance cannot be created.
     */
    <T> T getInstance(Class<T> type);
}

