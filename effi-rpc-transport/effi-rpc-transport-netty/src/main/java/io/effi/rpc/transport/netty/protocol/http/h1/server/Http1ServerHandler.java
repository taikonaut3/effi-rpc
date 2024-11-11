package io.effi.rpc.transport.netty.protocol.http.h1.server;

import io.effi.rpc.common.url.URL;
import io.effi.rpc.transport.http.HttpResponse;
import io.effi.rpc.transport.netty.NettySupport;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpRequest;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * Http server message converter.
 */
@Sharable
public final class Http1ServerHandler extends ChannelDuplexHandler {

    private final URL endpointUrl;

    public Http1ServerHandler(URL endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest fullHttpRequest) {
            msg = NettySupport.fromFullHttpRequest(endpointUrl, fullHttpRequest);
        }
        super.channelRead(ctx, msg);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof HttpResponse<?> response) {
            HttpResponse<byte[]> httpResponse = (HttpResponse<byte[]>) response;
            msg = NettySupport.toFullHttpResponse(httpResponse);
        }
        super.write(ctx, msg, promise);
    }

}
