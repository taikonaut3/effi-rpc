package io.effi.rpc.protocol.support;

import io.effi.rpc.common.util.AssertUtil;
import io.effi.rpc.common.util.NetUtil;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.Locator;

import java.net.InetSocketAddress;

/**
 * Resolves the remote address directly, returning the predefined {@link InetSocketAddress}.
 */
public class DirectLocator implements Locator {

    private final InetSocketAddress remoteAddress;

    /**
     * Initializes with a specified {@link InetSocketAddress}.
     *
     * @param remoteAddress The remote address to be returned by the {@link #locate(CallInvocation)} method.
     * @throws IllegalArgumentException if the remote address is null.
     */
    public DirectLocator(InetSocketAddress remoteAddress) {
        this.remoteAddress = AssertUtil.notNull(remoteAddress, "remote address cannot be null");
    }

    /**
     * Initializes with a remote address string, converting it to an {@link InetSocketAddress}.
     *
     * @param remoteAddress The remote address as a string (e.g., "127.0.0.1:8080").
     */
    public DirectLocator(String remoteAddress) {
        this.remoteAddress = NetUtil.toInetSocketAddress(remoteAddress);
    }

    /**
     * Returns the predefined remote address.
     *
     * @param invocation The invocation details, not used in this case.
     * @return The {@link InetSocketAddress} that was passed to the constructor.
     */
    @Override
    public InetSocketAddress locate(CallInvocation<?> invocation) {
        return remoteAddress;
    }
}

