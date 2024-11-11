package io.effi.rpc.common.extension.resoruce;

/**
 * Cleanable resource.
 * {@link #clear()} to clear or reset the internal state of the object.
 * This can be used for objects that need to release or reset resources without being closed or destroyed.
 */
public interface Cleanable {

    /**
     * Clears the internal state or resources of this object.
     * This method is used to reset or clean up the object without fully closing or destroying it.
     * After calling this method, the object should be in a clean, reusable state.
     * <p>
     * Implementations may define what "clearing" means depending on the resource,
     * such as emptying caches, clearing buffers, or resetting configurations.
     */
    void clear();
}

