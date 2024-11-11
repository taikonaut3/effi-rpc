package io.effi.rpc.protocol.faulttolerance;

import io.effi.rpc.common.constant.Component;
import io.effi.rpc.common.constant.Constant;
import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.extension.spi.Extension;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import io.effi.rpc.metrics.CallerMetrics;
import io.effi.rpc.protocol.support.CompletableReplyFuture;

/**
 * Fail-retry implementation of {@link FaultTolerance}.
 * Attempts to retry an operation a specified number of times before ultimately failing.
 */
@Extension(Component.FaultTolerance.FAIL_RETRY)
public class FailRetry extends AbstractFaultTolerance {

    private static final Logger logger = LoggerFactory.getLogger(FailRetry.class);

    @Override
    public void doOperation(CompletableReplyFuture future, Throwable e) throws RpcException {
        var invocation = future.invocation();
        URL url = invocation.callerUrl();
        int retries = url.getIntParam(KeyConstant.RETRIES, Constant.DEFAULT_RETRIES);
        int errorCount = future.errorCount().get();
        // Retry the operation if the error count is less than or equal to the retries
        if (errorCount <= retries) {
            invocation.invoke();
            logger.error("An exception occurred in the calling service: {}, Start retry: {}", e, e.getMessage(), errorCount);
            CallerMetrics callerMetrics = invocation.invoker().get(CallerMetrics.ATTRIBUTE_KEY);
            callerMetrics.retryCount().increment();
            return;
        }
        throw RpcException.wrap(e);
    }
}

