package io.effi.rpc.common.extension;

import java.util.function.Supplier;

/**
 * LazyInitializer is a utility class that can to lazy initialization of instances,
 * meaning the instance is only created when it is first requested.
 * This class is thread-safe and ensures that the initialization happens
 * only once, even in a multi-threaded environment.
 *
 * @param <T> the type of the object being lazily initialized
 */
public class LazyInitializer<T> {

    // The lazily initialized instance, marked as volatile to ensure visibility across threads
    protected volatile T instance;

    // Supplier that provides the initialization logic for the instance
    private final Supplier<T> supplier;

    /**
     * Constructor that accepts a Supplier responsible for providing the
     * initialization logic for the instance.
     *
     * @param supplier the Supplier used to create the instance
     */
    public LazyInitializer(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Returns the lazily initialized instance. If the instance is null and
     * canNull is true, null is returned without initialization. Otherwise,
     * the instance is initialized using the Supplier if it's not already set.
     *
     * @param canNull flag indicating whether to return null if the instance is not initialized
     * @return the lazily initialized instance, or null if canNull is true and the instance is uninitialized
     */
    public T get(boolean canNull) {
        if (instance == null) {
            // If allowed, return null without initializing
            if (canNull) return instance;
            synchronized (this) {
                if (instance == null) {
                    instance = supplier.get();
                }
            }
        }
        return instance;
    }

    /**
     * Returns the lazily initialized instance. If the instance is not initialized,
     * it will be created using the Supplier. This method allows returning null if the instance is not initialized.
     *
     * @return the lazily initialized instance, or null if the instance is not yet created
     */
    public T get() {
        return get(true);
    }
}

