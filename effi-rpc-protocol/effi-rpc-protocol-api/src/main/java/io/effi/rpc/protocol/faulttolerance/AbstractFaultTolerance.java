package io.effi.rpc.protocol.faulttolerance;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.protocol.support.CompletableReplyFuture;

/**
 * Abstract implementation of {@link FaultTolerance}.
 */
public abstract class AbstractFaultTolerance implements FaultTolerance {

    @Override
    public void operation(CompletableReplyFuture future,Throwable e) throws RpcException {
        if (!future.completableFuture().isDone()) {
            doOperation(future,e);
        }
    }

    protected abstract void doOperation(CompletableReplyFuture future,Throwable e) throws RpcException;

}
