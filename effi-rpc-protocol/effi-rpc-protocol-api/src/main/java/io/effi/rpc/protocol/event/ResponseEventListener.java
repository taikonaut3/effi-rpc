package io.effi.rpc.protocol.event;

import io.effi.rpc.common.constant.KeyConstant;
import io.effi.rpc.common.executor.RpcThreadPool;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.ObjectUtil;
import io.effi.rpc.core.ReplyFuture;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.core.support.InvocationSupport;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;

/**
 * Responsible for handling the response support.
 */
public class ResponseEventListener extends EnvelopeEventListener<ResponseEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ResponseEventListener.class);

    public ResponseEventListener() {
        super(RpcThreadPool.defaultCPUExecutor("response-handler"));
    }

    @Override
    protected void handEnvelopeEvent(ResponseEvent event) {
        logger.debug("Received <{}>", ObjectUtil.simpleClassName(event));
        URL requestUrl = event.source().url();
        URL callerUrl = InvocationSupport.acquireCallerUrl(requestUrl);
        ReplyFuture future = ReplyFuture.acquireFuture(callerUrl);
        if (future != null) {
            Result result = event.source().result();
            future.complete(result);
        } else {
            logger.error("ResponseFuture({}) can't exist", callerUrl.getLongParam(KeyConstant.UNIQUE_ID));
        }
    }

    @Override
    protected void jvmShuttingDown(ResponseEvent event) {
        handEnvelopeEvent(event);
    }
}
