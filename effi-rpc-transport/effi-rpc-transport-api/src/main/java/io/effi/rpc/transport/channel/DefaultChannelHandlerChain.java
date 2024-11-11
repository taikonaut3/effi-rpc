package io.effi.rpc.transport.channel;

import io.effi.rpc.common.exception.RpcException;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link ChannelHandlerChain}.
 */
public class DefaultChannelHandlerChain implements ChannelHandlerChain {

    private final List<ChannelHandler> channelHandlers = new ArrayList<>();

    @Override
    public ChannelHandlerChain addLast(ChannelHandler channelHandler) {
        channelHandlers.add(channelHandler);
        return this;
    }

    @Override
    public ChannelHandler[] channelHandlers() {
        return channelHandlers.toArray(ChannelHandler[]::new);
    }

    @Override
    public void connected(Channel channel) throws RpcException {
        for (ChannelHandler channelHandler : channelHandlers) {
            channelHandler.connected(channel);
        }
    }

    @Override
    public void disconnected(Channel channel) throws RpcException {
        for (ChannelHandler channelHandler : channelHandlers) {
            channelHandler.disconnected(channel);
        }
    }

    @Override
    public void received(ChannelMessage msg) throws RpcException {
        for (ChannelHandler channelHandler : channelHandlers) {
            channelHandler.received(msg);
        }
    }

    @Override
    public void sent(ChannelMessage msg) throws RpcException {
        for (ChannelHandler channelHandler : channelHandlers) {
            channelHandler.sent(msg);
        }
    }

    @Override
    public void caught(Channel channel, Throwable cause) throws RpcException {
        for (ChannelHandler channelHandler : channelHandlers) {
            channelHandler.caught(channel, cause);
        }
    }

}
