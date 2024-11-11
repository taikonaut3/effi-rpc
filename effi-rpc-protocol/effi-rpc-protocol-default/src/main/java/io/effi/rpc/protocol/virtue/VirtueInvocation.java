package io.effi.rpc.protocol.virtue;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.core.Callee;
import io.effi.rpc.core.Caller;
import io.effi.rpc.core.support.DefaultInvocation;

/**
 * Virtue Invocation.
 */
public class VirtueInvocation<T> extends DefaultInvocation<T> {

    public VirtueInvocation(URL clientUrl, Caller<?> caller, Object[] args) {
        super(clientUrl, caller, args);
    }

    public VirtueInvocation(URL requestUrl, Callee<?> callee, Object[] args) {
        super(requestUrl, callee, args);
    }
}
