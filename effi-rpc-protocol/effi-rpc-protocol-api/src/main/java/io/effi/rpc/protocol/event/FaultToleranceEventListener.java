package io.effi.rpc.protocol.event;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.Caller;
import io.effi.rpc.event.EventListener;
import io.effi.rpc.protocol.faulttolerance.FaultTolerance;
import io.effi.rpc.protocol.support.CompletableReplyFuture;

/**
 * FaultTolerance EventListener.
 */
public class FaultToleranceEventListener implements EventListener<FaultToleranceEvent> {
    @Override
    public void onEvent(FaultToleranceEvent event) {
        CompletableReplyFuture future = event.source();
        CallInvocation<?> invocation = future.invocation();
        FaultTolerance faultTolerance = ExtensionLoader.loadExtension(FaultTolerance.class, invocation.callerUrl());
        Caller<?> caller = invocation.caller();
        try {
            faultTolerance.operation(future, event.exception());
        } catch (RpcException e) {
            future.completableFuture().completeExceptionally(e);
        }
    }
}
