package io.effi.rpc.common.extension;

/**
 * Provide the ability to create an exact deep copy of an object.
 *
 * @param <T> the type of object that can be replicated
 */
public interface Replicable<T> {

    /**
     * Creates and returns a deep copy of the object.
     * This ensures that all internal mutable state is fully duplicated.
     *
     * @return a deep copy of the object
     */
    T replicate();
}
