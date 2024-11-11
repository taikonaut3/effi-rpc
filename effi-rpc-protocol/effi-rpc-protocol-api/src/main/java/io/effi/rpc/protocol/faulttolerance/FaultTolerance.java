package io.effi.rpc.protocol.faulttolerance;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.extension.spi.Extensible;
import io.effi.rpc.protocol.support.CompletableReplyFuture;

import static io.effi.rpc.common.constant.Component.FaultTolerance.FAIL_FAST;

/**
 * Handles failures during RPC operations.
 */
@Extensible(FAIL_FAST)
public interface FaultTolerance {

    /**
     * Executes an operation using the provided {@link CompletableReplyFuture}.
     *
     * @param future the {@link CompletableReplyFuture} representing the asynchronous
     *               operation to be executed
     * @throws RpcException if an error occurs during execution
     */
    void operation(CompletableReplyFuture future,Throwable e) throws RpcException;
}




