package io.effi.rpc.core;

import java.net.InetSocketAddress;

/**
 * Locate target addresses based on a given invocation context.
 */
public interface Locator {

    /**
     * Locates the target URL based on the provided invocation context.
     *
     * @param invocation the {@link CallInvocation} instance representing the
     *                   context of the method call.
     * @return a {@link InetSocketAddress} representing the target address for the invocation,
     *         or null if no suitable address could be found.
     */
    InetSocketAddress locate(CallInvocation<?> invocation);
}

