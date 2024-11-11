package io.effi.rpc.transport.netty.protocol.http.h1.client;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.http.HttpResponse;
import io.effi.rpc.transport.netty.ByteBufReader;
import io.effi.rpc.transport.netty.NettySupport;
import io.effi.rpc.transport.netty.client.NettyPoolClient;
import io.effi.rpc.transport.netty.protocol.http.URLBinderChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.effi.rpc.transport.http.HttpRequest;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * Http client message converter.
 */
@Sharable
public final class Http1ClientHandler extends URLBinderChannelHandler {

    private final NettyPoolClient client;

    public Http1ClientHandler(NettyPoolClient client) {
        this.client = client;
    }

    @Override
    protected void writeHttpRequest(ChannelHandlerContext ctx, HttpRequest<byte[]> request, ChannelPromise promise) throws Exception{
        FullHttpRequest fullHttpRequest = NettySupport.toFullHttpRequest(request);
        ctx.write(fullHttpRequest, promise);
    }

    @Override
    protected void readHttpResponse(ChannelHandlerContext ctx, Object msg, URL requestUrl) throws Exception{
        if (msg instanceof FullHttpResponse fullHttpResponse) {
            HttpResponse<ByteBufReader> httpResponse = NettySupport.fromFullHttpResponse(fullHttpResponse, requestUrl);
            ctx.fireChannelRead(httpResponse);
            NettySupport.unbindURL(ctx.channel());
            client.release(ctx.channel());
        }
    }
}
