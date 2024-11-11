package io.effi.rpc.common.extension;

/**
 * Provide a fluent API for creating and configuring objects while enabling method chaining.
 *
 * @param <T> the type of objects being built
 * @param <C> the type of the implementing class that extends this interface,
 *            allowing for method chaining
 */
public interface ChainBuilder<T, C extends ChainBuilder<T, C>> extends Builder<T> {

    /**
     * Returns the current instance of the implementing class, allowing for
     * method chaining in a fluent style.
     *
     * @return the current instance of type C, cast from this instance.
     */
    @SuppressWarnings("unchecked")
    default C returnChain() {
        return (C) this;
    }
}

