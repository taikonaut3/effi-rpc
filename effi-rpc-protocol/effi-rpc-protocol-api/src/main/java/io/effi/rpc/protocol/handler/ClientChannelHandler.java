package io.effi.rpc.protocol.handler;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.common.extension.RpcContext;
import io.effi.rpc.core.ReplyFuture;
import io.effi.rpc.protocol.event.ClientHandlerExceptionEvent;
import io.effi.rpc.protocol.event.ResponseEvent;
import io.effi.rpc.transport.Response;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.channel.ChannelMessage;

/**
 * Client ChannelHandler.
 */
public final class ClientChannelHandler implements ChannelHandler {

    @Override
    public void received(ChannelMessage msg) throws RpcException {
        if (msg.message() instanceof Response response) {
            msg.channel().portal().publishEvent(new ResponseEvent(response));
        }
    }

    @Override
    public void caught(Channel channel, Throwable cause) throws RpcException {
        ReplyFuture future = RpcContext.currentContext().get(ReplyFuture.ATTRIBUTE_KEY);
        channel.portal().publishEvent(new ClientHandlerExceptionEvent(future, channel, cause));
    }

}
