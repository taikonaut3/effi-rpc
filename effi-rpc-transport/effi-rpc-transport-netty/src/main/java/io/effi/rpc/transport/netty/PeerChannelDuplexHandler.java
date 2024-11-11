package io.effi.rpc.transport.netty;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.channel.ChannelHandler;
import io.effi.rpc.transport.channel.ChannelMessage;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * Netty channel handler adapter.
 */
public class PeerChannelDuplexHandler extends ChannelDuplexHandler implements NettyChannelObtain {

    private final ChannelHandler channelHandler;

    private final URL endpointUrl;

    public PeerChannelDuplexHandler(ChannelHandler channelHandler, URL endpointUrl) {
        this.channelHandler = channelHandler;
        this.endpointUrl = endpointUrl;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channelHandler.connected(acquireChannel(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelHandler.disconnected(acquireChannel(ctx));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        channelHandler.received(new ChannelMessage(acquireChannel(ctx), msg));
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        NettyChannel nettyChannel = acquireChannel(ctx);
        NettySupport.registerWriteExceptionHandler(promise, channelHandler, nettyChannel);
        ChannelMessage channelMessage = new ChannelMessage(nettyChannel, msg);
        channelHandler.sent(channelMessage);
        super.write(ctx, channelMessage.message(), promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        channelHandler.caught(acquireChannel(ctx), cause);
    }

    @Override
    public URL endpointUrl() {
        return endpointUrl;
    }
}
