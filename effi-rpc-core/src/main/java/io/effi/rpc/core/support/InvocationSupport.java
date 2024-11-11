package io.effi.rpc.core.support;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.url.URLType;
import io.effi.rpc.core.CallInvocation;

import java.net.InetSocketAddress;

/**
 * Utility class providing support functions for handling invocation.
 */
public class InvocationSupport {

    /**
     * Acquires the caller's URL if valid; otherwise, retrieves it from the specified key.
     *
     * @param url the URL to verify and potentially retrieve the caller URL from
     * @return the valid caller URL, or a fallback URL based on the caller key
     */
    public static URL acquireCallerUrl(URL url) {
        return URLType.CALLER.valid(url) ? url : url.get(KeyConstant.CALLER_URL);
    }

    /**
     * Updates the remote address for caller, request, and client URLs associated with the invocation.
     *
     * @param invocation     the invocation containing the URLs to update
     * @param remoteAddress  the remote address to set on each URL
     */
    public static void updateAddress(CallInvocation<?> invocation, InetSocketAddress remoteAddress) {
        URL callerUrl = invocation.callerUrl();
        URL requestUrl = invocation.requestUrl();
        URL clientUrl = invocation.clientUrl();
        callerUrl.address(remoteAddress);
        requestUrl.address(remoteAddress);
        clientUrl.address(remoteAddress);
    }
}

