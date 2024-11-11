package io.effi.rpc.core;

import io.effi.rpc.common.url.URL;

/**
 * Provide methods specific to the context of a rpc.
 * <p>
 * It includes details about the callee and the server's
 * URL handling the invocation.
 *
 * @param <T>
 * @see Invocation
 */
public interface ReceiveInvocation<T> extends Invocation<T> {

    /**
     * Returns the Callee associated with this invocation.
     *
     * @return a {@link Callee} object representing the entity that is
     * being called in the invocation, providing context for the call.
     */
    Callee<?> callee();

    /**
     * Returns the URL of the server handling this invocation.
     *
     * @return a {@link URL} representing the address of the server that
     * is processing the method call, allowing for network routing
     * and communication.
     */
    URL serverUrl();
}

