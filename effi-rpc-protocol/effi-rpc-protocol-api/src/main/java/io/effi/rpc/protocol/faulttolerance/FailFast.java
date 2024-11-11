package io.effi.rpc.protocol.faulttolerance;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.protocol.support.CompletableReplyFuture;

import static io.effi.rpc.common.constant.Component.FaultTolerance.FAIL_FAST;

/**
 * Fail-fast implementation of {@link FaultTolerance}.Throw an exception when an operation
 * fails, rather than allowing the operation to continue.
 */
@Extension(FAIL_FAST)
public class FailFast extends AbstractFaultTolerance {

    @Override
    protected void doOperation(CompletableReplyFuture future, Throwable e) throws RpcException {
        throw RpcException.wrap(e);
    }
}

