package io.effi.rpc.protocol.event;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.executor.RpcThreadPool;
import io.effi.rpc.common.extension.RpcContext;
import io.effi.rpc.common.extension.spi.ExtensionLoader;
import io.effi.rpc.common.url.URL;
import io.effi.rpc.common.util.ObjectUtil;
import io.effi.rpc.core.ReceiveInvocation;
import io.effi.rpc.core.result.Result;
import io.effi.rpc.internal.logging.Logger;
import io.effi.rpc.internal.logging.LoggerFactory;
import io.effi.rpc.transport.Protocol;
import io.effi.rpc.transport.Request;

/**
 * Receive request support and reflect the service.
 */
public class RequestEventListener extends EnvelopeEventListener<RequestEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RequestEventListener.class);

    public RequestEventListener() {
        super(RpcThreadPool.defaultIOExecutor("request-handler"));
    }

    @Override
    protected void handEnvelopeEvent(RequestEvent event) {
        logger.debug("Received <{}>", ObjectUtil.simpleClassName(event));
        Request request = event.source();
        URL url = request.url();
        RpcContext.currentContext().set(Request.ATTRIBUTE_KEY, request);
        RpcContext.RequestContext.parse(url);
        var invocation = (ReceiveInvocation<?>) request.invocation();
        Result result = invocation.callee().receive(invocation);
        boolean oneway = request.oneway();
        if (!oneway) {
            protocol(event).sendResponse(event.channel(), invocation, result);
        }
    }

    @Override
    protected void jvmShuttingDown(RequestEvent event) {
        logger.debug("Received <{}> but jvm is shutting down", ObjectUtil.simpleClassName(event));
        RpcException e = new RpcException("Server closing and no longer processing requests");
        Request request = event.source();
        protocol(event).sendResponse(event.channel(), (ReceiveInvocation<?>) request.invocation(), new Result(request.url(), e));
    }

    private Protocol protocol(RequestEvent event) {
        return ExtensionLoader.loadExtension(Protocol.class, event.source().url().protocol());
    }
}
