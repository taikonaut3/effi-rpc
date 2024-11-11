package io.effi.rpc.common.extension.resoruce;

/**
 * Closeable resource.
 * {@link #close()} to close the resource and {@link #isActive()} check its active state.
 * This can be used for any resource that needs to be explicitly closed after usage.
 */
public interface Closeable {

    /**
     * Closes the resource, releasing any underlying resources (such as streams, connections, etc.).
     * After calling this method, the resource should no longer be considered active.
     * Implementations should ensure that calling this method multiple times has no adverse effect.
     */
    void close();

    /**
     * Checks if the resource is still active and not yet closed.
     *
     * @return {@link true} if the resource is active and can be used,
     * {@link false} if the resource has been closed or is no longer available.
     */
    boolean isActive();
}
