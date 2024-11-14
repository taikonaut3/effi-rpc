package io.effi.rpc.core;

import io.effi.rpc.common.url.URL;

/**
 * Provides additional methods specific to the context of a rpc.
 * <p>
 * It includes information about the caller and the URLs associated
 * with the caller and client.
 *
 * @param <T>
 * @see Invocation
 */
public interface CallInvocation<T> extends Invocation<T> {

    /**
     * Returns the Caller associated with this invocation.
     *
     * @return a {@link Caller} object representing the entity that initiated
     * the invocation, providing context for the call.
     */
    Caller<?> caller();

    /**
     * Returns the URL of the caller.
     *
     * @return
     */
    URL callerUrl();

    /**
     * Returns the URL of the client making the invocation.
     *
     * @return
     */
    URL clientUrl();

    /**
     * Returns the future associated with this invocation.
     *
     * @return
     */
    ReplyFuture future();
}

