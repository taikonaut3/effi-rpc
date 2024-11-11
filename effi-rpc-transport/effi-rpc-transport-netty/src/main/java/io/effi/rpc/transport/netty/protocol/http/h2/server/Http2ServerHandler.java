package io.effi.rpc.transport.netty.protocol.http.h2.server;

import io.effi.rpc.transport.http.HttpRequest;
import io.effi.rpc.transport.http.HttpResponse;
import io.effi.rpc.transport.netty.ByteBufReader;
import io.effi.rpc.transport.netty.InitializedConfig;
import io.effi.rpc.transport.netty.NettySupport;
import io.effi.rpc.transport.netty.protocol.http.h2.Http2RequestStream;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http2.Http2DataFrame;
import io.netty.handler.codec.http2.Http2FrameStream;
import io.netty.handler.codec.http2.Http2HeadersFrame;
import io.netty.handler.codec.http2.Http2StreamFrame;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * Http2 Server Handler.
 */
@Sharable
public final class Http2ServerHandler extends ChannelDuplexHandler {

    private final InitializedConfig config;

    Http2ServerHandler(InitializedConfig config) {
        this.config = config;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ctx.pipeline().addLast(config.handler());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof HttpResponse<?> response) {
            HttpResponse<byte[]> httpResponse = (HttpResponse<byte[]>) response;
            Http2StreamFrame[] frames = NettySupport.toHttp2StreamFrames(httpResponse);
            Http2FrameStream http2FrameStream = NettySupport.unbindStream(response.url());
            for (Http2StreamFrame frame : frames) {
                ctx.write(frame.stream(http2FrameStream), ctx.newPromise());
            }
            ctx.flush();
        } else {
            super.write(ctx, msg, promise);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Http2RequestStream requestStream = null;
        if (msg instanceof Http2HeadersFrame headersFrame) {
            requestStream = onHeadersRead(ctx, headersFrame);
        } else if (msg instanceof Http2DataFrame dataFrame) {
            requestStream = onDataRead(ctx, dataFrame);
        } else {
            super.channelRead(ctx, msg);
        }
        if (requestStream != null && requestStream.endStream()) {
            HttpRequest<ByteBufReader> httpRequest = NettySupport.fromHtt2RequestStream(requestStream);
            ctx.fireChannelRead(httpRequest);
            NettySupport.bindStream(requestStream.stream(), requestStream.url());
            NettySupport.removeHttp2RequestStream(ctx, requestStream);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * handle headers frame.
     *
     * @param ctx
     * @param headersFrame
     * @throws Exception
     */
    private Http2RequestStream onHeadersRead(ChannelHandlerContext ctx, Http2HeadersFrame headersFrame) throws Exception {
        Http2RequestStream requestStream = NettySupport.acquireHttp2RequestStream(ctx, headersFrame.stream(), config.url());
        requestStream.parseHeaderFrame(headersFrame);
        return requestStream;
    }

    /**
     * Handle data frame.
     *
     * @param ctx
     * @param dataFrame
     * @throws Exception
     */
    private Http2RequestStream onDataRead(ChannelHandlerContext ctx, Http2DataFrame dataFrame) throws Exception {
        Http2RequestStream requestStream = NettySupport.acquireHttp2RequestStream(ctx, dataFrame.stream(), config.url());
        requestStream.parseDataFrame(dataFrame);
        return requestStream;
    }
}
