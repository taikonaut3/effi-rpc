package io.effi.rpc.protocol.handler;

import io.effi.rpc.common.exception.RpcException;
import io.effi.rpc.protocol.event.RequestEvent;
import io.effi.rpc.protocol.event.ServerHandlerExceptionEvent;
import io.effi.rpc.transport.Request;
import io.effi.rpc.transport.channel.Channel;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.channel.ChannelMessage;

/**
 * Server ChannelHandler.
 */
public final class ServerChannelHandler implements ChannelHandler {

    @Override
    public void received(ChannelMessage msg) {
        Channel channel = msg.channel();
        if (msg.message() instanceof Request request) {
            channel.portal().publishEvent(new RequestEvent(request, channel));
        }
    }

    @Override
    public void caught(Channel channel, Throwable cause) throws RpcException {
        channel.portal().publishEvent(new ServerHandlerExceptionEvent(channel, cause));
    }
}
