package io.effi.rpc.transport.netty.protocol.http;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.http.HttpRequest;
import io.effi.rpc.transport.netty.NettySupport;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * Bind request url to current channel.
 */
public abstract class URLBinderChannelHandler extends ChannelDuplexHandler {

    @SuppressWarnings("unchecked")
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof HttpRequest<?> httpRequest && httpRequest.body() instanceof byte[]) {
            writeHttpRequest(ctx, (HttpRequest<byte[]>) httpRequest, promise);
            // bind request url to current channel
            NettySupport.bindURL(httpRequest.url(), ctx.channel());
        } else {
            super.write(ctx, msg, promise);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        URL requestUrl = NettySupport.acquireBoundChannel(ctx.channel());
        if (requestUrl != null) {
            readHttpResponse(ctx, msg, requestUrl);
        } else {
            super.channelRead(ctx, msg);
        }
    }

    protected abstract void writeHttpRequest(ChannelHandlerContext ctx, HttpRequest<byte[]> request, ChannelPromise promise) throws Exception;

    protected abstract void readHttpResponse(ChannelHandlerContext ctx, Object msg, URL requestUrl) throws Exception;
}
