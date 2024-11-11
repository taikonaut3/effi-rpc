package io.effi.rpc.protocol.handler;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.extension.RpcContext;
import io.effi.rpc.core.CallInvocation;
import io.effi.rpc.core.ReplyFuture;
import io.effi.rpc.transport.Request;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.channel.ChannelMessage;

/**
 * Bind RpcFuture to current context.
 */
public class RpcFutureBinderHandler implements ChannelHandler {

    @Override
    public void sent(ChannelMessage msg) throws RpcException {
        if (msg.message() instanceof Request request) {
            CallInvocation<?> invocation = (CallInvocation<?>) request.invocation();
            ReplyFuture future = ReplyFuture.acquireFuture(invocation.callerUrl());
            if (future != null) {
                RpcContext.currentContext().set(ReplyFuture.ATTRIBUTE_KEY, future);
            }
        }
    }
}
