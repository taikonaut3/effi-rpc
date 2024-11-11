package io.effi.rpc.common.extension;

/**
 * A generic interface for building objects of type.
 *
 * <p>This interface is typically implemented by classes that
 * follow the Builder design pattern, allowing for a step-by-step
 * construction of objects.</p>
 *
 * @param <T> the type of object that this builder constructs
 */
public interface Builder<T> {

    /**
     * Build and return an object of type.
     *
     * @return
     */
    T build();

}
